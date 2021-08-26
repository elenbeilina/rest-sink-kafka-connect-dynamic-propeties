package com.aqualen.kafkaconnect.rest.enums;

import com.aqualen.kafkaconnect.rest.config.IdentifierRestCallBinderConfig;
import com.aqualen.kafkaconnect.rest.service.identifier.location.IdentifierBodyFinder;
import com.aqualen.kafkaconnect.rest.service.identifier.location.IdentifierFinder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.aqualen.kafkaconnect.rest.util.ConfigurationUtil.REQUIRED_PROPS;
import static org.assertj.core.api.Assertions.assertThat;

class IdentifierLocationTest {

    @Test
    void resolveIdentifierFinder() {
        IdentifierFinder finder = IdentifierLocation.resolveIdentifierFinder(IdentifierLocation.BODY,
                new IdentifierRestCallBinderConfig(REQUIRED_PROPS));

        assertThat(finder)
                .isNotNull()
                .hasNoNullFieldsOrProperties()
                .isExactlyInstanceOf(IdentifierBodyFinder.class);
    }

    @Test
    void values() {
        IdentifierLocation[] values = IdentifierLocation.values();

        Assertions.assertThat(values)
                .isNotNull()
                .hasSizeGreaterThanOrEqualTo(1)
                .contains(IdentifierLocation.BODY);

    }

    @Test
    void valueOf() {
        IdentifierLocation location = IdentifierLocation.valueOf("BODY");

        assertThat(location).isNotNull();
        assertThat(location.name()).isEqualTo("BODY");
    }
}