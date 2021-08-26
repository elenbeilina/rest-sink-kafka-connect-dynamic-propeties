package com.aqualen.kafkaconnect.rest.service.identifier.location;

import com.aqualen.kafkaconnect.rest.config.IdentifierRestCallBinderConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.kafka.connect.header.Header;
import org.apache.kafka.connect.sink.SinkRecord;

@NoArgsConstructor
@AllArgsConstructor
public class IdentifierHeaderFinder implements IdentifierFinder {

    private IdentifierRestCallBinderConfig binderConfig;

    /**
     * Method that finds identifier in header.
     * @param object - sink record
     * @return - identifier
     */
    @Override
    public String findIdentifier(Object object) {
        SinkRecord record = (SinkRecord) object;

        String key = binderConfig.getIdentificationKey();
        Header header = record.headers().lastWithName(key);

        return header.value().toString();
    }
}
