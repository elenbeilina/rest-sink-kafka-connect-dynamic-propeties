package com.aqualen.kafkaconnect.rest.config;

import com.aqualen.kafkaconnect.rest.enums.HttpMethod;
import com.aqualen.kafkaconnect.rest.enums.IdentifierLocation;
import com.aqualen.kafkaconnect.rest.service.identifier.IdentifierRestCallBinder;
import com.aqualen.kafkaconnect.rest.util.EnumValidator;
import lombok.EqualsAndHashCode;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;

import static com.aqualen.kafkaconnect.rest.enums.IdentifierLocation.BODY;
import static com.aqualen.kafkaconnect.rest.util.IdentifierUtil.getBaseIdentifierProperty;
import static org.apache.kafka.common.config.ConfigDef.Importance.HIGH;

@EqualsAndHashCode(callSuper = true)
public class IdentifierRestCallBinderConfig extends AbstractConfig {

    private static final String API_BASE_URI = "api.base.uri";
    private static final String API_BASE_URI_DOC =
            "Base uri of api that will be used to push data to.";

    private static final String IDENTIFIER_LOCATION = "identifier.location";
    private static final String IDENTIFIER_LOCATION_DOC = "Location of identifier." +
            " See all possible locations in IdentifierLocation.class.";
    private static final String IDENTIFIER_LOCATION_DEFAULT = BODY.name();

    private static final String IDENTIFICATION_KEY = "identification.key";
    private static final String IDENTIFICATION_KEY_DOC =
            "Name of the key field for identification.";

    private static final String IDENTIFIER_VALUES = "identifier.values";
    private static final String IDENTIFIER_VALUES_DOC = "Identification values," +
            "based on them we decide which api to call and the process of calling.";

    private static final String URI = "uri";
    private static final String HTTP_METHOD = "http.method";
    protected static final List<String> DYNAMIC_PROPS_FEATURES = Arrays.asList(URI, HTTP_METHOD);

    public IdentifierRestCallBinderConfig(final Map<?, ?> parsedConfig) {
        super(conf(parsedConfig), parsedConfig);
    }

    /**
     * Method for generating configuration that is required for identifier
     * with rest calls binder service.
     *
     * @return - configuration that manipulates IdentifierRestCallsBinder.
     * @see IdentifierRestCallBinder
     */
    public static ConfigDef conf(Map<?, ?> configProps) {
        return new DynamicConfigDef(IDENTIFIER_VALUES, DYNAMIC_PROPS_FEATURES)
                .defineDynamicProps(configProps)
                .define(API_BASE_URI,
                        ConfigDef.Type.STRING,
                        ConfigDef.NO_DEFAULT_VALUE,
                        HIGH,
                        API_BASE_URI_DOC)
                .define(IDENTIFIER_LOCATION,
                        ConfigDef.Type.STRING,
                        IDENTIFIER_LOCATION_DEFAULT,
                        EnumValidator.in(IdentifierLocation.values()),
                        HIGH,
                        IDENTIFIER_LOCATION_DOC)
                .define(IDENTIFICATION_KEY,
                        ConfigDef.Type.STRING,
                        ConfigDef.NO_DEFAULT_VALUE,
                        HIGH,
                        IDENTIFICATION_KEY_DOC)
                .define(IDENTIFIER_VALUES,
                        ConfigDef.Type.LIST,
                        ConfigDef.NO_DEFAULT_VALUE,
                        HIGH,
                        IDENTIFIER_VALUES_DOC)
                ;
    }

    public IdentifierLocation getIdentifierLocation() {
        return IdentifierLocation.valueOf(this.getString(IDENTIFIER_LOCATION));
    }

    public String getIdentificationKey() {
        return this.getString(IDENTIFICATION_KEY);
    }

    public String getUriForIdentifier(String identifier) {
        String baseIdentifierProperty = getBaseIdentifierProperty(identifier);

        String identifierUri = this.getString(baseIdentifierProperty + URI);

        return getApiBaseUri().concat(identifierUri);
    }

    private String getApiBaseUri() {
        return this.getString(API_BASE_URI);
    }

    public HttpMethod getHttpMethodForIdentifier(String identifier) {
        String baseIdentifierProperty = getBaseIdentifierProperty(identifier);
        String httpMethod = this.getString(baseIdentifierProperty + HTTP_METHOD);

        return HttpMethod.valueOf(httpMethod);
    }

    public Set<String> getIdentifiers() {
        List<String> list = this.getList(IDENTIFIER_VALUES);

        return new HashSet<>(list);
    }
}
