package integration;

import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.HttpStatus;
import com.github.whileloop.rest4j.Router;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.github.whileloop.rest4j.HttpMethod.POST;

public class UsersService {
    private Datastore store;

    public UsersService(Datastore store) {
        this.store = store;
    }

    Router getRoutes() {
        Router r = new Router(); // Bring Your Own Server
        r.handle("/", this::createUser).setMethods(POST);
        r.handle("/", this::getUsers);
        return r;
    }

    private void getUsers(HttpRequest req, HttpResponse resp) {
    }

    private void createUser(HttpRequest req, HttpResponse resp) throws IOException {
        JsonObject obj = req.bodyAsJson().getAsJsonObject();
        store.create(obj);

        resp.writeHeader(HttpStatus.CREATED);
        resp.write(obj.toString());
    }

    static class Datastore {
        private List<JsonObject> users = new ArrayList<>();

        public void create(JsonObject user) {
            users.add(user);
        }

        public List<JsonObject> getAll() {
            return new ArrayList<>(users);
        }
    }
}
