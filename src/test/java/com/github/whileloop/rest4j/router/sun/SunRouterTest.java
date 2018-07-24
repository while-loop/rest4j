package com.github.whileloop.rest4j.router.sun;

import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.handler.FileHandler;
import com.github.whileloop.rest4j.integration.RouterTest;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by aalves on 7/24/18
 */
public class SunRouterTest extends RouterTest {

    private HttpServer server;

    @Override
    public String before(Router apiRouter, FileHandler fileHandler) throws Exception {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/api", new SunRouter(apiRouter));
        server.createContext("/", new SunRouter(fileHandler.getRouter()));
        server.setExecutor(Executors.newSingleThreadExecutor());

        server.start();
        return "http://localhost:" + server.getAddress().getPort();
    }

    @Override
    public void after() {
        server.stop(0);
    }
}