package samltest;

import ch.wisv.dienst2.apiclient.model.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * CH Authentication Token
 */
public class CHAuthenticationToken extends ExpiringUsernameAuthenticationToken {

    private Authentication originalAuthentication;
    private Person person;
    private Set<String> ldapRoles;

    public static CHAuthenticationToken createAuthenticationToken(Authentication originalAuthentication, Person
            person, Set<String> ldapRoles) {
        Date tokenExpiration;
        if (originalAuthentication instanceof ExpiringUsernameAuthenticationToken) {
            tokenExpiration = ((ExpiringUsernameAuthenticationToken) originalAuthentication).getTokenExpiration();
        } else {
            tokenExpiration = Date.from(Instant.now().plusSeconds(8L * 3600L));
        }
        String principal = "WISVCH." + person.getId();
        Collection<? extends GrantedAuthority> authorities = originalAuthentication.getAuthorities();
        return new CHAuthenticationToken(tokenExpiration, principal, authorities,
                originalAuthentication, person, ldapRoles);
    }

    public CHAuthenticationToken(Date tokenExpiration, Object principal, Collection<? extends
            GrantedAuthority> authorities, Authentication originalAuthentication, Person person, Set<String>
            ldapRoles) {
        super(tokenExpiration, principal, null, authorities);
        this.originalAuthentication = originalAuthentication;
        this.person = person;
        this.ldapRoles = ldapRoles;
    }


    @Override
    public void eraseCredentials() {
        if (originalAuthentication instanceof CredentialsContainer) {
            ((CredentialsContainer) originalAuthentication).eraseCredentials();
        }
    }

    public Authentication getOriginalAuthentication() {
        return originalAuthentication;
    }

    public Person getPerson() {
        return person;
    }

    public Set<String> getLdapRoles() {
        return ldapRoles;
    }
}
