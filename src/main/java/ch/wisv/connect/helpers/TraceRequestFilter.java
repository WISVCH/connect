/*
 * Copyright 2019 W.I.S.V. 'Christiaan Huygens'
 * Copyright 2018 The MITRE Corporation
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

package ch.wisv.connect.helpers;

import datadog.trace.api.CorrelationIdentifier;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
public class TraceRequestFilter implements Filter {
    private static final String TRACE_ID_KEY = "dd.trace_id";
    private static final String SPAN_ID_KEY = "dd.span_id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            ThreadContext.put(TRACE_ID_KEY, CorrelationIdentifier.getTraceId());
            ThreadContext.put(SPAN_ID_KEY, CorrelationIdentifier.getSpanId());
            chain.doFilter(request, response);
        } finally {
            ThreadContext.remove(TRACE_ID_KEY);
            ThreadContext.remove(SPAN_ID_KEY);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
