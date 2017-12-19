package com.github.whileloop.rest4j;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class MockHttpExchange extends HttpExchange {
    public String method;
    public URI uri;
    public Map<String, Object> attrs = new HashMap<>();
    public Headers reqHeaders = new Headers();
    public Headers respHeaders = new Headers();
    public FakeOutputStream fos = new FakeOutputStream();
    public int statusCode = -1;
    public long contentLength = -1;


    public MockHttpExchange() {
    }

    public MockHttpExchange(String method) {
        this.method = method;
    }

    public MockHttpExchange(String method, String uri) throws URISyntaxException, IOException {
        this(method, new URI(uri));
    }

    public MockHttpExchange(String method, URI uri) throws IOException {
        this.method = method;
        this.uri = uri;
    }

    @Override
    public Headers getRequestHeaders() {
        return reqHeaders;
    }

    @Override
    public Headers getResponseHeaders() {
        return respHeaders;
    }

    @Override
    public URI getRequestURI() {
        return uri;
    }

    @Override
    public String getRequestMethod() {
        return method;
    }

    @Override
    public HttpContext getHttpContext() {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public InputStream getRequestBody() {
        return null;
    }

    @Override
    public OutputStream getResponseBody() {
        return fos;
    }

    @Override
    public void sendResponseHeaders(int i, long l) throws IOException {
        statusCode = i;
        contentLength = l;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public int getResponseCode() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return attrs.get(s);
    }

    @Override
    public void setAttribute(String s, Object o) {
        attrs.put(s, o);
    }

    @Override
    public void setStreams(InputStream inputStream, OutputStream outputStream) {

    }

    @Override
    public HttpPrincipal getPrincipal() {
        return null;
    }

    public String getBody() {
        return fos.toString();
    }

    class FakeOutputStream extends OutputStream {
        StringBuilder sb = new StringBuilder();

        @Override
        public void write(int b) throws IOException {
            sb.append((char) b);
        }

        @Override
        public String toString() {
            String out = sb.toString();
            sb.setLength(0);
            return out;
        }
    }
}
