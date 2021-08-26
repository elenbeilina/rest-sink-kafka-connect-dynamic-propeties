package com.aqualen.kafkaconnect.rest.config;

import okhttp3.Headers;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static com.aqualen.kafkaconnect.rest.util.ConfigurationUtil.REQUIRED_PROPS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class AuthConfigTest {

    @Test
    void testWithoutMandatoryParams() {
        Map<String, String> emptyProp = Collections.emptyMap();

        assertDoesNotThrow(() -> new AuthConfig(emptyProp));
    }

    @Test
    void testWithMandatoryParams() {
        assertDoesNotThrow(() -> new AuthConfig(REQUIRED_PROPS));
    }

    @Test
    void getAuthHeaders() {
        REQUIRED_PROPS.put("auth.headers", "1:test1,2:test2");
        AuthConfig config = new AuthConfig(REQUIRED_PROPS);

        Headers authHeaders = config.getAuthHeaders();

        assertThat(authHeaders).isNotNull();
        assertThat(authHeaders.size()).isEqualTo(2);
        assertThat(authHeaders.get("1")).isEqualTo("test1");
    }

    @Test
    void getAuthQueryParams() {
        REQUIRED_PROPS.put("auth.query.params", "1:test1,2:test2");
        AuthConfig config = new AuthConfig(REQUIRED_PROPS);

        Map<String, String> queryParams = config.getAuthQueryParams();

        assertThat(queryParams)
                .isNotNull()
                .hasSize(2)
                .containsEntry("1", "test1");
    }
}