package io.selendroid.server.common.http;

public class TmpSelendroidServer {

    public static void startServer() {
        HttpServer httpServer = new HttpServer(8080);

        httpServer.addHandler(new HttpServlet() {
            @Override
            public void handleHttpRequest(HttpRequest httpRequest, HttpResponse httpResponse) throws Exception {
                httpResponse.setContent("Hi there from selendroid");
            }
        });

        httpServer.start();
    }
}
