package com.aqualen.kafkaconnect.rest.service.authorization;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;

@Slf4j
public class WithoutAuthorizationService extends AuthorizationService {

    @Override
    public void setAuthorizationHeader(Request.Builder builder) {
        // No need for authorization header, because auth mode = none
    }

    @Override
    public void authenticate() {
        log.info("No need for authorization, because auth mode = none");
    }
}
