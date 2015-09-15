package io.appium.uiautomator2;

import android.os.Bundle;
import android.support.test.runner.AndroidJUnitRunner;

import java.lang.reflect.Method;

// Code based on https://code.google.com/p/android/issues/detail?id=170607
// Work around 0 byte coverage.ec file.
public class JacocoRunner extends AndroidJUnitRunner {

    static {
        System.setProperty("jacoco-agent.destfile", "/data/data/" + BuildConfig.APPLICATION_ID + "/coverage.ec");
    }

    // Required to avoid no tests found error.
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        try {
            final Class rt = Class.forName("org.jacoco.agent.rt.RT");
            final Method getAgent = rt.getMethod("getAgent");
            final Method dump = getAgent.getReturnType().getMethod("dump", boolean.class);
            final Object agent = getAgent.invoke(null);
            dump.invoke(agent, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.finish(resultCode, results);
    }
}
