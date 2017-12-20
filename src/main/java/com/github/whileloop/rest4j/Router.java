package com.github.whileloop.rest4j;

import com.sun.net.httpserver.HttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.github.whileloop.rest4j.HttpStatus.*;

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

    Route match(HttpRequest request) {
        for (Route r : routes) {
            if (r.matches(request)) {
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
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {
        try {
            Route route = match(req);
            if (route == null) {
                resp.writeHeader(NOT_FOUND);
                resp.getRawBody().close();
                System.err.println("Path not found " + req.getUrl().getPath());
                return;
            } else if (!route.methods.contains(req.getMethod())) {
                resp.writeHeader(METHOD_NOT_ALLOWED);
                resp.getRawBody().close();
                System.err.printf("Method not allowed: %s. %s", req.getMethod(), req.getUrl().getPath());
                return;
            }

            for (Map.Entry<String, String> e : route.vars.entrySet()) {
                req.setParam(e.getKey(), e.getValue());
            }

            MiddlewareUtil.wrapMiddleware(route.handler, middlewares).handle(req, resp);
        } catch (Exception e) {
            try {
                logger.error("failed to chain request", e);
                resp.error(INTERNAL_SERVER_ERROR);
            } catch (IOException e1) {
                logger.error("Failed to write header " + e1.toString());
            }
        }
    }
}
