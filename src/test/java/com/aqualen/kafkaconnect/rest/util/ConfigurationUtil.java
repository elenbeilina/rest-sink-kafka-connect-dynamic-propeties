package com.aqualen.kafkaconnect.rest.util;

import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ConfigurationUtil {
    public static Map<String, String> REQUIRED_PROPS = new HashMap<String, String>() {{
        put("topics", "test");
        put("identification.key", "test");
        put("identifier.values", "Test Values");
        put("api.base.uri", URI);
        put("identifier.test-values.uri", "test");
        put("identifier.test-values.http.method", "POST");
        put("interval", "1");
    }};

    public static final String URI = "https://test/";
}
