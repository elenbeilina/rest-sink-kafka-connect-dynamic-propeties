package com.aqualen.kafkaconnect.rest.config;

import com.aqualen.kafkaconnect.rest.enums.AuthType;
import com.aqualen.kafkaconnect.rest.service.RequestExecutor;
import com.aqualen.kafkaconnect.rest.service.authorization.AuthorizationService;
import com.aqualen.kafkaconnect.rest.service.identifier.IdentifierRestCallBinder;
import com.aqualen.kafkaconnect.rest.util.EnumValidator;
import com.aqualen.kafkaconnect.rest.RestSinkConnector;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.Map;

import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;
import static org.apache.kafka.common.config.ConfigDef.Importance.LOW;

@EqualsAndHashCode(callSuper = true)
public class RestSinkConfig extends AbstractConfig {

    private static final String TOPIC_CONFIG = "topics";
    private static final String TOPIC_DOC = "Topic from which events will be pulled.";

    private static final String MAX_RETRIES = "max.retries";
    private static final String MAX_RETRIES_DOC =
            "Number of times to retry request in case of failure.";
    private static final int MAX_RETRIES_DEFAULT = 5;

    private static final String INTERVAL = "interval";
    private static final String INTERVAL_DOC = "Interval in milliseconds.";
    private static final long INTERVAL_DEFAULT = 2000L;

    private static final String REST_CALL_BINDER = "rest.call.binder";
    private static final String REST_CALL_BINDER_DOC =
            "Class for binding identifier with api request.";
    private static final Class<IdentifierRestCallBinder> REST_CALL_BINDER_DEFAULT =
            IdentifierRestCallBinder.class;

    private static final String AUTH_TYPE = "auth.type";
    private static final String AUTH_TYPE_DOC =
            "Auth type of API, valid values are: NONE, BEARER_TOKEN";
    private static final String AUTH_TYPE_DEFAULT = AuthType.NONE.name();

    public static final String EMPTY = "";

    private final RequestExecutor requestExecutor;
    private final IdentifierRestCallBinder restCallBinder;

    public RestSinkConfig(final Map<?, ?> parsedConfig) {
        super(conf(), parsedConfig);

        OkHttpClient client = new OkHttpClient();

        AuthorizationService authService = getConfiguredAuthService();
        authService.setHttpClient(client);

        requestExecutor = new RequestExecutor(client, authService);
        restCallBinder = getConfiguredRestCallBinder();
    }

    /**
     * Method for generating configuration that is required for sink connector.
     *
     * @return - configuration that manipulates RestSinkConnector.
     * @see RestSinkConnector
     */
    public static ConfigDef conf() {
        return new ConfigDef()
                .define(TOPIC_CONFIG,
                        ConfigDef.Type.STRING,
                        ConfigDef.NO_DEFAULT_VALUE,
                        HIGH,
                        TOPIC_DOC)
                .define(MAX_RETRIES,
                        ConfigDef.Type.INT,
                        MAX_RETRIES_DEFAULT,
                        ConfigDef.Importance.LOW,
                        MAX_RETRIES_DOC)
                .define(INTERVAL,
                        ConfigDef.Type.LONG,
                        INTERVAL_DEFAULT,
                        LOW,
                        INTERVAL_DOC)

                .define(REST_CALL_BINDER,
                        ConfigDef.Type.CLASS,
                        REST_CALL_BINDER_DEFAULT,
                        ConfigDef.Importance.LOW,
                        REST_CALL_BINDER_DOC)

                .define(AUTH_TYPE,
                        ConfigDef.Type.STRING,
                        AUTH_TYPE_DEFAULT,
                        EnumValidator.in(AuthType.values()),
                        ConfigDef.Importance.LOW,
                        AUTH_TYPE_DOC)
                ;
    }

    public int getMaxRetries() {
        return this.getInt(MAX_RETRIES);
    }

    public long getInterval() {
        return this.getLong(INTERVAL);
    }

    public RequestExecutor getRequestExecutor() {
        return requestExecutor;
    }

    public IdentifierRestCallBinder getRestCallBinder() {
        return restCallBinder;
    }

    public final AuthType getAuthType() {
        String authType = this.getString(AUTH_TYPE);

        return AuthType.valueOf(authType);
    }

    @SneakyThrows
    public final AuthorizationService getConfiguredAuthService(){
        AuthorizationService authService = getAuthType().getValue().newInstance();

        authService.configure(this.originals());

        return authService;
    }

    public final IdentifierRestCallBinder getConfiguredRestCallBinder(){
        return this.getConfiguredInstance(REST_CALL_BINDER, REST_CALL_BINDER_DEFAULT);
    }
}
