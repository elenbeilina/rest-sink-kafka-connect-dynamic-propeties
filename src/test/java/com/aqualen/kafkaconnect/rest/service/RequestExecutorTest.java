package com.aqualen.kafkaconnect.rest.service;

import com.aqualen.kafkaconnect.rest.service.authorization.AuthorizationService;
import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import lombok.SneakyThrows;
import okhttp3.*;
import org.apache.commons.httpclient.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestExecutorTest {

    @Mock
    private OkHttpClient client;
    @Mock
    private AuthorizationService authorizationService;
    @InjectMocks
    private RequestExecutor executor;

    @Captor
    private ArgumentCaptor<Request> request;

    private Response.Builder response;
    private Request.Builder requestBuilder;

    @BeforeEach
    void setUp() {
        requestBuilder = new Request.Builder().url(ConfigurationUtil.URI);
        response = new Response.Builder()
                .request(requestBuilder.build())
                .protocol(Protocol.HTTP_2)
                .message("")
                .body(null);
    }

    @Test
    @SneakyThrows
    void sendRequest() {
        Call remoteCall = mock(Call.class);

        when(remoteCall.execute()).thenReturn(response.code(200).build());
        when(client.newCall(any())).thenReturn(remoteCall);

        Response result = executor.sendRequest(requestBuilder);

        assertThat(result).isNotNull();
        assertThat(result.code()).isEqualTo(HttpStatus.SC_OK);

        verify(client).newCall(request.capture());
        assertThat(request.getValue().url().url()).hasToString(ConfigurationUtil.URI);

        verify(authorizationService, times(0)).authenticate();
    }

    @Test
    @SneakyThrows
    void sendRequestWithError() {
        Call remoteCall = mock(Call.class);
        Request.Builder requestBuilder = new Request.Builder().url(ConfigurationUtil.URI);

        when(remoteCall.execute())
                .thenReturn(response.code(401).build());
        when(client.newCall(any())).thenReturn(remoteCall);

        executor.sendRequest(requestBuilder);

        verify(authorizationService, times(1)).authenticate();
    }
}