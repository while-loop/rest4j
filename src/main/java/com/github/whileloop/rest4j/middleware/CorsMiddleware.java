package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.Handler;
import com.github.whileloop.rest4j.Middleware;

public class CorsMiddleware implements Middleware {
    @Override
    public Handler handle(Handler next) {
        return ((req, resp) -> {
            resp.headers.set("Access-Control-Allow-Origin", "*");
            resp.headers.set("Access-Control-Allow-Methods", "*");
            resp.headers.set("Access-Control-Allow-Headers", "Origin", "X-Requested-With", "Content-Type", "Accept");
            next.handle(req, resp);
        });
    }
}
