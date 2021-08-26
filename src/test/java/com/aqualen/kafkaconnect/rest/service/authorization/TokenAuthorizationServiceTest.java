package com.aqualen.kafkaconnect.rest.service.authorization;

import com.aqualen.kafkaconnect.rest.config.AuthConfig;
import com.aqualen.kafkaconnect.rest.util.ConfigurationUtil;
import lombok.SneakyThrows;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenAuthorizationServiceTest {

    private static final Map<String, String> AUTH_PROPS = new HashMap<String, String>() {{
        put("auth.url", ConfigurationUtil.URI);
        put("auth.headers", "1:test,2:test2");
        put("auth.query.params", "1:query,2:query2");
    }};

    @Mock
    private OkHttpClient client;

    @Spy
    private final AuthConfig config =
            new AuthConfig(AUTH_PROPS);

    @InjectMocks
    private TokenAuthorizationService authorizationService;

    @Captor
    private ArgumentCaptor<Request> request;

    private Request.Builder builder;

    @BeforeEach
    void setUp() {
        builder = new Request.Builder().url(ConfigurationUtil.URI);
    }

    @Test
    void setAuthorizationHeaderWithNullToken() {
        authorizationService.setAuthorizationHeader(builder);

        assertThat(builder.build().header("Authorization")).isNull();
    }

    @Test
    void setAuthorizationHeader() {
        authorizationService.setAuthorizationHeader(builder);

        assertThat(builder.build().header("Authorization")).isNull();

        ResponseBody body = ResponseBody.create(MediaType.get(javax.ws.rs.core.MediaType.APPLICATION_JSON), "{\"access_token\":\"test\"}");
        authorizationService.setToken(body);
        authorizationService.setAuthorizationHeader(builder);

        assertThat(builder.build().header("Authorization"))
                .isNotEmpty()
                .isEqualTo("Bearer test");

    }

    @Test
    @SneakyThrows
    void authenticate() {
        Call remoteCall = mock(Call.class);

        when(remoteCall.execute()).thenReturn(new Response.Builder()
                .request(builder.build())
                .protocol(Protocol.HTTP_2)
                .code(200).message("")
                .body(null)
                .build());
        when(client.newCall(any())).thenReturn(remoteCall);

        authorizationService.authenticate();

        verify(client).newCall(request.capture());
        Request result = request.getValue();

        assertThat(result.headers().size()).isEqualTo(2);
        assertThat(result.headers().get("1")).isEqualTo("test");

        assertThat(result.url().querySize()).isEqualTo(2);
        assertThat(result.url().queryParameterValue(0)).isEqualTo("query");
    }
}