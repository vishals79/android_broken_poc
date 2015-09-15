/*
 * Copyright 2014 eBay Software Foundation and selendroid committers.
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
package io.selendroid.server.common.http;

import java.util.ArrayList;
import java.util.List;

import io.appium.uiautomator2.util.Log;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class HttpServer {
    private final int port;
    private Thread serverThread;
    private final List<IHttpServlet> handlers = new ArrayList<IHttpServlet>();

    public HttpServer(int port) {
        this.port = port;
    }

    public void addHandler(IHttpServlet handler) {
        handlers.add(handler);
    }

    public void start() {
        Log.i("io.selendroid.server.common.http.HttpServer start() invoked!");
        if (serverThread != null) {
            throw new IllegalStateException("Server is already running");
        }
        serverThread = new Thread() {
            @Override
            public void run() {
                Log.i("Server thread run method invoked. Handlers size: " + handlers.size());
                EventLoopGroup bossGroup = new NioEventLoopGroup();
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    ServerBootstrap bootstrap = new ServerBootstrap();

                    bootstrap.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .childHandler(new ServerInitializer(handlers))
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .childOption(ChannelOption.SO_KEEPALIVE, true);

                    ChannelFuture f = bootstrap.bind(port).sync();
                    f.channel().closeFuture().sync();
                } catch (InterruptedException ignored) {
                } finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            }
        };
        serverThread.start();
    }

    public void stop() {
        if (serverThread == null) {
            throw new IllegalStateException("Server is not running");
        }
        serverThread.interrupt();
    }

    public int getPort() {
        return port;
    }

}
