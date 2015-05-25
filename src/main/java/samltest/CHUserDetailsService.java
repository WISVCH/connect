package samltest;

import ch.wisv.dienst2.apiclient.model.Member;
import ch.wisv.dienst2.apiclient.model.Person;
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
 *
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = verifyMembership(dienst2Repository.getPersonFromLdapUsername(username));
        return createUserDetails(person);
    }

    public UserDetails loadUserByStudentNumber(String studentNumber) throws UsernameNotFoundException {
        // If we get a NetID username from SAML, we should verify it against Dienst2 as well.
        Person person = verifyMembership(dienst2Repository.getPersonFromStudentNumber(studentNumber));
        return createUserDetails(person);
    }

    private CHUserDetails createUserDetails(Person person) {
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
        return new CHUserDetails(person, ldapGroups);
    }

    private Person verifyMembership(Optional<Person> person) {
        return person.filter(this::verifyMembership).orElseThrow(() ->
                new CHMemberNotFoundException("Not a valid CH member"));
    }

    public boolean verifyMembership(Person person) {
        return person.getMember().map(Member::isCurrentMember).orElse(false);
    }

    public class CHMemberNotFoundException extends UsernameNotFoundException {
        public CHMemberNotFoundException(String msg) {
            super(msg);
        }

        public CHMemberNotFoundException(String msg, Throwable t) {
            super(msg, t);
        }
    }
}
