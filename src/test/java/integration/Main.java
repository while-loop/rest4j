package integration;

import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.handler.FileHandler;
import com.github.whileloop.rest4j.middleware.JsonMiddleware;
import com.github.whileloop.rest4j.middleware.LoggerMiddleware;
import com.github.whileloop.rest4j.router.sun.SunRouter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.net.httpserver.HttpServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@Ignore("httpServer not working")
public class Main {
    private static HttpServer server;
    private static FileHandler fh;
    private static String base;


    @BeforeClass
    public static void beforeClass() throws IOException, URISyntaxException {
        Unirest.setTimeouts(250, 250);
        UsersService us = new UsersService(new UsersService.Datastore());
        PostsService ps = new PostsService(new PostsService.Datastore());

        String dir = Main.class.getClassLoader().getResource("www").toExternalForm();
        fh = new FileHandler(dir);

        Router router = new Router("/v1");
        router.use(new LoggerMiddleware(), new JsonMiddleware());
        router.handle("/users", us.getRoutes());
        router.handle("/posts", ps.getRoutes());

        server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/api", new SunRouter(router));
        server.createContext("/", new SunRouter(fh.getRouter()));
        server.setExecutor(Executors.newSingleThreadExecutor());

        server.start();
        base = "http://localhost:" + server.getAddress().getPort();
        System.out.println("lll");
    }

    @AfterClass
    public static void afterClass() {
        server.stop(0);
        fh.close();
    }

    @Test
    public void testFileHandler() throws UnirestException {
        HttpResponse<String> resp = Unirest.post(base + "/index.html").asString();
        assertThat(resp.getBody(), containsString("<title>rest4j</title>"));
        assertEquals(200, resp.getStatus());
        assertEquals("text/html", resp.getHeaders().getFirst("Content-Type"));
    }

    @Test
    public void testFileHandler2() throws UnirestException {
        HttpResponse<String> resp = Unirest.post(base + "/index.html").asString();
        assertThat(resp.getBody(), containsString("<title>rest4j</title>"));
        assertEquals(200, resp.getStatus());
        assertEquals("text/html", resp.getHeaders().getFirst("Content-Type"));
    }
}
