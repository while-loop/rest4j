package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.Handler;
import com.github.whileloop.rest4j.Middleware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerMiddleware implements Middleware {
    private Logger logger;


    public LoggerMiddleware() {
        this(null);
    }

    public LoggerMiddleware(Logger logger) {
        if (logger == null) {
            logger = LoggerFactory.getLogger(LoggerMiddleware.class);
        }

        this.logger = logger;
    }

    @Override
    public Handler handle(Handler next) {
        return (req, resp) -> {
            long start = System.currentTimeMillis();
            next.handle(req, resp);
            long elapsed = System.currentTimeMillis() - start;

//                logger.info(String.format("%-7s %-6s %d %s", ex.getRequestMethod(), elapsed + "ms", r.statusCode,ex.getRequestURI().getRawPath()));
            logger.info(String.format("%-7s %-6s %d %s", "GET", elapsed + "ms", 200, "path"));
        };
    }
}
