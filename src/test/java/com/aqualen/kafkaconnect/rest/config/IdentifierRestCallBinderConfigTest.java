package com.aqualen.kafkaconnect.rest.config;

import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import org.apache.kafka.common.config.ConfigException;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdentifierRestCallBinderConfigTest {
    @Test
    void testWithoutMandatoryParams() {
        Map<String, String> emptyProp = Collections.emptyMap();

        assertThrows(ConfigException.class, () -> new IdentifierRestCallBinderConfig(emptyProp));
    }

    @Test
    void testWithMandatoryParams() {
        assertDoesNotThrow(() -> new IdentifierRestCallBinderConfig(ConfigurationUtil.REQUIRED_PROPS));
    }

    @Test
    void testIdentifierValidation(){
        Map<String, String> propsWithoutUri = new HashMap<>(ConfigurationUtil.REQUIRED_PROPS);
        propsWithoutUri.remove("identifier.test-values.uri");

        assertThrows(ConfigException.class, () -> new IdentifierRestCallBinderConfig(propsWithoutUri));
    }
}