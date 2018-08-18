package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.HttpStatus;
import com.github.whileloop.rest4j.router.sun.SunResponse;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by aalves on 8/17/18
 */
final class JettyResponse implements HttpResponse {
    private Logger logger = LoggerFactory.getLogger(SunResponse.class);
    private HttpServletResponse resp;
    private HttpStatus sentStatus = HttpStatus.OK;
    boolean sentHeaders = false;

    public JettyResponse(HttpServletResponse response) {
        this.resp = response;
    }

    @Override
    public OutputStream getRawBody() {
        try {
            return resp.getOutputStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void write(byte[] content) throws IOException {
        sentHeaders = true;

        resp.setStatus(sentStatus.code());
        resp.setContentLength(content.length);
        getRawBody().write(content);
        getRawBody().flush();
    }

    @Override
    public void writeHeader(HttpStatus status) throws IOException {
        sentStatus = status;
    }

    @Override
    public HttpStatus getStatus() {
        return sentStatus;
    }

    @Override
    public <T> T getParam(String key) {
        // not impl
        return null;
    }

    @Override
    public <T> void setParam(String key, T object) {
        // not impl
    }

    @Override
    public List<String> getHeader(String field) {
        return new ArrayList<>(resp.getHeaders(field));
    }

    @Override
    public void setHeader(String field, String... values) {
        if (values.length > 0) {
            resp.setHeader(field, values[0]);
        }

        for (int i = 1; i < values.length; i++) {
            resp.addHeader(field, values[i]);
        }
    }
}
