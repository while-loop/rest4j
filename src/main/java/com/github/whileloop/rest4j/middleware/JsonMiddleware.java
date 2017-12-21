package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.Handler;
import com.github.whileloop.rest4j.HttpStatus;
import com.github.whileloop.rest4j.Middleware;

import static com.github.whileloop.rest4j.HttpStatus.UNSUPPORTED_MEDIA_TYPE;

public class JsonMiddleware implements Middleware {

    @Override
    public Handler handle(Handler next) {
        return (req, resp) -> {
            String cl = req.getFirstHeader("Content-Length");
            String ct = req.getFirstHeader("Content-Type");
            if (cl != null || ct != null) {
                if (ct == null || !ct.contains("application/json")) {
                    resp.error(UNSUPPORTED_MEDIA_TYPE, ct);
                    return;
                }
            }

            resp.setHeader("Content-Type", "application/json");
            next.handle(req, resp);
        };
    }
}
