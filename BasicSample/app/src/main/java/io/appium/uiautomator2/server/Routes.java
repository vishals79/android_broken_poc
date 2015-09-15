package io.appium.uiautomator2.server;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import io.appium.uiautomator2.commands.AbstractCommand;
import io.appium.uiautomator2.commands.FindElement;
import io.appium.uiautomator2.commands.GetSource;
import io.appium.uiautomator2.commands.Status;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;

public abstract class Routes {

    private static Optional<ImmutableMap.Builder<String, AbstractCommand>> getBuilder = fromNullable(new ImmutableMap.Builder<String, AbstractCommand>());
    private static Optional<ImmutableMap<String, AbstractCommand>> getMap = Optional.absent();

    private static Optional<ImmutableMap.Builder<String, AbstractCommand>> postBuilder = fromNullable(new ImmutableMap.Builder<String, AbstractCommand>());
    private static Optional<ImmutableMap<String, AbstractCommand>> postMap = Optional.absent();

    private static Optional<ImmutableMap<String, String[]>> getRoutes = Optional.absent();
    private static Optional<ImmutableMap<String, String[]>> postRoutes = Optional.absent();

    // See Selendroid's AndroidServlet.java for supported routes
    static {
        get("/wd/hub/status", new Status());
        post("/wd/hub/session/:sessionId/element", new FindElement());
        get("/wd/hub/session/:sessionId/source", new GetSource());
        Routes.finish();
    }

    public static void get(final String uri, final AbstractCommand command) {
        getBuilder.get().put(uri, command);
    }

    public static void post(final String uri, final AbstractCommand command) {
        postBuilder.get().put(uri, command);
    }

    public static void finish() {
        if (!getBuilder.isPresent())
            throw new IllegalStateException("finish called more than once!");
        if (!postBuilder.isPresent())
            throw new IllegalStateException("finish called more than once!");
        getMap = fromNullable(getBuilder.get().build());
        getBuilder = absent();

        postMap = fromNullable(postBuilder.get().build());
        postBuilder = absent();

        ImmutableMap.Builder<String, String[]> getRoutesBuilder = new ImmutableMap.Builder<>();
        for (String key : getMap.get().keySet()) {
            getRoutesBuilder.put(key, key.split("/"));
        }
        getRoutes = Optional.fromNullable(getRoutesBuilder.build());

        ImmutableMap.Builder<String, String[]> postRoutesBuilder = new ImmutableMap.Builder<>();
        for (String key : postMap.get().keySet()) {
            postRoutesBuilder.put(key, key.split("/"));
        }
        postRoutes = Optional.fromNullable(postRoutesBuilder.build());
    }

    public static AbstractCommand getCommand(IAppiumRequest request) {
        if (getBuilder.isPresent()) throw new IllegalStateException("finish not called!");
        if (postBuilder.isPresent()) throw new IllegalStateException("finish not called!");

        String method = request.method().get();
        String route = request.route().get();

        if (method.contentEquals("GET")) {
            return getMap.get().get(route);
        } else if (method.contentEquals("POST")) {
            return postMap.get().get(route);
        } else {
            throw new RuntimeException("Unknown method: " + method.toString());
        }
    }

    public static Optional<ImmutableMap<String, String[]>> getRoutes() {
        return getRoutes;
    }

    public static Optional<ImmutableMap<String, String[]>> postRoutes() {
        return postRoutes;
    }
}
