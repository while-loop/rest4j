package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.handler.FileHandler;
import com.github.whileloop.rest4j.integration.RouterTest;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import javax.servlet.http.HttpServlet;
import java.net.InetSocketAddress;

import static org.junit.Assert.fail;

/**
 * Created by aalves on 8/17/18
 */
public class JettyRouterTest extends RouterTest {
    private Server server;

    @Override
    public String before(Router apiRouter, FileHandler fileHandler) throws Exception {
        server = new Server(new InetSocketAddress(0));

        ContextHandler api = new ContextHandler("/api");
        api.setHandler(new JettyRouter(apiRouter));

        ContextHandler files = new ContextHandler("/");
        files.setHandler(new JettyRouter(fileHandler.getRouter()));

        server.setHandler(new HandlerList(api, files));
        server.start();
        return "http://localhost:" + server.getURI().getPort();
    }

    @Override
    public void after() {
        try {
            server.stop();
        } catch (Exception e) {
            fail(e.toString());
        }
    }
}