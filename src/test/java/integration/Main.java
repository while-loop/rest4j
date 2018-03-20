package integration;

import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.handler.FileHandler;
import com.github.whileloop.rest4j.middleware.JsonMiddleware;
import com.github.whileloop.rest4j.middleware.LoggerMiddleware;
import com.github.whileloop.rest4j.router.sun.SunRouter;
import com.google.gson.Gson;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.net.httpserver.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

public class Main {
    public static Gson GSON = new Gson();

    private static FileHandler fh;
    private static Router apiRouter;


    @Before
    public void before() throws IOException, URISyntaxException {
        UsersService us = new UsersService(new UsersService.Datastore());
        JobsService js = new JobsService();

        String dir = Main.class.getClassLoader().getResource("www").toExternalForm();
        fh = new FileHandler(dir);

        apiRouter = new Router("/api/v1");
        apiRouter.use(new LoggerMiddleware(), new JsonMiddleware());
        apiRouter.handle("/jobs", js.getRoutes());
        apiRouter.handle("/users", us.getRoutes());
    }

    @After
    public void after() {
        fh.close();
    }

    @Test
    public void testSunRouter() throws IOException, UnirestException {
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/api", new SunRouter(apiRouter));
        server.createContext("/", new SunRouter(fh.getRouter()));
        server.setExecutor(Executors.newSingleThreadExecutor());

        server.start();
        String base = "http://localhost:" + server.getAddress().getPort();
        Runner.test(base);
        server.stop(0);
    }
}
