package com.aqualen.kafkaconnect.rest.enums;

import com.aqualen.kafkaconnect.rest.config.IdentifierRestCallBinderConfig;
import com.aqualen.kafkaconnect.rest.service.identifier.location.IdentifierBodyFinder;
import com.aqualen.kafkaconnect.rest.service.identifier.location.IdentifierFinder;
import com.aqualen.kafkaconnect.rest.service.identifier.location.IdentifierHeaderFinder;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

/**
 * Enum for configuring which implementation of IdentifierFinder to call.
 * If needed to add new finder implementation - add
 * new identifier location as enum value with class to implement.
 */
@AllArgsConstructor
public enum IdentifierLocation {
    HEADER(IdentifierHeaderFinder.class),
    BODY(IdentifierBodyFinder.class);

    private final Class<? extends IdentifierFinder> value;

    @SneakyThrows
    public static IdentifierFinder resolveIdentifierFinder(IdentifierLocation location,
                                                           IdentifierRestCallBinderConfig config) {
        return location.value
                .getDeclaredConstructor(IdentifierRestCallBinderConfig.class)
                .newInstance(config);
    }
}
