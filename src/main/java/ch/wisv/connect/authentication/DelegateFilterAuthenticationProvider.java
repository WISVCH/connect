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
        if (log.isTraceEnabled()) {
            log.trace("Pre-delegate authentication type: {}", authentication.getClass().toString());
        }

        authentication = delegate.authenticate(authentication);

        if (log.isTraceEnabled()) {
            log.trace("Post-delegate authentication type: {}", authentication.getClass().toGenericString());
        }

        authentication = filter.authenticate(authentication);

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
