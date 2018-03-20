package com.github.whileloop.rest4j;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import static com.github.whileloop.rest4j.HttpMethod.GET;

/**
 * Created by aalves on 12/18/17
 */
public interface HttpRequest extends HttpContext {
    JsonParser parser = new JsonParser();
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

    default JsonElement bodyAsJson(){
        return parser.parse(new InputStreamReader(getRawBody())).getAsJsonObject();
    }
}
