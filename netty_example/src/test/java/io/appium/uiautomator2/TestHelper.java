package io.appium.uiautomator2;

import com.squareup.okhttp.*;
import io.netty.example.http.helloworld.HttpHelloWorldServer;
import io.selendroid.server.common.http.TmpSelendroidServer;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.Assert.fail;

public abstract class TestHelper {

    static {
        startServer();
        // JUnit has no easy way to do before/after all tests
        // without explicitly listing every test in a suite.
        // A test listener will not work. see:
        // https://github.com/junit-team/junit/issues/644
    }

    private static final OkHttpClient client = new OkHttpClient();
    private static final String baseUrl = "http://localhost:8080";
    private static Thread serverThread = null;

    static {
        final int timeout = 15 * 1000;
        client.setConnectTimeout(timeout, SECONDS);
        client.setReadTimeout(timeout, SECONDS);
        client.setWriteTimeout(timeout, SECONDS);
    }

    public static String get(final String path) {
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .build();

        return execute(request);
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public static void startServer() {
        if (serverThread != null) return;

        serverThread = new Thread() {
            @Override
            public void run() {
                try {
                   // HttpHelloWorldServer.main(null);
                    TmpSelendroidServer.startServer();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        serverThread.start();
    }

    public static void stopServer() {
        if (serverThread == null) return;
        serverThread.interrupt();
        serverThread = null;
    }

    public static String post(final String path) {
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .post(RequestBody.create(JSON, "{\"test\": true}"))
                .build();


        return execute(request);
    }

    private static String execute(Request request) {
        String result = "";
        try {
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            fail(request.method() + " \"" + request.urlString() + "\" failed. " + e.getMessage());
        }
        return result;
    }
}
