package com.aqualen.kafkaconnect.rest.service.identifier.location;

import com.aqualen.kafkaconnect.rest.config.IdentifierRestCallBinderConfig;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor
@AllArgsConstructor
public class IdentifierBodyFinder implements IdentifierFinder {

    private IdentifierRestCallBinderConfig binderConfig;

    /**
     * Method that finds identifier in body.
     * @param object - json object
     * @return - identifier
     */
    public String findIdentifier(Object object){
        JSONObject jsonObject = (JSONObject) object;

        return jsonObject.getString(binderConfig.getIdentificationKey());
    }
}
