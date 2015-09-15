package io.appium.uiautomator2.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import io.appium.uiautomator2.server.IAppiumRequest;

import static io.appium.uiautomator2.util.Device.device;
import static io.appium.uiautomator2.util.Log.e;

public class GetSource extends AbstractCommand {
    @Override
    public String execute(final IAppiumRequest request) {
        ByteArrayOutputStream tmp = new ByteArrayOutputStream();
        try {
            device().dumpWindowHierarchy(tmp);
        } catch (IOException e) {
            e(e.getMessage());
        }

        String content = "";

        try {
            content = tmp.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e(e.getMessage());
        }

        return content;
    }
}
