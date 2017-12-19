package com.github.whileloop.rest4j.router.sun;

import com.github.whileloop.rest4j.Router;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Created by aalves on 12/18/17
 */
public class SunRouter implements HttpHandler {
    Router base;

    public SunRouter(Router r) {
        base = r;
    }

    @Override
    public void handle(HttpExchange ex) {

//        HttpResponse response;
//        try {
//            Route route = match(ex);
//            if (route == null) {
//                ex.sendResponseHeaders(Http.STATUS_NOT_FOUND, 0);
//                ex.getResponseBody().close();
//                System.err.println("Path not found " + ex.getRequestURI());
//                return;
//            }
//
//            for (Map.Entry<String, String> e : route.vars.entrySet()) {
//                ex.setAttribute(e.getKey(), e.getValue());
//            }
//
//            sendResponse(new ChainImpl(middlewares, route.handler).call(ex), ex);
//        } catch (Exception e) {
//            logger.error("failed to chain request", e);
//            sendResponse(new HttpResponse<>(Http.STATUS_INTERNAL_SERVER_ERROR, e.toString()), ex);
//        }
    }
}
