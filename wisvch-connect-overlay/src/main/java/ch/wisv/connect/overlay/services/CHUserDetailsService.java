package ch.wisv.connect.overlay.services;

import ch.wisv.connect.overlay.model.CHUserDetails;
import ch.wisv.dienst2.apiclient.model.Member;
import ch.wisv.dienst2.apiclient.model.Person;
import ch.wisv.dienst2.apiclient.model.Student;
import ch.wisv.dienst2.apiclient.util.Dienst2Repository;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.SpringSecurityLdapTemplate;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchControls;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * CH User Details Service
 * <p>
 * Loads Dienst2 Person object and LDAP groups with Dienst2 LDAP username.
 */
@Service
public class CHUserDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(CHUserDetailsService.class);

    @Autowired
    private Dienst2Repository dienst2Repository;
    private final SpringSecurityLdapTemplate ldapTemplate;

    @Autowired
    public CHUserDetailsService(LdapContextSource contextSource) {
        ldapTemplate = new SpringSecurityLdapTemplate(contextSource);
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        ldapTemplate.setSearchControls(searchControls);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws CHAuthenticationException {
        try {
            Person person = verifyMembership(dienst2Repository.getPersonFromLdapUsername(username));
            return createUserDetails(person, CHUserDetails.AuthenticationSource.CH_LDAP);
        } catch (Exception e) {
            log.error("Could not load user details by username", e);
            throw e;
        }
    }

    /**
     * Look up user by NetID and/or student number.
     * <p>
     * Objects from Dienst2 are checked for conflicts: if both NetID and student number are set, they need to match our
     * parameters. If either is set, we check for conflicting matches.
     * <p>
     * This code assumes exact matching and uniqueness constraints for NetID and student number in Dienst2.
     *
     * @param netid         NetID to look for
     * @param studentNumber Student number to be matched
     * @return user details object
     * @throws CHAuthenticationException
     */
    public UserDetails loadUserByNetidStudentNumber(String netid, String studentNumber) throws
            CHAuthenticationException {
        assert StringUtils.isNotBlank(netid);

        // First look up by NetID
        Optional<Person> personFromNetid = dienst2Repository.getPersonFromNetid(netid);
        if (personFromNetid.isPresent()) {
            Person person = verifyMembership(personFromNetid);
            if (StringUtils.isNotBlank(studentNumber)) {
                // Check if Dienst2 student number matches parameter, if not: conflict
                String matchedStudentNumber = person.getStudent().map(Student::getStudentNumber).orElse("");
                if (StringUtils.isBlank(matchedStudentNumber) || !studentNumber.equals(matchedStudentNumber)) {
                    throw new CHMemberConflictException();
                }
            }
            // Student number parameter was blank, or it matched Dienst2 student number: we're good
            return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
        } else if (StringUtils.isBlank(studentNumber)) {
            // No match, and we cannot search by student number: invalid member
            throw new CHInvalidMemberException();
        }

        // Person not found by NetID, or Dienst2 student number was empty: we need to check for conflicts
        Optional<Person> personFromStudentNumber = dienst2Repository.getPersonFromStudentNumber(studentNumber);
        if (personFromStudentNumber.isPresent() && personFromNetid.isPresent()) {
            // We found two different matches: conflict
            throw new CHMemberConflictException();
        } else if (personFromNetid.isPresent()) {
            // No match by student number, so NetID result was fine: we're good
            return createUserDetails(verifyMembership(personFromNetid), CHUserDetails.AuthenticationSource.TU_SSO);
        } else if (personFromStudentNumber.isPresent()) {
            Person person = verifyMembership(personFromStudentNumber);
            // If Dienst2 parameter is set, it should have matched earlier: conflict
            if (StringUtils.isNotBlank(person.getNetid())) {
                throw new CHMemberConflictException();
            } else {
                return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
            }
        } else {
            // No matches: invalid member
            throw new CHInvalidMemberException();
        }
    }

    private CHUserDetails createUserDetails(Person person, CHUserDetails.AuthenticationSource authenticationSource) {
        assert person != null;
        // TODO: externalise search options
        String ldapUsername = person.getLdapUsername();
        Set<String> ldapGroups = Collections.emptySet();
        if (StringUtils.isNotEmpty(ldapUsername)) {
            String dn = String.format("uid=%s,ou=People,dc=ank,dc=chnet", ldapUsername);
            ldapGroups = ldapTemplate.searchForSingleAttributeValues("ou=Group", "memberUid={1}", new String[]{dn,
                    ldapUsername}, "cn");
            if (log.isDebugEnabled()) {
                log.debug("LDAP roles from search: " + ldapGroups);
            }
        }
        return new CHUserDetails(person, ldapGroups, authenticationSource);
    }

    public CHUserDetails loadUserById(int id) throws CHAuthenticationException {
        Person person = verifyMembership(dienst2Repository.getPerson(id));
        return createUserDetails(person, null);
    }

    private Person verifyMembership(Optional<Person> person) {
        return person.filter(this::verifyMembership).orElseThrow(CHInvalidMemberException::new);
    }

    public boolean verifyMembership(Person person) {
        return person.getMember().map(Member::isCurrentMember).orElse(false);
    }

    public abstract class CHAuthenticationException extends UsernameNotFoundException {
        public CHAuthenticationException(String msg) {
            super(msg);
        }
    }

    public class CHInvalidMemberException extends CHAuthenticationException {

        public CHInvalidMemberException() {
            super("Not a valid CH member");
        }
    }

    public class CHMemberConflictException extends CHAuthenticationException {

        public CHMemberConflictException() {
            super("Conflict between NetID and student number");
        }
    }
}
