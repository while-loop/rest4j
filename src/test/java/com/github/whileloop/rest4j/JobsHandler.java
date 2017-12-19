package com.github.whileloop.rest4j;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.github.whileloop.rest4j.HttpMethod.*;
import static com.github.whileloop.rest4j.HttpStatus.*;


public class JobsHandler {
    private static Gson GSON = new Gson();
    Map<String, JsonObject> store = new HashMap<>();

    Router getRoutes() {
        Router r = new Router();
        r.handleFunc("/", this::getAll);
        r.handleFunc("/", this::create).setMethods(POST);
        r.handleFunc("/{uuid}", this::get);
        r.handleFunc("/{uuid}", this::update).setMethods(PUT);
        r.handleFunc("/{uuid}", this::delete).setMethods(DELETE);
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

        JsonObject obj = new JsonParser().parse(new InputStreamReader(request)).getAsJsonObject();
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
        JsonObject obj = new JsonParser().parse(new InputStreamReader(request)).getAsJsonObject();

        String uuid = UUID.randomUUID().toString();
        obj.addProperty("uuid", UUID.randomUUID().toString());
        store.put(uuid, obj);

        response.writeHeader(CREATED);
        response.write(obj.toString().getBytes());
    }

    private void getAll(HttpRequest req, HttpResponse resp) throws IOException {
        resp.headers.set("Content-Type", "application/json");
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
