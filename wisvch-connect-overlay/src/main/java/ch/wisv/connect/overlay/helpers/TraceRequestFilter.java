/*
 * Copyright 2015-2018 W.I.S.V. 'Christiaan Huygens'
 * Copyright 2017 The MITRE Corporation
 *    and the MIT Internet Trust Consortium
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

package ch.wisv.connect.overlay.helpers;

import datadog.trace.api.CorrelationIdentifier;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class TraceRequestFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            ThreadContext.put("ddTraceID", " ddTraceID:" + String.valueOf(CorrelationIdentifier.getTraceId()));
            ThreadContext.put("ddSpanID", " ddSpanID:" + String.valueOf(CorrelationIdentifier.getSpanId()));
            chain.doFilter(request, response);
        } finally {
            ThreadContext.remove("ddTraceID");
            ThreadContext.remove("ddSpanID");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
