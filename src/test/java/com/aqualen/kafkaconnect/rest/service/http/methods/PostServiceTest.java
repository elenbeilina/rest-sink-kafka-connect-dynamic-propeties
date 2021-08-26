package com.aqualen.kafkaconnect.rest.service.http.methods;

import com.aqualen.kafkaconnect.rest.enums.HttpMethod;
import okhttp3.Request;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostServiceTest {

    @Test
    void constructRequest() {
        PostService postService = new PostService();
        Request.Builder request = new Request.Builder().url("https://test");
        JSONObject object = new JSONObject();
        object.put("test", 1);

        Request result = postService.constructRequest(request, object).build();

        assertThat(result).isExactlyInstanceOf(Request.class);
        assertThat(result.method()).isEqualTo(HttpMethod.POST.name());
    }
}