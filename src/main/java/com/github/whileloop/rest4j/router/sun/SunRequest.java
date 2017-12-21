package com.github.whileloop.rest4j.router.sun;

import com.github.whileloop.rest4j.HttpMethod;
import com.github.whileloop.rest4j.HttpRequest;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class SunRequest implements HttpRequest {
    private Logger logger = LoggerFactory.getLogger(SunRequest.class);
    private HttpExchange ex;

    SunRequest(HttpExchange ex) {
        this.ex = ex;
    }

    @Override
    public InputStream getRawBody() {
        return ex.getRequestBody();
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.valueOf(ex.getRequestMethod());
    }

    @Override
    public URL getUrl() {
        try {
            int port = ex.getLocalAddress().getPort();
            String url = String.format("http%s://%s%s%s",
                    "", // scheme
                    ex.getLocalAddress().getHostName(), // hostname
                    (port == 80) ? "" : ":" + port, // port or empty
                    ex.getRequestURI().getRawPath()); // raw path
            return new URL(url);
        } catch (MalformedURLException e) {
            logger.error(e.toString() + ": " + ex.getRequestURI().toString());
        }
        return null;
    }

    @Override
    public InetSocketAddress remoteAddr() {
        return ex.getRemoteAddress();
    }

    @Override
    public String getProtocol() {
        return ex.getProtocol();
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
        return ex.getRequestHeaders().get(field);
    }

    @Override
    public void setHeader(String field, String... values) {
        ex.getRequestHeaders().put(field, Arrays.asList(values));
    }
}
