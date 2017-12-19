import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.middleware.LoggerMiddleware;
import com.github.whileloop.rest4j.router.sun.SunRouter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import static com.github.whileloop.rest4j.HttpMethod.POST;
import static com.github.whileloop.rest4j.HttpStatus.BAD_REQUEST;

public class UserService {
    private Datastore store;

    public UserService(Datastore store) {
        this.store = store;
    }

    SunRouter getRoutes() {
        SunRouter r = new SunRouter(); // Bring Your Own Server
        r.handle("/{uuid}", this::updateUser).setMethods(POST);
        r.handle("/", this::hello);
        return r;
    }

    private void hello(HttpRequest request, HttpResponse response) throws IOException {
        response.write("Hello world!");
    }

    private void updateUser(HttpRequest request, HttpResponse response) throws IOException {
        String uuid = request.getParam("uuid");
        if (uuid == null || uuid.isEmpty()) {
            response.error(BAD_REQUEST, "uuid not given");
            return;
        }

        JsonObject user = new JsonParser().parse(new InputStreamReader(request)).getAsJsonObject();
        store.update(user);
    }

    public static void main(String[] args) throws IOException {
        UserService userService = new UserService(new Datastore());

        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        SunRouter r = userService.getRoutes();
        r.use(new LoggerMiddleware());

        server.createContext("/v1", r);
        server.setExecutor(Executors.newSingleThreadExecutor());

        System.out.println("Starting server");
        server.start();
    }

    static class Datastore {
        public void update(JsonObject user) {
            // logic here
        }
    }
}
