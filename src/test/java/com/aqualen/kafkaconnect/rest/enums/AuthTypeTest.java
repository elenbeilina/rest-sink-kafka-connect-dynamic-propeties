package com.aqualen.kafkaconnect.rest.enums;

import com.aqualen.kafkaconnect.rest.service.authorization.AuthorizationService;
import com.aqualen.kafkaconnect.rest.service.authorization.WithoutAuthorizationService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AuthTypeTest {

    @Test
    void getValue() {
        Class<? extends AuthorizationService> value = AuthType.NONE.getValue();

        assertThat(value.getName()).isEqualTo(WithoutAuthorizationService.class.getName());
    }

    @Test
    void values() {
        AuthType[] values = AuthType.values();

        Assertions.assertThat(values)
                .isNotNull()
                .hasSizeGreaterThanOrEqualTo(1)
                .contains(AuthType.NONE);

    }

    @Test
    void valueOf() {
        AuthType none = AuthType.valueOf("NONE");

        assertThat(none).isNotNull();
        assertThat(none.name()).isEqualTo("NONE");
    }
}