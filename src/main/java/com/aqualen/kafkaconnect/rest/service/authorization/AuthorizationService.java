package com.aqualen.kafkaconnect.rest.service.authorization;

import com.aqualen.kafkaconnect.rest.config.AuthConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.kafka.common.Configurable;

import java.util.Map;

public abstract class AuthorizationService implements Configurable {
    protected OkHttpClient httpClient;
    protected AuthConfig config;

    @Override
    public void configure(Map<String, ?> props) {
        config = new AuthConfig(props);
    }

    public void setHttpClient(OkHttpClient httpClient){
        this.httpClient = httpClient;
    }

    public abstract void setAuthorizationHeader(Request.Builder builder);
    public abstract void authenticate();
}
