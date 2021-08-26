package com.aqualen.kafkaconnect.rest.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {

    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);

    private static final String CONNECTOR_VERSION = "connector.version";

    private static final String PROPERTIES_FILE = "/rest-sink-connector.properties";
    private static Properties properties;

    static {
        try (InputStream stream = PropertiesUtil.class.getResourceAsStream(PROPERTIES_FILE)) {
            properties = new Properties();
            properties.load(stream);
        } catch (Exception ex) {
            log.warn("Error while loading properties: ", ex);
        }
    }

    /**
     * Method for getting project version which is located in pom.xml
     *
     * @return connector version
     */
    public static String getConnectorVersion() {
        return properties.getProperty(CONNECTOR_VERSION);
    }
}
