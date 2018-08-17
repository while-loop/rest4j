package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by aalves on 8/17/18
 */
final class JettyResponse implements HttpResponse {
    @Override
    public OutputStream getRawBody() {
        return null;
    }

    @Override
    public void write(byte[] content) throws IOException {

    }

    @Override
    public void writeHeader(HttpStatus status) throws IOException {

    }

    @Override
    public HttpStatus getStatus() {
        return null;
    }

    @Override
    public <T> T getParam(String key) {
        return null;
    }

    @Override
    public <T> void setParam(String key, T object) {

    }

    @Override
    public List<String> getHeader(String field) {
        return null;
    }

    @Override
    public void setHeader(String field, String... values) {

    }
}
