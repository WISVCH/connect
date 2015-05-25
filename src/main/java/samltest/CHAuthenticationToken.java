package samltest;

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

    private int memberNumber;
    private Authentication originalAuthentication;

    public static CHAuthenticationToken createAuthenticationToken(Authentication originalAuthentication, int
            memberNumber) {
        Date tokenExpiration;
        if (originalAuthentication instanceof ExpiringUsernameAuthenticationToken) {
            tokenExpiration = ((ExpiringUsernameAuthenticationToken) originalAuthentication).getTokenExpiration();
        } else {
            tokenExpiration = Date.from(Instant.now().plusSeconds(8L * 3600L));
        }
        String principal = "WISVCH." + memberNumber;
        Collection<? extends GrantedAuthority> authorities = originalAuthentication.getAuthorities();
        return new CHAuthenticationToken(tokenExpiration, principal, null, authorities,
                originalAuthentication, memberNumber);
    }

    public CHAuthenticationToken(Date tokenExpiration, Object principal, Object credentials, Collection<? extends
            GrantedAuthority> authorities, Authentication originalAuthentication, int memberNumber) {
        super(tokenExpiration, principal, credentials, authorities);
        this.originalAuthentication = originalAuthentication;
        this.memberNumber = memberNumber;
    }


    @Override
    public void eraseCredentials() {
        if (originalAuthentication instanceof CredentialsContainer) {
            ((CredentialsContainer) originalAuthentication).eraseCredentials();
        }
    }

    public int getMemberNumber() {
        return memberNumber;
    }
}
