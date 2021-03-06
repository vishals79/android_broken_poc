package io.appium.uiautomator2;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static junit.framework.Assert.fail;

public abstract class TestHelper {

    private static final OkHttpClient client = new OkHttpClient();
    private static final String baseUrl = "http://localhost:4456";
    private static final Instrumentation mInstrumentation = InstrumentationRegistry.getInstrumentation();

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
            fail("Post \"" + request.urlString() + "\" failed. " + e.getMessage());
        }
        return result;
    }

    // todo: see createStartActivityIntent from selendroid
    // Ported from android-support-test/rules/src/main/java/android/support/test/rule/ActivityTestRule.java
    public static <T extends Activity> T launchActivity(Class<T> activityClass) {
        final String targetPackage = mInstrumentation.getTargetContext().getPackageName();
        Intent startIntent = new Intent(Intent.ACTION_MAIN);
        startIntent.setClassName(targetPackage, activityClass.getName());
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // The following cast is correct because the activity we're creating is of the same type as
        // the one passed in
        T mActivity = activityClass.cast(mInstrumentation.startActivitySync(startIntent));

        mInstrumentation.waitForIdleSync();

        return mActivity;
    }

}
