package com.aqualen.kafkaconnect.rest.util;

import com.aqualen.kafkaconnect.rest.enums.AuthType;
import org.apache.kafka.common.config.ConfigException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumValidatorTest {

    @Test
    void ensureValid() {
        assertDoesNotThrow(() -> EnumValidator.in(AuthType.values()).ensureValid(null, "NONE"));
    }

    @Test
    void ensureNotValid() {
        AuthType[] values = AuthType.values();
        EnumValidator validator = EnumValidator.in(values);

        assertThrows(ConfigException.class, () -> validator.ensureValid(null, "API_KEY"));
    }
}