package com.aqualen.kafkaconnect.rest.service.identifier.location;

import com.aqualen.kafkaconnect.rest.config.IdentifierRestCallBinderConfig;
import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class IdentifierBodyFinderTest {
    @Spy
    private final IdentifierRestCallBinderConfig config =
            new IdentifierRestCallBinderConfig(ConfigurationUtil.REQUIRED_PROPS);
    @InjectMocks
    private IdentifierBodyFinder finder;

    @Test
    void findIdentifier() {
        JSONObject object = new JSONObject();
        object.put("test", "test");

        String identifier = finder.findIdentifier(object);

        assertThat(identifier).isEqualTo(object.getString("test"));
    }
}