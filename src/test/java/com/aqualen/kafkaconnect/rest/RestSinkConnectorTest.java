package com.aqualen.kafkaconnect.rest;

import com.aqualen.kafkaconnect.rest.config.RestSinkConfig;
import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import com.aqualen.kafkaconnect.rest.util.PropertiesUtil;
import org.apache.kafka.connect.errors.ConnectException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestSinkConnectorTest {

    @Mock
    private RestSinkConfig config;
    @InjectMocks
    private RestSinkConnector connector;

    @Test
    void failStart() {
        Map<String, String> emptyProp = Collections.emptyMap();

        assertThrows(ConnectException.class, () -> connector.start(emptyProp));
    }

    @Test
    void start() {
        assertDoesNotThrow(() -> connector.start(ConfigurationUtil.REQUIRED_PROPS));
    }

    @Test
    void taskConfigs() {
        when(config.originalsStrings()).thenReturn(ConfigurationUtil.REQUIRED_PROPS);

        List<Map<String, String>> configs = connector.taskConfigs(2);

        assertThat(configs).hasSize(2);

        Map<String, String> config = configs.get(0);

        assertThat(config).hasSizeGreaterThanOrEqualTo(1);
        assertTrue(config.containsKey("topics"));
    }

    @Test
    void testConfigDefReturnsCorrectConfigDefinition() {
        Assertions.assertEquals(
                RestSinkConfig.conf()
                        .names(),
                connector.config()
                        .names(),
                "Configuration must match"
        );
    }

    @Test
    void testTaskClassReturnsCorrectTask() {
        assertSame(RestSinkTask.class, connector.taskClass(), "Task class is not identical");
    }

    @Test
    void version() {
        String version = PropertiesUtil.getConnectorVersion();
        String connectorVersion = new RestSinkConnector().version();

        assertEquals(version, connectorVersion);
        assertEquals("1.0.0", connectorVersion);
    }
}