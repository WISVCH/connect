package samltest;

import ch.wisv.dienst2.apiclient.model.Person;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

/**
 * CH Authentication Token
 */
public class CHAuthenticationToken extends ExpiringUsernameAuthenticationToken {

    private Person person;
    private Authentication originalAuthentication;

    public static CHAuthenticationToken createAuthenticationToken(Authentication originalAuthentication, Person person) {
        Date tokenExpiration;
        if (originalAuthentication instanceof ExpiringUsernameAuthenticationToken) {
            tokenExpiration = ((ExpiringUsernameAuthenticationToken) originalAuthentication).getTokenExpiration();
        } else {
            tokenExpiration = Date.from(Instant.now().plusSeconds(8L * 3600L));
        }
        String principal = "WISVCH." + person.getId();
        Collection<? extends GrantedAuthority> authorities = originalAuthentication.getAuthorities();
        return new CHAuthenticationToken(tokenExpiration, principal, person, authorities,
                originalAuthentication);
    }

    public CHAuthenticationToken(Date tokenExpiration, Object principal, Person person, Collection<? extends
            GrantedAuthority> authorities, Authentication originalAuthentication) {
        super(tokenExpiration, principal, null, authorities);
        this.originalAuthentication = originalAuthentication;
        this.person = person;
    }


    @Override
    public void eraseCredentials() {
        if (originalAuthentication instanceof CredentialsContainer) {
            ((CredentialsContainer) originalAuthentication).eraseCredentials();
        }
    }

    public Person getPerson() {
        return person;
    }
}
