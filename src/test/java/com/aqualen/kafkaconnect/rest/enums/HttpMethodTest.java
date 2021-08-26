package com.aqualen.kafkaconnect.rest.enums;

import com.aqualen.kafkaconnect.rest.service.http.methods.PostService;
import com.aqualen.kafkaconnect.rest.service.http.methods.RestService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HttpMethodTest {

    @Test
    void resolveService() {
        RestService restService = HttpMethod.resolveHttpService(HttpMethod.POST);

        assertThat(restService)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isExactlyInstanceOf(PostService.class);
    }

    @Test
    void values() {
        HttpMethod[] values = HttpMethod.values();

        Assertions.assertThat(values)
                .isNotNull()
                .hasSizeGreaterThanOrEqualTo(1)
                .contains(HttpMethod.POST);

    }

    @Test
    void valueOf() {
        HttpMethod post = HttpMethod.valueOf("POST");

        assertThat(post).isNotNull();
        assertThat(post.name()).isEqualTo("POST");
    }
}