package physicks.secondBoard.sandbox;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MockWebServerTest {

    private MockWebServer server;

    @BeforeEach
    public void setup() throws Exception {
        this.server = new MockWebServer();
        this.server.start();
    }

    @AfterEach
    public void cleanup() throws Exception {
        this.server.shutdown();
    }

    @DisplayName("Mock Server URI 로 요청을 보내고 응답을 받는다")
    @Test
    void MockWebServer_getBasicResponse() throws IOException {
        // given
        this.server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("hello, world!"));
        String url = this.server.url("/hello").toString();

        OkHttpClient client = new OkHttpClient();

        // when
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        // then
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body().string()).isEqualTo("hello, world!");
    }

}
