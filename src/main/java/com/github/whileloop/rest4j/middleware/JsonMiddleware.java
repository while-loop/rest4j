package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.Handler;
import com.github.whileloop.rest4j.HttpStatus;
import com.github.whileloop.rest4j.Middleware;

import static com.github.whileloop.rest4j.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

public class JsonMiddleware implements Middleware {

    @Override
    public Handler handle(Handler next) {
        return (req, resp) -> {
            String cl = req.headers.getFirst("Content-Length");
            String ct = req.headers.getFirst("Content-Type");
            if (cl != null || ct != null) {
                if (ct == null || !ct.contains("application/json")) {
                    resp.error(UNSUPPORTED_MEDIA_TYPE, ct);
                }
            }

            resp.headers.set("Content-Type", "application/json");
            next.handle(req, resp);
        };
    }
}
