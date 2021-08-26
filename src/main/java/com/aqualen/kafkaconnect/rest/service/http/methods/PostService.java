package com.aqualen.kafkaconnect.rest.service.http.methods;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@NoArgsConstructor
public class PostService implements RestService {

    /**
     * Method for completing post request.
     *
     * @return completed Request
     * @see Request
     */
    @SneakyThrows
    public Request.Builder constructRequest(Request.Builder request, JSONObject object) {
        addBodyToRequest(request, object);

        return request;
    }

    private void addBodyToRequest(Request.Builder request, JSONObject object) {
        request.post(RequestBody.create(
                MediaType.get(APPLICATION_JSON),
                object.toString()
        ));
    }
}
