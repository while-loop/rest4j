package com.github.whileloop.rest4j.router.sun;

import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.HttpStatus;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

public class SunResponse implements HttpResponse {
    private Logger logger = LoggerFactory.getLogger(SunResponse.class);
    HttpExchange ex;
    private HttpStatus sentStatus = null;
    boolean sentHeaders = false;

    SunResponse(HttpExchange ex) {
        this.ex = ex;
    }

    @Override
    public <T> T getParam(String key) {
        return (T) ex.getAttribute(key);
    }

    @Override
    public <T> void setParam(String key, T object) {
        ex.setAttribute(key, object);
    }

    @Override
    public List<String> getHeader(String field) {
        return ex.getResponseHeaders().get(field);
    }

    @Override
    public void setHeader(String field, String... values) {
        ex.getResponseHeaders().put(field, Arrays.asList(values));
    }

    @Override
    public OutputStream getRawBody() {
        return ex.getResponseBody();
    }

    @Override
    public void write(byte[] content) throws IOException {
        sentHeaders = true;
        ex.sendResponseHeaders(sentStatus.code(), content.length);
        ex.getResponseBody().write(content);
    }

    @Override
    public void writeHeader(HttpStatus status) throws IOException {
        this.sentStatus = status;
    }

    @Override
    public HttpStatus getStatus() {
        return sentStatus;
    }
}
