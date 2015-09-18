/*
 * Copyright 2012-2014 eBay Software Foundation and selendroid committers.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.selendroid.server.common;

import io.appium.uiautomator2.commands.AbstractCommand;
import io.appium.uiautomator2.server.IAppiumRequest;
import io.appium.uiautomator2.server.Routes;
import io.appium.uiautomator2.util.Log;
import io.selendroid.server.common.http.IHttpResponse;
import io.selendroid.server.common.http.IHttpServlet;

public class AppiumServlet implements IHttpServlet {
    @Override
    public void handleHttpRequest(IAppiumRequest httpRequest, IHttpResponse httpResponse) throws Exception {
        Log.i("AppiumServlet - handleHttpRequest");
        String method = httpRequest.method().get();
        String uri = httpRequest.uri().get();

        Log.i("Responding to " + method + " " + uri);
        AbstractCommand command = Routes.getCommand(httpRequest);
        Log.i("Found command: " + command);

        // todo: implement shutdown command to end the session.
        // look into run listener or espresso rule to end session after all tests complete
        // http://stackoverflow.com/a/14773170

        if (command == null) {
            httpResponse.setStatus(404).setContent("Unknown route " + method + " " + uri).end();
            return;
        }

        String content = command.execute(httpRequest);
	httpResponse.setContent(content);
        httpResponse.setStatus(200);
        Log.i("Sending content: " + content);
    }
}
