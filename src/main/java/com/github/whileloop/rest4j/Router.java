package com.github.whileloop.rest4j;

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

    List<Route> match(HttpRequest request) {
        List<Route> rs = new ArrayList<>();
        for (Route r : routes) {
            if (r.matches(request)) {
                rs.add(r);
            }
        }
        return rs;
    }

    public Route handle(Route r) {
        r.path = root + r.path;
        routes.add(r);
        return r;
    }

    public Route handle(String path, Handler handler) {
        return handle(new Route(path, handler));
    }

    public Router handle(String rootPath, Router router) {
        if (this == router) {
            logger.warn("trying to add routes to self");
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Root: ").append(root).append(System.lineSeparator());
        sb.append("Middlewares: ");
        for (Middleware m:middlewares) {
            sb.append(m.getClass().getSimpleName()).append(" ");
        }
        sb.append(System.lineSeparator());
        sb.append("Routes: ").append(System.lineSeparator());
        for (Route m:routes) {
            sb.append(m).append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public void handle(HttpRequest req, HttpResponse resp) throws Exception {
        try {
            List<Route> routes = match(req);
            if (routes == null || routes.size() <= 0) {
                resp.writeHeader(NOT_FOUND);
                System.err.println("Path not found " + req.getUrl().getPath());
                return;
            }

            Route route = null;
            for (Route r : routes) {
                if (r.methods.contains(req.getMethod())) {
                    route = r;
                    break;
                }
            }

            if (route == null) {
                resp.writeHeader(METHOD_NOT_ALLOWED);
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
