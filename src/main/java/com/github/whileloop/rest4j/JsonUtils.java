package com.github.whileloop.rest4j;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    static final Gson GSON = new Gson();
    static final JsonParser PARSER = new JsonParser();

    static JsonElement asJson(Reader reader) {
        return PARSER.parse(reader);
    }

    static <T> T asObject(Reader reader, Class<T> clazz) {
        return GSON.fromJson(reader, clazz);
    }

    static <T> T asObject(Reader reader, Type type) {
        return GSON.fromJson(reader, type);
    }

    public static String is2String(InputStream is) {
        try (java.util.Scanner s = new java.util.Scanner(is, StandardCharsets.UTF_8.name())) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    static String toJson(Object object) {
        return GSON.toJson(object);
    }
}
