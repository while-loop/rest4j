package com.github.whileloop.rest4j.test;

import com.github.whileloop.rest4j.HttpMethod;
import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpStatus;

import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.whileloop.rest4j.HttpStatus.OK;

/**
 * Created by aalves on 12/18/17
 */
public class RequestRecorder implements HttpRequest {
    private StringBuffer buf = new StringBuffer();
    private HttpStatus status = OK;
    private HttpHeaders headers = new HttpHeaders();
    private Map<String, Object> params = new HashMap<>();
    private HttpMethod method;
    private URL url;

    public RequestRecorder(HttpMethod method, String url) throws MalformedURLException {
        this.method = method;
        this.url = new URL(url);
    }

    public RequestRecorder() {
    }

    public void write(String str) {
        buf.append(str);
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

    @Override
    public InputStream getRawBody() {
        return new StringBufferInputStream(buf.toString());
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public InetSocketAddress remoteAddr() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }
}
