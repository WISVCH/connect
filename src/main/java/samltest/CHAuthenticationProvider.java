package samltest;

import ch.wisv.dienst2.apiclient.model.Member;
import ch.wisv.dienst2.apiclient.model.Person;
import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.saml.SAMLCredential;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * CH Authentication Provider
 */
public class CHAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(DelegateFilterAuthenticationProvider.class);

    @Autowired
    private Dienst2Repository dienst2Repository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // TODO: dingen met LDAP?

        Object credentials = authentication.getCredentials();
        Object principal = authentication.getPrincipal();

        if (credentials != null && credentials instanceof SAMLCredential) {
            SAMLCredential samlCredential = (SAMLCredential) credentials;
            List<Attribute> attributes = samlCredential.getAttributes();
            String attributesString = attributes.stream().map(Attribute::getName).map(n -> n + ": " + samlCredential
                    .getAttributeAsString(n)).collect(Collectors.joining("; "));
            log.info("Authenticated: " + authentication.getName() + " - " + attributesString);

            String samlStudentNumber = samlCredential.getAttributeAsString("tudStudentNumber");
            Person person = getPersonFromStudentNumber(samlStudentNumber);
            return CHAuthenticationToken.createAuthenticationToken(authentication, person);
        } else if (principal != null && principal instanceof LdapUserDetails) {
            LdapUserDetails ldapUserDetails = (LdapUserDetails) principal;
            String ldapUsername = ldapUserDetails.getUsername();
            log.info("Authenticated: " + ldapUsername);
            Person person = getPersonFromLdapUsername(ldapUsername);
            return CHAuthenticationToken.createAuthenticationToken(authentication, person);
        } else {
            log.debug("Invalid authentication object type");
            throw new IllegalArgumentException("Invalid authentication object type");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    private Person getPersonFromStudentNumber(String studentNumber) {
        // If we get a NetID username from SAML, we should verify it against Dienst2 as well.
        return verifyMembership(dienst2Repository.getPersonFromStudentNumber(studentNumber));
    }

    private Person getPersonFromLdapUsername(String studentNumber) {
        return verifyMembership(dienst2Repository.getPersonFromLdapUsername(studentNumber));
    }

    private Person verifyMembership(Optional<Person> person) {
        return person.filter(this::verifyMembership).orElseThrow(() -> new InsufficientAuthenticationException("Not a CH member"));
    }

    public boolean verifyMembership(Person person) {
        return person.getMember().map(Member::isCurrentMember).orElse(false);
    }
}
