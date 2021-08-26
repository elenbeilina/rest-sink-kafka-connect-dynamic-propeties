package com.aqualen.kafkaconnect.rest.config;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.aqualen.kafkaconnect.rest.util.IdentifierUtil.getBaseIdentifierProperty;

public class DynamicConfigDef extends ConfigDef {

    private final List<String> dynamicPropsFeatures;
    private final String dynamicPropsName;

    private static final String MANDATORY_PROPERTY_ERROR = "Mandatory property: %s is not defined!";

    public DynamicConfigDef(String dynamicPropsName, List<String> dynamicPropsFeatures) {
        super();
        this.dynamicPropsFeatures = new ArrayList<>(dynamicPropsFeatures);
        this.dynamicPropsName = dynamicPropsName;
    }

    public ConfigDef defineDynamicProps(Map<?, ?> props) {
        String values = getProcessedProps(props).get(dynamicPropsName);
        String[] valuesArray = StringUtils.splitPreserveAllTokens(values, ",");
        dynamicPropsCheck(valuesArray);

        for (String value : valuesArray) {
            for (String prop : dynamicPropsFeatures) {
                String dynamicProperty = getBaseIdentifierProperty(value).concat(prop);
                this.define(
                        dynamicProperty,
                        Type.STRING,
                        NO_DEFAULT_VALUE,
                        null,
                        Importance.HIGH,
                        dynamicProperty
                );
            }
        }

        return this;
    }

    private Map<String, String> getProcessedProps(Map<?, ?> props) {
        return props.entrySet().stream().collect(Collectors.toMap(
                e -> (String) e.getKey(),
                e -> (String) e.getValue()));
    }

    private void dynamicPropsCheck(String[] valuesArray){
        if(Objects.isNull(valuesArray)){
            throw new ConfigException(String.format(MANDATORY_PROPERTY_ERROR,dynamicPropsName));
        }
    }
}
