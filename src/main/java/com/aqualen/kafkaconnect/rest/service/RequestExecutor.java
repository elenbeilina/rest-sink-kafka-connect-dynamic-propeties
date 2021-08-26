package com.aqualen.kafkaconnect.rest.service;

import com.aqualen.kafkaconnect.rest.service.authorization.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.httpclient.HttpStatus;

import java.util.Objects;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class RequestExecutor {

    private OkHttpClient client;
    private AuthorizationService authService;

    /**
     * Sends the request and takes care of authentication.
     *
     * @param request Request to be sent
     * @return response content
     */
    @SneakyThrows
    public Response sendRequest(Request.Builder request) {
        authService.setAuthorizationHeader(request);

        Response response = client
                .newCall(request.build())
                .execute();

        if (isUnauthorizedResponse(response)) {
            log.info("Attempting re-authentication.");
            authService.authenticate();
        }

        if (Objects.nonNull(response.body())) {
            response.close();
        }

        return response;
    }

    /**
     * @return True if status code is 401
     */
    private static boolean isUnauthorizedResponse(Response response) {
        return response.code() == HttpStatus.SC_UNAUTHORIZED;
    }
}
