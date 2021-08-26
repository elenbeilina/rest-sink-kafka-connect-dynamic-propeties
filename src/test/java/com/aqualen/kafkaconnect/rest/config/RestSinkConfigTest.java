package com.aqualen.kafkaconnect.rest.config;

import org.apache.kafka.common.config.ConfigException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static com.aqualen.kafkaconnect.rest.util.ConfigurationUtil.REQUIRED_PROPS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RestSinkConfigTest {
    @Test
    void testWithoutMandatoryParams() {
        Map<String, String> emptyProp = Collections.emptyMap();

        assertThrows(ConfigException.class, () -> new RestSinkConfig(emptyProp));
    }

    @Test
    void testWithMandatoryParams() {
        assertDoesNotThrow(() -> new RestSinkConfig(REQUIRED_PROPS));
    }
}