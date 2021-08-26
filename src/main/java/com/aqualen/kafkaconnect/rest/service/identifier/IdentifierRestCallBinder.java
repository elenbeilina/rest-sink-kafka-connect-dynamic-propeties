package com.aqualen.kafkaconnect.rest.service.identifier;

import com.aqualen.kafkaconnect.rest.config.IdentifierRestCallBinderConfig;
import com.aqualen.kafkaconnect.rest.enums.HttpMethod;
import com.aqualen.kafkaconnect.rest.enums.IdentifierLocation;
import com.aqualen.kafkaconnect.rest.service.http.methods.RestService;
import com.aqualen.kafkaconnect.rest.service.identifier.location.IdentifierFinder;
import lombok.NoArgsConstructor;
import okhttp3.Request;
import org.apache.kafka.common.Configurable;
import org.apache.kafka.connect.header.Header;
import org.apache.kafka.connect.header.Headers;
import org.apache.kafka.connect.sink.SinkRecord;
import org.json.JSONObject;

import java.util.Map;

import static com.aqualen.kafkaconnect.rest.enums.HttpMethod.resolveHttpService;
import static com.aqualen.kafkaconnect.rest.enums.IdentifierLocation.BODY;
import static com.aqualen.kafkaconnect.rest.enums.IdentifierLocation.resolveIdentifierFinder;
import static com.aqualen.kafkaconnect.rest.util.IdentifierUtil.identifierCheck;

@NoArgsConstructor
public class IdentifierRestCallBinder implements Configurable {

    private IdentifierRestCallBinderConfig binderConfig;

    /**
     * Method for configuring IdentifierRestCallBinder by IdentifierRestCallBinderConfig.
     *
     * @param props - props that are coming from connector configuration file
     * @see IdentifierRestCallBinderConfig
     */
    @Override
    public void configure(Map<String, ?> props) {
        binderConfig = new IdentifierRestCallBinderConfig(props);
    }

    /**
     * Method for constructing request by identifier that is found by
     * IdentifierFinder.
     *
     * @param sinkRecord - sinkRecord that was pulled from kafka broker
     * @return - constructed request
     * @see IdentifierFinder
     */
    public Request.Builder constructRequestByIdentifier(SinkRecord sinkRecord) {
        String value = (String) sinkRecord.value();
        JSONObject jsonObject = new JSONObject(value);

        String identifier = getIdentifier(jsonObject, sinkRecord);
        identifierCheck(identifier, binderConfig.getIdentifiers());

        Request.Builder builder = getBasicRequestBuilder(identifier, sinkRecord);

        RestService restService = getRestService(identifier);
        return restService.constructRequest(builder, jsonObject);
    }

    /**
     * Method for constructing basic Request Builder:
     * - uri
     * - headers
     *
     * @param identifier - identifier value
     * @param sinkRecord     - sinkRecord that was pulled from kafka
     * @return - basic builder
     */
    private Request.Builder getBasicRequestBuilder(String identifier, SinkRecord sinkRecord) {
        Request.Builder builder = new Request.Builder();

        addUrl(builder, identifier);
        addHeaders(builder, sinkRecord.headers());

        return builder;
    }

    private void addUrl(Request.Builder request, String identifier) {
        String uri = binderConfig.getUriForIdentifier(identifier);

        request.url(uri);
    }

    private void addHeaders(Request.Builder request, Headers headers) {
        for (Header header : headers) {
            request.addHeader(header.key(), String.valueOf(header.value()));
        }
    }

    /**
     * Method for getting identifier from location that was configured in binder config.
     *
     * @param object - sinkRecord value that was configured to JSONObject
     * @param sinkRecord - sinkRecord that was pulled from kafka
     * @return - identifier
     * @see IdentifierRestCallBinderConfig
     */
    private String getIdentifier(JSONObject object, SinkRecord sinkRecord) {
        IdentifierLocation location = binderConfig.getIdentifierLocation();

        IdentifierFinder finder = resolveIdentifierFinder(location, binderConfig);

        if (location == BODY) {
            return finder.findIdentifier(object);
        }

        return finder.findIdentifier(sinkRecord);
    }

    private RestService getRestService(String identifier) {
        HttpMethod httpMethod = binderConfig.getHttpMethodForIdentifier(identifier);

        return resolveHttpService(httpMethod);
    }
}
