package com.github.whileloop.rest4j.router.sun;

import com.github.whileloop.rest4j.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by aalves on 12/18/17
 */
public class SunRouter implements HttpHandler {
    private Router base;
    private Logger logger = LoggerFactory.getLogger(Router.class);

    public SunRouter(Router r) {
        if (r == null) {
            throw new NullPointerException("null sun router");
        }

        base = r;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        try {
            SunRequest req = new SunRequest(ex);
            SunResponse resp = new SunResponse(ex);
            base.handle(req, resp);
            if (!resp.sentHeaders) {
                resp.ex.sendResponseHeaders(resp.getStatus().code(), -1);
            }
        } catch (Exception e) {
            logger.error("failed to execute handler: " + e.toString());
            ex.sendResponseHeaders(500, 0);
        } finally {
            ex.getResponseBody().close();
        }
    }
}
