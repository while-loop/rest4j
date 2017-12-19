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
public class HttpRequest extends InputStream {
    Map<String, Object> params = new TreeMap<>();
    public HttpHeaders headers = new HttpHeaders();
    private HttpMethod method = GET;
    private URL url;

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

    @Override
    public int read() {
        return 0;
    }

    /**
     * Get Path or Query parameter
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T extends Object> T getParam(String key) {
        return (T) params.get(key);
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
}
