package ch.wisv.connect.authentication;

import org.opensaml.saml2.core.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.security.saml.SAMLCredential;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CH Authentication Provider
 */
public class CHAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CHAuthenticationProvider.class);

    @Autowired
    CHUserDetailsService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // TODO: UserCache
        Object credentials = authentication.getCredentials();
        Object principal = authentication.getPrincipal();

        if (credentials != null && credentials instanceof SAMLCredential) {
            SAMLCredential samlCredential = (SAMLCredential) credentials;
            List<Attribute> attributes = samlCredential.getAttributes();
            if (log.isDebugEnabled()) {
                String attributesString = attributes.stream().map(Attribute::getName).map(n -> n + ": " +
                        samlCredential.getAttributeAsString(n)).collect(Collectors.joining("; "));
                log.debug("Authenticated: " + authentication.getName() + " - " + attributesString);
            }

            String organization = samlCredential.getAttributeAsString("organization");
            if (!"tudelft".equals(organization)) {
                throw new IllegalArgumentException("Organization not supported");
            }
            String netid = samlCredential.getAttributeAsString("uid");
            netid = netid.substring(0, netid.indexOf('@'));
            String studentNumber = samlCredential.getAttributeAsString("tudStudentNumber");
            UserDetails userDetails = userDetailService.loadUserByNetidStudentNumber(netid, studentNumber);
            return CHAuthenticationToken.createAuthenticationToken(authentication, userDetails);
        } else if (principal != null && principal instanceof LdapUserDetails) {
            LdapUserDetails ldapUserDetails = (LdapUserDetails) principal;
            String ldapUsername = ldapUserDetails.getUsername();
            log.debug("Authenticated: " + ldapUsername);

            UserDetails userDetails = userDetailService.loadUserByUsername(ldapUsername);
            return CHAuthenticationToken.createAuthenticationToken(authentication, userDetails);
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
