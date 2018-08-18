package com.github.whileloop.rest4j.router.jetty;

import com.github.whileloop.rest4j.HttpMethod;
import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.router.sun.SunRequest;
import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by aalves on 8/17/18
 */
final class JettyRequest implements HttpRequest {
    private Logger logger = LoggerFactory.getLogger(JettyRequest.class);
    private HttpServletRequest req;

    public JettyRequest(HttpServletRequest request) {
        this.req = request;
    }

    @Override
    public InputStream getRawBody() {
        try {
            return req.getInputStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.valueOf(req.getMethod().toUpperCase());
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(req.getRequestURL().toString());
        } catch (MalformedURLException e) {
            logger.error(e.toString() + ": " + req.getRequestURI());
        }
        return null;
    }

    @Override
    public InetSocketAddress remoteAddr() {
        return new InetSocketAddress(req.getRemoteHost(), req.getRemotePort());
    }

    @Override
    public String getProtocol() {
        return req.getProtocol();
    }

    @Override
    public <T> T getParam(String key) {
        return (T) req.getAttribute(key);
    }

    @Override
    public <T> void setParam(String key, T object) {
        req.setAttribute(key, object);
    }

    @Override
    public List<String> getHeader(String field) {
        List<String> headers = new ArrayList<>();
        Enumeration<String> h = req.getHeaders(field);
        while (h.hasMoreElements()) {
            headers.add(h.nextElement());
        }

        return headers;
    }

    @Override
    public void setHeader(String field, String... values) {
        throw new NotImplementedException();
    }

    HttpServletRequest getReq(){
        return req;
    }
}
