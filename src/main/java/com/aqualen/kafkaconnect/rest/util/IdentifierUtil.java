package com.aqualen.kafkaconnect.rest.util;

import lombok.experimental.UtilityClass;
import org.apache.kafka.common.config.ConfigException;

import java.util.Set;

@UtilityClass
public class IdentifierUtil {

    public static final String DELIMITER = ".";
    private static final String IDENTIFIER = "identifier";

    private static final String NO_IDENTIFIER_EXCEPTION = "There is no such identifier in the configuration!";

    public static String getBaseIdentifierProperty(String identifier) {
        return IDENTIFIER + DELIMITER + processByConvention(identifier) + DELIMITER;
    }

    private String processByConvention(String identifier) {
        return identifier.replaceAll("\\s+", "-").toLowerCase();
    }

    public static void identifierCheck(String identifier, Set<String> identifiers) {
        if (!identifiers.contains(identifier)) {
            throw new ConfigException(NO_IDENTIFIER_EXCEPTION);
        }
    }
}
