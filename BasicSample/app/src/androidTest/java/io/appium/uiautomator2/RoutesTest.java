package io.appium.uiautomator2;

import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static io.appium.uiautomator2.TestHelper.get;
import static io.appium.uiautomator2.TestHelper.post;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/*
todo: Use appium java client to test the endpoints (once the server supports more commands)

adb logcat -c; adb logcat -s "appium"
*/
@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class RoutesTest {
    // todo: see if run listener is compatible with android studio
    // https://github.com/junit-team/junit/blob/master/src/main/java/org/junit/runner/notification/RunListener.java

    // Launch main activity exactly once before all tests.
    // Don't use ActivityTestRule because that'll launch once per test.
    static {
        TestHelper.launchActivity(MainActivity.class);
    }

    @Test
    public void statusTest() { // get("/wd/hub/status", new Status());
        assertThat(get("/wd/hub/status"), equalTo("status command!"));
    }

    @Test
    public void findElementTest() throws IOException { // post("/wd/hub/session/:sessionId/element", new FindElement());
        String expected = "find element!";
        assertThat(post("/wd/hub/session/:sessionId/element"), equalTo(expected));
        assertThat(post("/wd/hub/session/12345/element"), equalTo(expected));
    }

    @Test
    public void getSourceTest() throws IOException { // get("/wd/hub/session/:sessionId/source", new GetSource());
        String xmlHeader = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?>";
        assertThat(get("/wd/hub/session/:sessionId/source"), containsString(xmlHeader));
        assertThat(get("/wd/hub/session/12345/source"), containsString(xmlHeader));
    }
}
