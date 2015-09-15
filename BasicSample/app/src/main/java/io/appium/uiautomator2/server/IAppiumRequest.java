package io.appium.uiautomator2.server;

import com.google.common.base.Optional;

public interface IAppiumRequest {
    // Generic netty request methods

    /**
     * Returns the request method (GET, POST, DELETE, PUT)
     */
    Optional<String> method();

    /**
     * Returns the request uri
     */
    Optional<String> uri();

    /**
     * Returns the value of the target header
     */
    Optional<String> header(String headerName);

    /**
     * Returns the request body
     */
    Optional<String> body();

    // Appium specific.

    /**
     * Returns the route the uri is mapped to
     */
    Optional<String> route();

    /**
     * Returns the sessionId attribute
     */
    Optional<String> sessionId();

    /**
     * Returns the name attribute
     */
    Optional<String> name();

    /**
     * Returns the id attribute
     */
    Optional<String> id();

    /**
     * Returns the command attribute
     */
    Optional<String> command();
}
