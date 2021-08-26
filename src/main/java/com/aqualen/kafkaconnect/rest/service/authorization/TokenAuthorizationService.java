package com.aqualen.kafkaconnect.rest.service.authorization;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import org.json.JSONObject;
import okhttp3.Request;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okhttp3.RequestBody;

import java.util.Objects;

@NoArgsConstructor
public class TokenAuthorizationService extends AuthorizationService {

    private String token;

    /**
     * Method for adding authentication header to request
     *
     * @param builder - request builder
     */
    @Override
    public void setAuthorizationHeader(Request.Builder builder) {
        if (Objects.nonNull(token)) {
            builder.header("Authorization", "Bearer " + token);
        }
    }

    /**
     * Method for authenticating into HighRadius
     * After successful completion - sets valid access token
     */
    @SneakyThrows
    @Override
    public void authenticate() {
        HttpUrl.Builder builder = Objects.requireNonNull(
                HttpUrl.parse(config.getAuthUrl())
        )
                .newBuilder();

        config.getAuthQueryParams().forEach(builder::addQueryParameter);

        Request request = new Request.Builder()
                .url(builder.build())
                .headers(config.getAuthHeaders())
                .post(RequestBody.create(
                        MediaType.get(javax.ws.rs.core.MediaType.APPLICATION_JSON), ""))
                .build();

        ResponseBody body = httpClient.newCall(request).execute().body();
        setToken(body);
    }

    /**
     * Method for setting token:
     * <p>- converts response body to json</p>
     * <p>- gets value of access_token</p>
     * <p>- sets it to token field</p>
     * <p>- closes client response</p>
     *
     * @param body - body that came from auth request
     */
    @SneakyThrows
    protected void setToken(ResponseBody body) {
        if (Objects.nonNull(body)) {
            JSONObject object = new JSONObject(body.string());

            this.token = object.getString("access_token");

            body.close();
        }
    }
}
