package ch.wisv.connect.overlay.services;

import ch.wisv.connect.overlay.model.CHUserDetails;
import ch.wisv.dienst2.apiclient.model.Member;
import ch.wisv.dienst2.apiclient.model.Person;
import ch.wisv.dienst2.apiclient.model.Student;
import ch.wisv.dienst2.apiclient.util.Dienst2Repository;
import com.google.common.base.Preconditions;
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
        } catch (Throwable e) {
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
        Preconditions.checkArgument(StringUtils.isNotBlank(netid), "netid cannot be blank");

        Optional<Person> personFromNetid = dienst2Repository.getPersonFromNetid(netid);
        Optional<Person> personFromStudentNumber = StringUtils.isNotBlank(studentNumber) ?
                dienst2Repository.getPersonFromStudentNumber(studentNumber) : Optional.empty();

        if (personFromNetid.isPresent() && personFromStudentNumber.isPresent()) {
            Person person = verifyMembership(personFromNetid);
            if (personFromStudentNumber.get().getId() != personFromNetid.get().getId()) {
                // We have two different matches: conflict
                log.warn("Conflict: Dienst2 person {} matching student number is not Dienst2 person {} matching NetID",
                        personFromStudentNumber.get().getId(), personFromNetid.get().getId());
                throw new CHMemberConflictException();
            } else {
                return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
            }
        } else if (personFromNetid.isPresent()) {
            Person person = verifyMembership(personFromNetid);
            String dienst2StudentNumber = person.getStudent().map(Student::getStudentNumber).orElse("");
            if (StringUtils.isNotBlank(dienst2StudentNumber)) {
                // If Dienst2 student number is set, it should have matched earlier: conflict
                log.warn("Conflict: Student number {} from Dienst2 does not match student number {} from SAML",
                        dienst2StudentNumber, studentNumber);
                throw new CHMemberConflictException();
            } else {
                // TODO: write back student number to Dienst2
                return createUserDetails(person, CHUserDetails.AuthenticationSource.TU_SSO);
            }
        } else if (personFromStudentNumber.isPresent()) {
            Person person = verifyMembership(personFromStudentNumber);
            String dienst2Netid = person.getNetid();
            if (StringUtils.isNotBlank(dienst2Netid)) {
                // If Dienst2 NetID is set, it should have matched earlier: conflict
                log.warn("Conflict: NetID {} from Dienst2 does not match NetID {} from SAML",
                        dienst2Netid, netid);
                throw new CHMemberConflictException();
            } else {
                // TODO: write back NetID to Dienst2
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
            log.debug("LDAP roles from search: {}", ldapGroups);
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

    public abstract static class CHAuthenticationException extends UsernameNotFoundException {
        public CHAuthenticationException(String msg) {
            super(msg);
        }
    }

    public static class CHInvalidMemberException extends CHAuthenticationException {

        public CHInvalidMemberException() {
            super("Not a valid CH member");
        }
    }

    public static class CHMemberConflictException extends CHAuthenticationException {

        public CHMemberConflictException() {
            super("Conflict between NetID and student number");
        }
    }
}
