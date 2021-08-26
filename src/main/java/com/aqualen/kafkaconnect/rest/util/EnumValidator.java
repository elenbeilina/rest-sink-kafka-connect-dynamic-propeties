package com.aqualen.kafkaconnect.rest.util;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import java.util.HashSet;
import java.util.Set;

public final class EnumValidator implements ConfigDef.Validator {
    private final Set<String> validValues;

    private EnumValidator(Set<String> validValues) {
        this.validValues = new HashSet<>(validValues);
    }

    public static <E> EnumValidator in(E[] enumerators) {
        final Set<String> validValues = new HashSet<>(enumerators.length * 2);
        for (E e : enumerators) {
            validValues.add(e.toString());
        }
        return new EnumValidator(validValues);
    }

    @Override
    public void ensureValid(String key, Object value) {
        String enumName = (String) value;
        if (!validValues.contains(enumName)) {
            throw new ConfigException(
                    key, value, "Invalid enumerator, allowed values: " + validValues
            );
        }
    }
}
