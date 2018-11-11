package ch.wisv.connect.overlay.authentication;

import io.opentracing.Scope;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;
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
        Tracer tracer = GlobalTracer.get();

        if (log.isTraceEnabled()) {
            log.trace("Pre-delegate authentication type: {}", authentication.getClass().toString());
        }

        try (Scope scope = tracer.buildSpan("AuthenticationDelegate").startActive(true)) {
            scope.span().setTag("class", delegate.getClass().getName());
            authentication = delegate.authenticate(authentication);
        }

        if (log.isTraceEnabled()) {
            log.trace("Post-delegate authentication type: {}", authentication.getClass().toGenericString());
        }

        try (Scope scope = tracer.buildSpan("AuthenticationFilter").startActive(true)) {
            scope.span().setTag("class", filter.getClass().getName());
            authentication = filter.authenticate(authentication);
        }

        if (log.isTraceEnabled()) {
            log.trace("Post-filter authentication type: {}", authentication.getClass().toGenericString());
        }

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}
