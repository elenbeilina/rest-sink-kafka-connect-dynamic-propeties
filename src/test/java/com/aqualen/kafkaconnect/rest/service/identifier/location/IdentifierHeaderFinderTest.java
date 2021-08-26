package com.aqualen.kafkaconnect.rest.service.identifier.location;

import com.aqualen.kafkaconnect.rest.config.IdentifierRestCallBinderConfig;
import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import org.apache.kafka.common.record.TimestampType;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.header.ConnectHeaders;
import org.apache.kafka.connect.header.Headers;
import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class IdentifierHeaderFinderTest {

    @Spy
    private final IdentifierRestCallBinderConfig config =
            new IdentifierRestCallBinderConfig(ConfigurationUtil.REQUIRED_PROPS);
    @InjectMocks
    private IdentifierHeaderFinder finder;

    @Test
    void findIdentifier() {
        Headers headers = new ConnectHeaders();
        headers.add("test", "test_header", Schema.STRING_SCHEMA);

        SinkRecord record = new SinkRecord("test", 1, Schema.STRING_SCHEMA, "test",
                Schema.STRING_SCHEMA, "{\"test\":\"test\"}", 1,
                new Date().getTime(), TimestampType.CREATE_TIME, headers);

        String identifier = finder.findIdentifier(record);

        assertThat(identifier).isEqualTo("test_header");
    }
}