package com.github.whileloop.rest4j.test;

import com.github.whileloop.rest4j.HttpHeaders;
import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.whileloop.rest4j.HttpStatus.OK;

/**
 * Created by aalves on 12/18/17
 */
public class ResponseRecorder implements HttpResponse {
    private StringBuffer buf = new StringBuffer();
    private HttpStatus status = OK;
    private HttpHeaders headers = new HttpHeaders();
    private Map<String, Object> params = new HashMap<>();

    @Override
    public void writeHeader(HttpStatus status) {
        this.status = status;
    }

    @Override
    public void error(HttpStatus status, String message) {
        this.status = status;
        buf.append(message);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public OutputStream getRawBody() {
        return null;
    }

    @Override
    public void write(byte[] content) {
        buf.append(new String(content));
    }

    public String getBody() {
        String s = buf.toString();
        buf.setLength(0);
        return s;
    }

    @Override
    public <T> T getParam(String key) {
        return (T) params.get(key);
    }

    @Override
    public <T> void setParam(String key, T object) {
        params.put(key, object);
    }

    @Override
    public List<String> getHeader(String field) {
        return headers.get(field);
    }

    @Override
    public void setHeader(String field, String... values) {
        headers.set(field, values);
    }
}
