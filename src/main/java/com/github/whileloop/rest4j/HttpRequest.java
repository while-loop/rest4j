package com.github.whileloop.rest4j;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

import static com.github.whileloop.rest4j.HttpMethod.GET;

/**
 * Created by aalves on 12/18/17
 */
public class HttpRequest {
    protected Map<String, Object> params = new TreeMap<>();
    public HttpHeaders headers = new HttpHeaders();
    protected HttpMethod method = GET;
    protected URL url;
    protected InputStream body;
    protected String protocol;

    {
        try {
            url = new URL("http://localhost");
        } catch (MalformedURLException ignored) {
        }
    }

    public HttpRequest() {
    }

    public HttpRequest(HttpMethod method, String path) throws MalformedURLException {
        this.method = method;
        this.url = new URL(path);
    }

    /**
     * If getBody or getRawBody has already been called, this stream will be empty
     *
     * @return
     */
    public InputStream getRawBody() {
        return body;
    }

    /**
     * Get Path or Query parameter
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getParam(String key) {
        return (T) params.get(key);
    }

    public <T> void setParam(String key, T object) {
        this.params.put(key, object);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public URL getUrl() {
        return url;
    }

    public String remoteAddr() {
        return null;
    }

    public String getProtocol() {
        return protocol;
    }


    public static class Builder {
        private Map<String, Object> params = new TreeMap<>();
        private HttpHeaders headers = new HttpHeaders();
        private HttpMethod method = GET;
        private URL url;
        private InputStream body;
        private String protocol;

        public Builder setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Builder setHeaders(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder setMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder setUrl(URL url) {
            this.url = url;
            return this;
        }

        public Builder setBody(InputStream body) {
            this.body = body;
            return this;
        }

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public HttpRequest build() {
            HttpRequest r = new HttpRequest();
            r.params = params;
            r.headers = headers;
            r.method = method;
            r.url = url;
            r.body = body;
            r.protocol = protocol;
            return r;
        }
    }
}
