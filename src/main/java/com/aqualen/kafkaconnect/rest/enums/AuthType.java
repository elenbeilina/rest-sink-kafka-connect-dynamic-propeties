package com.aqualen.kafkaconnect.rest.enums;

import com.aqualen.kafkaconnect.rest.service.authorization.AuthorizationService;
import com.aqualen.kafkaconnect.rest.service.authorization.TokenAuthorizationService;
import com.aqualen.kafkaconnect.rest.service.authorization.WithoutAuthorizationService;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum for configuring which implementation of AuthorizationService to call.
 * If needed to add new auth implementation - add
 * new auth type as enum with value as class to implement.
 */
@Getter
@AllArgsConstructor
public enum AuthType {
    NONE(WithoutAuthorizationService.class),
    BEARER_TOKEN(TokenAuthorizationService.class);

    private final Class<? extends AuthorizationService> value;
}
