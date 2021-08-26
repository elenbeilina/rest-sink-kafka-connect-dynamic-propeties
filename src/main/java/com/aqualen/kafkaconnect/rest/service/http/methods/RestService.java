package com.aqualen.kafkaconnect.rest.service.http.methods;

import okhttp3.Request;
import org.json.JSONObject;

public interface RestService {
    Request.Builder constructRequest(Request.Builder request, JSONObject object);
}
