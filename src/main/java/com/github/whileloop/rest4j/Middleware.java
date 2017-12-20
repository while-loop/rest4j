package com.github.whileloop.rest4j;

import java.util.List;

public interface Middleware {
    Handler handle(Handler next);
}

class MiddlewareUtil {
    static Handler wrapMiddleware(Handler handler, List<Middleware> middlewares) {
        Handler wrapped = handler;
        for (int i = middlewares.size() - 1; i >= 0; i--) {
            wrapped = middlewares.get(i).handle(wrapped);
        }
        return wrapped;
    }
}
