package io.appium.uiautomator2.commands;

import io.appium.uiautomator2.server.IAppiumRequest;

public abstract class AbstractCommand {
    public abstract String execute(final IAppiumRequest request);
}
