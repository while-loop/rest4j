package com.github.whileloop.rest4j;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URL;

/**
 * Created by aalves on 12/18/17
 */
public interface HttpRequest extends HttpContext {
    /**
     * If getBody or getRawBody has already been called, this stream will be empty
     *
     * @return
     */
    InputStream getRawBody();

    HttpMethod getMethod();

    URL getUrl();

    InetSocketAddress remoteAddr();

    String getProtocol();

    default JsonElement asJson() {
        return JsonUtils.asJson(new InputStreamReader(getRawBody()));
    }

    default String asString() {
        return JsonUtils.is2String(getRawBody());
    }

    default <T> T asObject(Class<T> clazz) {
        return JsonUtils.asObject(new InputStreamReader(getRawBody()), clazz);
    }

    default <T> T asObject(Type type) {
        return JsonUtils.asObject(new InputStreamReader(getRawBody()), type);
    }
}
