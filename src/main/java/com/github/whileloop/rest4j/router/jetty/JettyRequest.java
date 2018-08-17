package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.HttpMethod;
import com.github.whileloop.rest4j.HttpRequest;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;

/**
 * Created by aalves on 8/17/18
 */
final class JettyRequest implements HttpRequest {
    @Override
    public InputStream getRawBody() {
        return null;
    }

    @Override
    public HttpMethod getMethod() {
        return null;
    }

    @Override
    public URL getUrl() {
        return null;
    }

    @Override
    public InetSocketAddress remoteAddr() {
        return null;
    }

    @Override
    public String getProtocol() {
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
