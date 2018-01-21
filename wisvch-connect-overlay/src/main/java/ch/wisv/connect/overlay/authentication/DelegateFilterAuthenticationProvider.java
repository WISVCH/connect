package ch.wisv.connect.overlay.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Authentication Provider that performs delegates then filters
 */
public class DelegateFilterAuthenticationProvider implements AuthenticationProvider {
    private static final Logger log = LoggerFactory.getLogger(DelegateFilterAuthenticationProvider.class);

    private final AuthenticationProvider delegate;
    private final AuthenticationProvider filter;

    public DelegateFilterAuthenticationProvider(AuthenticationProvider delegate, AuthenticationProvider filter) {
        this.delegate = delegate;
        this.filter = filter;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (log.isDebugEnabled()) {
            log.debug("Pre-delegate authentication type: {}", authentication.getClass().toGenericString());
        }

        authentication = delegate.authenticate(authentication);
        if (log.isDebugEnabled()) {
            log.debug("Post-delegate authentication type: {}", authentication.getClass().toGenericString());
        }

        authentication = filter.authenticate(authentication);
        if (log.isDebugEnabled()) {
            log.debug("Post-filter authentication type: {}", authentication.getClass().toGenericString());
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}
