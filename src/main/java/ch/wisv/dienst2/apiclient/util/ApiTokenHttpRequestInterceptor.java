package ch.wisv.dienst2.apiclient.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Request interceptor to add API token
 */
public class ApiTokenHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private String apiToken;

    public ApiTokenHttpRequestInterceptor(String apiToken) {
        this.apiToken = apiToken;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Token " + apiToken);
        request.getHeaders().set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        request.getHeaders().set(HttpHeaders.ACCEPT_LANGUAGE, "en");
        return execution.execute(request, body);
    }
}
