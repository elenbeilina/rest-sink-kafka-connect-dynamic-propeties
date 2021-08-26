package com.aqualen.kafkaconnect.rest.config;

import com.aqualen.kafkaconnect.rest.service.authorization.AuthorizationService;
import okhttp3.Headers;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;

public class AuthConfig extends AbstractConfig {
    private static final String AUTH_URL = "auth.url";
    private static final String AUTH_URL_DOC = "Authentication uri for accessing all api methods.";

    public static final String AUTH_HEADERS = "auth.headers";
    public static final String AUTH_HEADERS_DOC = "Headers that are needed for authentication";

    public static final String AUTH_QUERY_PARAMS = "auth.query.params";

    public static final String AUTH_QUERY_PARAMS_DOC =
            "Query params that are needed for authentication";

    public AuthConfig(final Map<?, ?> parsedConfig) {
        super(conf(), parsedConfig);
    }

    /**
     * Method for generating configuration that is required for authentication
     * of the connector.
     *
     * @return - configuration that manipulates AuthorizationService.
     * @see AuthorizationService
     */
    public static ConfigDef conf() {
        return new ConfigDef()
                .define(AUTH_URL,
                        ConfigDef.Type.STRING,
                        RestSinkConfig.EMPTY,
                        LOW,
                        AUTH_URL_DOC)
                .define(AUTH_HEADERS,
                        ConfigDef.Type.LIST,
                        RestSinkConfig.EMPTY,
                        ConfigDef.Importance.LOW,
                        AUTH_HEADERS_DOC)
                .define(AUTH_QUERY_PARAMS,
                        ConfigDef.Type.LIST,
                        RestSinkConfig.EMPTY,
                        ConfigDef.Importance.LOW,
                        AUTH_QUERY_PARAMS_DOC)
                ;
    }

    public String getAuthUrl() {
        return this.getString(AUTH_URL);
    }

    public Headers getAuthHeaders() {
        List<String> list = this.getList(AUTH_HEADERS);

        return Headers.of(getMapOfProperties(list));
    }

    public Map<String, String> getAuthQueryParams() {
        List<String> list = this.getList(AUTH_QUERY_PARAMS);

        return getMapOfProperties(list);
    }

    private Map<String, String> getMapOfProperties(List<String> list) {
        return list.stream()
                .map(s -> s.split(":", 2))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));
    }
}
