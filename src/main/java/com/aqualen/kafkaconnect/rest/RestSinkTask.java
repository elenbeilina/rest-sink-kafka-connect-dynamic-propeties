package com.aqualen.kafkaconnect.rest;

import com.aqualen.kafkaconnect.rest.config.RestSinkConfig;
import com.aqualen.kafkaconnect.rest.service.RequestExecutor;
import com.aqualen.kafkaconnect.rest.service.identifier.IdentifierRestCallBinder;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.apache.kafka.connect.errors.RetriableException;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;

import java.util.Collection;
import java.util.Map;

import static com.aqualen.kafkaconnect.rest.util.PropertiesUtil.getConnectorVersion;
import static java.lang.System.currentTimeMillis;

@Slf4j
@NoArgsConstructor
public class RestSinkTask extends SinkTask {

    private RestSinkConfig config;
    private IdentifierRestCallBinder binder;
    private RequestExecutor executor;

    private static final String NOT_SUCCESSFUL_RESPONSE = "Response was not successful!";

    private Long lastPollTime = 0L;

    @Override
    public String version() {
        return getConnectorVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        config = new RestSinkConfig(props);
        binder = config.getRestCallBinder();
        executor = config.getRequestExecutor();
    }

    /**
     * Method that executes post request to external api
     *
     * @param collection - batch of messages from topic
     */
    @Override
    @SneakyThrows
    public void put(Collection<SinkRecord> collection) {
        if (collection.isEmpty()) {
            log.info("Empty record collection to process");
            return;
        }

        for (SinkRecord sinkRecord : collection) {
            processRecord(sinkRecord);
        }
    }

    /**
     * Method for processing records with interval.
     * If exception was occurred - retries execution.
     * @param sinkRecord from topic.
     */
    private void processRecord(SinkRecord sinkRecord) {
        int retries = 0;

        while (retries < config.getMaxRetries()) {
            try {
                sleepIfNeeded();

                if (execute(sinkRecord)) {
                    break;
                }
                throw new RetriableException(NOT_SUCCESSFUL_RESPONSE);
            } catch (Exception e) {
                log.error("Exception occurred: {0}, retrying...", e);
                retries++;
            } finally {
                lastPollTime = currentTimeMillis();
            }
        }
    }

    @SneakyThrows
    private void sleepIfNeeded() {
        long millis = config.getInterval() - (currentTimeMillis() - lastPollTime);

        if (millis > 0) {
            Thread.sleep(millis);
        }
    }

    private boolean execute(SinkRecord sinkRecord) {
        Request.Builder request = binder.constructRequestByIdentifier(sinkRecord);

        return executor.sendRequest(request).isSuccessful();
    }

    @Override
    public void stop() {
        log.info("{} Stopping RestSinkTask.", this);
    }
}
