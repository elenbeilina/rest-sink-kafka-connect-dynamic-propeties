package com.aqualen.kafkaconnect.rest.enums;

import com.aqualen.kafkaconnect.rest.service.http.methods.PostService;
import com.aqualen.kafkaconnect.rest.service.http.methods.RestService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * Enum for configuring which implementation of RestService to call.
 * If needed to add new rest implementation - add
 * new http method as enum value with class to implement.
 */
@AllArgsConstructor
public enum HttpMethod {
    POST(PostService.class);

    private final Class<? extends RestService> value;

    @SneakyThrows
    public static RestService resolveHttpService(HttpMethod httpMethod) {
        return httpMethod.value
                .getDeclaredConstructor()
                .newInstance();
    }
}
