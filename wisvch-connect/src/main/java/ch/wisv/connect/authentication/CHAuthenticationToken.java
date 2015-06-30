package ch.wisv.connect.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.providers.ExpiringUsernameAuthenticationToken;

import java.time.Instant;
import java.util.Collection;
import java.util.Date;

/**
 * CH Authentication Token
 */
public class CHAuthenticationToken extends ExpiringUsernameAuthenticationToken {

    private Authentication originalAuthentication;

    public static CHAuthenticationToken createAuthenticationToken(Authentication originalAuthentication,
                                                                  UserDetails userDetails) {
        Date tokenExpiration;
        if (originalAuthentication instanceof ExpiringUsernameAuthenticationToken) {
            tokenExpiration = ((ExpiringUsernameAuthenticationToken) originalAuthentication).getTokenExpiration();
        } else {
            tokenExpiration = Date.from(Instant.now().plusSeconds(8L * 3600L));
        }
        return new CHAuthenticationToken(tokenExpiration, userDetails, userDetails.getAuthorities(),
                originalAuthentication);
    }

    public CHAuthenticationToken(Date tokenExpiration, Object principal, Collection<? extends
            GrantedAuthority> authorities, Authentication originalAuthentication) {
        super(tokenExpiration, principal, null, authorities);
        this.originalAuthentication = originalAuthentication;
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
}
