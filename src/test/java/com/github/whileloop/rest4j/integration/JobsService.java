package com.github.whileloop.rest4j.integration;

import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.Router;
import com.google.gson.*;

import java.io.IOException;
import java.util.*;

import static com.github.whileloop.rest4j.HttpMethod.*;
import static com.github.whileloop.rest4j.HttpStatus.*;
import static com.github.whileloop.rest4j.integration.RouterTest.GSON;


public class JobsService {
    Map<String, JsonObject> store = new HashMap<>();

    Router getRoutes() {
        Router r = new Router();
        r.handle("/", this::getAll);
        r.handle("/", this::create).setMethods(POST);
        r.handle("/:uuid", this::get);
        r.handle("/:uuid", this::update).setMethods(PUT);
        r.handle("/:uuid", this::delete).setMethods(DELETE);
        return r;
    }

    private void update(HttpRequest request, HttpResponse response) throws IOException {
        String uuid = request.getParam("uuid");
        if (uuid == null || uuid.isEmpty()) {
            response.error(BAD_REQUEST, asJsonObject("error", "missing uuid").toString());
            return;
        }

        if (!store.containsKey(uuid)) {
            response.error(NOT_FOUND, asJsonObject("error", "object not found: " + uuid).toString());
            return;
        }

        JsonObject obj = request.bodyAsJson().getAsJsonObject();
        store.put(uuid, obj);
        response.write(obj.toString());
    }

    private void delete(HttpRequest request, HttpResponse response) throws IOException {
        String uuid = request.getParam("uuid");
        if (uuid == null || uuid.isEmpty()) {
            response.error(BAD_REQUEST, asJsonObject("error", "missing uuid").toString());
            return;
        }

        if (!store.containsKey(uuid)) {
            response.error(NOT_FOUND, asJsonObject("error", "object not found: " + uuid).toString());
            return;
        }

        response.writeHeader(NO_CONTENT);
    }

    private void get(HttpRequest request, HttpResponse response) throws IOException {
        String uuid = request.getParam("uuid");
        if (uuid == null || uuid.isEmpty()) {
            response.error(BAD_REQUEST, asJsonObject("error", "missing uuid").toString());
            return;
        }

        if (!store.containsKey(uuid)) {
            response.error(NOT_FOUND, asJsonObject("error", "object not found: " + uuid).toString());
            return;
        }

        response.write(store.get(uuid).toString());
    }

    private void create(HttpRequest request, HttpResponse response) throws IOException {
        JsonObject obj = request.bodyAsJson().getAsJsonObject();

        String uuid = UUID.randomUUID().toString();
        obj.addProperty("uuid", uuid);
        store.put(uuid, obj);

        response.writeHeader(CREATED);
        response.write(obj.toString());
    }

    private void getAll(HttpRequest req, HttpResponse resp) throws IOException {
        resp.setHeader("Content-Type", "application/json");
        resp.write(GSON.toJson(store.values()).getBytes());
    }

    private static JsonObject asJsonObject(String... vals) {
        JsonObject obj = new JsonObject();
        for (int i = 0; i < vals.length; i += 2) {
            obj.addProperty(vals[i], vals[i + 1]);
        }
        return obj;
    }
}
