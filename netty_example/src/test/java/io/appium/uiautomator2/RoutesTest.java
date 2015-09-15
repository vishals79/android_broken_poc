package io.appium.uiautomator2;

import org.junit.Test;

import java.io.IOException;

import static io.appium.uiautomator2.TestHelper.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

// able to re-use selendroid-server-common as is.
// advantage to testing it on the desktop. no android dependencies.
// share that code with selendroid (?)
public class RoutesTest {

    @Test
    public void helloWorldTest() throws IOException {
        String expectedString = "Hi there from selendroid";
        assertThat(get("/"), equalTo(expectedString));
    }
}
