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

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security ExceptionMappingAuthenticationFailureHandler with forward support
 */
public class ExceptionMappingAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final Map<String, String> failureUrlMap = new HashMap<>();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String url = failureUrlMap.get(exception.getClass().getName());

        logger.debug("Exception: " + exception.getClass().getName());
        logger.debug("Message: " + exception.getMessage());
        
        if (url != null) {
            if (isUseForward()) {
                logger.debug("Authentication failure, forwarding to " + url);
                request.getRequestDispatcher(url).forward(request, response);
            } else {
                logger.debug("Authentication failure, redirecting to " + url);
                getRedirectStrategy().sendRedirect(request, response, url);
            }
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }

    /**
     * Sets the map of exception types (by name) to URLs.
     *
     * @param failureUrlMap the map keyed by the fully-qualified name of the exception class, with the corresponding
     *                      failure URL as the value.
     * @throws IllegalArgumentException if the entries are not Strings or the URL is not valid.
     */
    public void setExceptionMappings(Map<?, ?> failureUrlMap) {
        this.failureUrlMap.clear();
        for (Map.Entry<?, ?> entry : failureUrlMap.entrySet()) {
            Object exception = entry.getKey();
            Object url = entry.getValue();
            Assert.isInstanceOf(String.class, exception, "Exception key must be a String (the exception classname).");
            Assert.isInstanceOf(String.class, url, "URL must be a String");
            Assert.isTrue(UrlUtils.isValidRedirectUrl((String) url), "Not a valid redirect URL: " + url);
            this.failureUrlMap.put((String) exception, (String) url);
        }
    }
}
