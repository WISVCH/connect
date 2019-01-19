/*
 * Copyright 2019 W.I.S.V. 'Christiaan Huygens'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.wisv.connect.authentication;

import ch.wisv.connect.model.CHUserDetails;
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

    private Authentication originalAuthentication;

    public static CHAuthenticationToken createAuthenticationToken(Authentication originalAuthentication,
                                                                  CHUserDetails userDetails) {
        Date tokenExpiration;
        if (originalAuthentication instanceof ExpiringUsernameAuthenticationToken) {
            tokenExpiration = ((ExpiringUsernameAuthenticationToken) originalAuthentication).getTokenExpiration();
        } else {
            tokenExpiration = Date.from(Instant.now().plusSeconds(8L * 3600L));
        }
        return new CHAuthenticationToken(tokenExpiration, userDetails.getUsername(), userDetails.getAuthorities(),
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
