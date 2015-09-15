package io.appium.uiautomator2.server;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

public class AppiumRequest implements IAppiumRequest {

    // Nano session attributes
    private FullHttpRequest request;
    private Optional<String> method = Optional.absent();
    private Optional<String> uri = Optional.absent();
    private Optional<String[]> uriSplit = Optional.absent(); // internal use only
    private Optional<String> body = Optional.absent();
    private Optional<String> headers = Optional.absent();

    // Appium specific
    private Optional<String> route = Optional.absent();
    private Optional<String[]> routeSplit = Optional.absent(); // internal use only
    private Optional<String> sessionId = Optional.absent(); // :sessionId
    private Optional<String> command = Optional.absent(); // :command
    private Optional<String> id = Optional.absent(); // :id
    private Optional<String> name = Optional.absent(); // :name

    public AppiumRequest(FullHttpRequest request) {
        this.request = request;
    }

    @Override
    public Optional<String> method() {
        if (!method.isPresent()) method = Optional.fromNullable(request.getMethod().name());
        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> uri() {
        if (!uri.isPresent()) uri = Optional.fromNullable(request.getUri());
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> body() {
        if (!body.isPresent())
            body = Optional.fromNullable(request.content().toString(CharsetUtil.UTF_8));
        return body;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> header(String headerName) {
        if (!headers.isPresent())
            headers = Optional.fromNullable(request.headers().get(headerName));
        return headers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> route() {
        if (!route.isPresent()) route = setRouteFromUri(uri().get());
        return route;
    }

    private Optional<String> setRouteFromUri(String uri) {
        ImmutableMap<String, String[]> routes;

        if (!method.isPresent()) throw new IllegalStateException("method is null");
        String methodString = method.get();

        if (methodString.contentEquals("GET")) {
            routes = Routes.getRoutes().get();
        } else if (methodString.contentEquals("POST")) {
            routes = Routes.postRoutes().get();
        } else {
            throw new RuntimeException("Unknown method: " + methodString);
        }

        Optional<String> result = Optional.absent();

        // given all routes for method, determine the route that matches uri.
        // actual: /wd/hub/session/12345/element
        // internal: /wd/hub/session/:sessionId/element
        String[] actualUri = uri.split("/");
        uriSplit = Optional.fromNullable(actualUri);

        for (Map.Entry entry : routes.entrySet()) {
            String[] internalRoute = (String[]) entry.getValue();
            if (uriEquals(actualUri, internalRoute)) {
                routeSplit = Optional.fromNullable(internalRoute);
                result = Optional.fromNullable((String) entry.getKey());
                break;
            }
        }

        return result;
    }

    private boolean uriEquals(String[] actualUri, String[] internalUri) {
        if (actualUri == null || internalUri == null) return false;
        int actualLength = actualUri.length;
        int internalLength = internalUri.length;
        if (actualLength != internalLength) return false;
        if (actualLength == 0 || internalLength == 0) return false;

        for (int i = 0; i < actualLength; i++) {
            String actual = actualUri[i];
            String internal = internalUri[i];

            if (!(internal.startsWith(":") || actual.equals(internal))) {
                return false;
            }
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> sessionId() {
        if (!sessionId.isPresent()) sessionId = attributeFromRouteAndUri(":sessionId");
        return sessionId;
    }

    private Optional<String> attributeFromRouteAndUri(String attribute) {
        String[] route = routeSplit.get();
        String[] uri = uriSplit.get();
        for (int i = 0; i < route.length; i++) {
            if (route[i].startsWith(":")) return Optional.fromNullable(uri[i]);
        }
        return Optional.absent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> command() {
        if (!command.isPresent()) command = attributeFromRouteAndUri(":command");
        return command;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> id() {
        if (!id.isPresent()) id = attributeFromRouteAndUri(":id");
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> name() {
        if (!name.isPresent()) name = attributeFromRouteAndUri(":name");
        return name;
    }
}

