package com.github.whileloop.rest4j;

import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Router implements Handler {

    protected List<Route> routes = new ArrayList<>();
    protected String root = "";
    protected List<Middleware> middlewares = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(Router.class);

    public Router() {
        this("");
    }

    public Router(String root) {
        this.root = root;
    }

    public String getRoot() {
        return root;
    }

    public List<Route> getRoutes() {
        return new ArrayList<>(routes);
    }

//    @Override
//    public void handle(HttpExchange ex) throws IOException {
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
//    }

    protected void sendResponse(HttpResponse response, HttpExchange ex) throws IOException {
//        OutputStream os = null;
//        try {
//            String r = (response.body == null) ? "" : response.body.toString();
//            int offset = 0;
//            byte[] bs = r.getBytes();
//            int len = bs.length;
//            ex.sendResponseHeaders(response.statusCode, len);
//            os = ex.getResponseBody();
//            while (offset < len) {
//                int toWrite = Math.min((len - offset), 1024);
//                if (toWrite <= 0) break;
//                os.write(bs, offset, toWrite);
//                offset += toWrite;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (os != null) {
//                os.close();
//            }
//        }
    }

    Route match(HttpExchange ex) {
        for (Route r : routes) {
            if (r.matches(ex)) {
                return r;
            }
        }
        return null;
    }

    public Route handle(Route r) {
        r.path = root + r.path;
        routes.add(r);
        if (!r.strictSlash) {
            Route r2 = new Route(r);
            if (r.path.endsWith("/")) {
                r2.path = r2.path.substring(0, r2.path.length() - 1);
            } else {
                r2.path += "/";
            }
            routes.add(r2);
        }
        return r;
    }

    public Route handleFunc(String path, Handler handlerFunc) {
        return handle(path, handlerFunc);
    }

    public Route handle(String path, Handler handler) {
        return handle(new Route(path, handler));
    }

    public Router handle(String rootPath, Router router) {
        if (this == router) {
            System.err.println("trying to add routes to self");
            return this;
        }
        for (Route r : router.routes) {
            r.path = rootPath + r.path;
            handle(r);
        }
        return this;
    }

    public Router use(Middleware... middlewares) {
        this.middlewares.addAll(Arrays.asList(middlewares));
        return this;
    }

//    @Override
//    public String toString() {
//        return GSON.toJson(this);
//    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) {

    }
}
