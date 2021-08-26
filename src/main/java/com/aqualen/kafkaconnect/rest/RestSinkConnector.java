package com.aqualen.kafkaconnect.rest;

import com.aqualen.kafkaconnect.rest.config.RestSinkConfig;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkConnector;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import static com.aqualen.kafkaconnect.rest.config.RestSinkConfig.conf;
import static com.aqualen.kafkaconnect.rest.util.PropertiesUtil.getConnectorVersion;

@Slf4j
@NoArgsConstructor
public class RestSinkConnector extends SinkConnector {

    private RestSinkConfig config;

    @Override
    public void start(Map<String, String> props) {
        log.info("{} Starting RestSinkConnector", this);
        try {
            config = new RestSinkConfig(props);
        } catch (ConfigException e) {
            throw new ConnectException(
                    "Couldn't start RestSinkConnector due to configuration error", e
            );
        }
    }

    @Override
    public Class<? extends Task> taskClass() {
        return RestSinkTask.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(final int maxTasks) {
        val configs = new ArrayList<Map<String, String>>(maxTasks);
        val taskProps = new HashMap<>(config.originalsStrings());
        for (int i = 0; i < maxTasks; i++) {
            configs.add(taskProps);
        }

        return configs;
    }

    @Override
    public void stop() {
        log.info("{} Stopping RestSinkConnector.", this);
    }

    @Override
    public ConfigDef config() {
        return conf();
    }

    @Override
    public String version() {
        return getConnectorVersion();
    }
}
