package com.github.whileloop.rest4j.router.sun;

import com.github.whileloop.rest4j.*;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by aalves on 12/18/17
 */
public class SunRouter implements HttpHandler {
    protected Router base;
    private Logger logger = LoggerFactory.getLogger(Router.class);


    public SunRouter(Router r) {
        base = r;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        HttpRequest req = new HttpRequest.Builder()
                .setBody(ex.getRequestBody())
                .setHeaders(sun2lib(ex.getRequestHeaders()))
                .setMethod(HttpMethod.valueOf(ex.getRequestMethod()))
                .setProtocol(ex.getProtocol())
                .setUrl(ex.getRequestURI().toURL())
                .build();

        HttpResponse resp = new HttpResponse.Builder()
                .setBody(ex.getResponseBody())
                .setHeaders(sun2lib(ex.getResponseHeaders()))
                .setProtocol(ex.getProtocol())
                .build();
        try {
            base.handle(req, resp);
        } catch (Exception e) {
            ex.sendResponseHeaders(500, 0);
            logger.error("failed to execute handler: " + e.toString());
        }
    }

    static HttpHeaders sun2lib(Headers headers) {
        HttpHeaders h = new HttpHeaders();

        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            h.set(e.getKey(), e.getValue());
        }
        return h;
    }
}
