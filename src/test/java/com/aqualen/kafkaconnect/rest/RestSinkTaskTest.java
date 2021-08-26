package com.aqualen.kafkaconnect.rest;

import com.aqualen.kafkaconnect.rest.config.RestSinkConfig;
import com.aqualen.kafkaconnect.rest.service.RequestExecutor;
import com.aqualen.kafkaconnect.rest.service.identifier.IdentifierRestCallBinder;
import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import com.aqualen.kafkaconnect.rest.util.PropertiesUtil;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestSinkTaskTest {

    @Mock
    private IdentifierRestCallBinder binder;
    @Mock
    private RequestExecutor executor;
    @Spy
    private final RestSinkConfig config =
            new RestSinkConfig(ConfigurationUtil.REQUIRED_PROPS);
    @InjectMocks
    private RestSinkTask restSinkTask;

    @Test
    void version() {
        String version = PropertiesUtil.getConnectorVersion();
        String connectorVersion = new RestSinkTask().version();

        assertEquals(version, connectorVersion);
        assertEquals("1.0.0", connectorVersion);
    }

    @Test
    void startWithEmptyProps() {
        Map<String, String> emptyProps = Collections.emptyMap();

        assertThrows(ConfigException.class, () -> restSinkTask.start(emptyProps));
    }

    @Test
    void start() {
        assertDoesNotThrow(() -> restSinkTask.start(ConfigurationUtil.REQUIRED_PROPS));
    }

    @Test
    void stop() {
        assertDoesNotThrow(() -> restSinkTask.stop());
    }

    @Test
    void poll() {
        Request.Builder request = new
                Request.Builder().url("https://test");
        Response.Builder response = new Response.Builder()
                .request(request.build())
                .protocol(Protocol.HTTP_2)
                .message("")
                .body(null);
        SinkRecord record = new SinkRecord("test", 1, Schema.STRING_SCHEMA, "test",
                Schema.STRING_SCHEMA, "{\"test\":\"test\"}", 1);

        when(binder.constructRequestByIdentifier(any())).thenReturn(request);
        when(executor.sendRequest(any()))
                .thenReturn(response.code(HttpStatus.SC_FORBIDDEN).build())
                .thenReturn(response.code(HttpStatus.SC_OK).build());

        restSinkTask.put(Collections.singletonList(record));

        verify(executor, times(2)).sendRequest(any());
    }
}