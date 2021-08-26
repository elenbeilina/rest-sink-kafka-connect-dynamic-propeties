package com.aqualen.kafkaconnect.rest.util;

import org.junit.jupiter.api.Test;

import static com.aqualen.kafkaconnect.rest.util.PropertiesUtil.getConnectorVersion;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PropertiesUtilTest {

    @Test
    void getConnectorVersionTest() {
        String version = getConnectorVersion();

        assertEquals("1.0.0", version);
    }
}