package io.appium.uiautomator2.commands;

import io.appium.uiautomator2.server.IAppiumRequest;

public class Status extends AbstractCommand {
    @Override
    public String execute(final IAppiumRequest request) {
        return "status command!";
    }
}
