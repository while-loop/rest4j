import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.middleware.CorsMiddleware;
import com.github.whileloop.rest4j.middleware.JsonMiddleware;
import com.github.whileloop.rest4j.middleware.LoggerMiddleware;
import com.github.whileloop.rest4j.router.sun.SunRouter;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import static com.github.whileloop.rest4j.HttpMethod.DELETE;
import static com.github.whileloop.rest4j.HttpMethod.PUT;
import static com.github.whileloop.rest4j.HttpStatus.ACCEPTED;

public class Example {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        Router r = new Router();
        r.handle("/", (req, resp) -> {
            String raddr = req.remoteAddr().getHostName();

            resp.setHeader("Content-Type", "text/plain");
            resp.write("hello " + raddr + "!!");
        });
        r.handle("/{uuid}", Example::update).setMethods(PUT);

        server.createContext("/", new SunRouter(r)); // BYOS
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
    }

    private static void update(HttpRequest req, HttpResponse resp) throws IOException {
        String body = is2String(req.getRawBody());
        String uuid = req.getParam("uuid");
        // do something with body

        resp.writeHeader(ACCEPTED);
    }

    private static String is2String(InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is, StandardCharsets.UTF_8.name())) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    private static void chain() {
//        Router r = new Router();
//        r.use(new LoggerMiddleware(),
//                new JsonMiddleware(),
//                new CorsMiddleware());
//
//        Router v1 = new Router();
//        Router usersR = new Router();
//        usersR.handle("/", this::getAll);                               // GET /v1/users
//        usersR.handle("/:uuid", this::updateUser).setMethods(PUT);     // PUT /v1/users/:uuid
//        usersR.handle("/:uuid", this::deleteUuser).setMethods(DELETE); // DELETE /v1/users/:uuid
//
//        Router postsR = new Router();
//        usersR.handle("/", this::getAll);                                   // GET /v1/posts
//        usersR.handle("/:postId", this::updatePost).setMethods(PUT);       // PUT /v1/posts/:uuid
//        usersR.handle("/:postId", this::deletePost).setMethods(DELETE);    // DELETE /v1/posts/:uuid
//
//        v1.handle("/users", usersR); // /v1/users
//        v1.handle("/posts", usersR); // /v1/posts
//        r.handle("/v1", v1); // /v1
    }
}