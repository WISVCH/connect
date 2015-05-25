package samltest;

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
            try {
                int memberNumber = dienst2Repository.verifyMembershipFromStudentNumber(samlStudentNumber);
                log.info("CH Member #{}", memberNumber);
                return CHAuthenticationToken.createAuthenticationToken(authentication, memberNumber);
            } catch (Dienst2Repository.Dienst2Exception e) {
                log.error("Dienst2 error", e);
                throw new InsufficientAuthenticationException("Member check failed", e);
            }
        } else if (principal != null && principal instanceof LdapUserDetails) {
            LdapUserDetails ldapUserDetails = (LdapUserDetails) principal;
            String ldapUsername = ldapUserDetails.getUsername();
            log.info("Authenticated: " + ldapUsername);

            try {
                int memberNumber = dienst2Repository.verifyMembershipFromLdapUsername(ldapUsername);
                log.info("CH Member #{}", memberNumber);
                return CHAuthenticationToken.createAuthenticationToken(authentication, memberNumber);
            } catch (Dienst2Repository.Dienst2Exception e) {
                log.error("Dienst2 error", e);
                throw new InsufficientAuthenticationException("Member check failed", e);
            }
        } else {
            log.debug("Invalid authentication object type");
            throw new IllegalArgumentException("Invalid authentication object type");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
