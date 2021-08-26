package com.aqualen.kafkaconnect.rest.service;

import com.aqualen.kafkaconnect.rest.enums.HttpMethod;
import com.aqualen.kafkaconnect.rest.service.identifier.IdentifierRestCallBinder;
import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import okhttp3.Request;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifierRestCallBinderTest {

    private IdentifierRestCallBinder binder;

    @BeforeEach
    void setUp() {
        binder = new IdentifierRestCallBinder();
        binder.configure(ConfigurationUtil.REQUIRED_PROPS);
    }

    @Test
    void constructRequestByIdentifier() {
        SinkRecord record = new SinkRecord("test", 1, Schema.STRING_SCHEMA, "test",
                Schema.STRING_SCHEMA, "{\"test\":\"Test Values\"}", 1);

        Request request = binder.constructRequestByIdentifier(record).build();

        assertThat(request).isNotNull();
        assertThat(request.url().uri()).hasToString(
                ConfigurationUtil.REQUIRED_PROPS.get("api.base.uri") + ConfigurationUtil.REQUIRED_PROPS.get("identifier.test-values.uri"));
        assertThat(request.method()).isEqualTo(HttpMethod.POST.name());
    }
}