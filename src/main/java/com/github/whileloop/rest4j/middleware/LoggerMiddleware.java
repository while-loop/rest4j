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
            // get the time before passing the request down the chain of middleware
            long start = System.currentTimeMillis();

            next.handle(req, resp); // apply the next handle

            long elapsed = System.currentTimeMillis() - start;
            logger.info(String.format("%-7s %-6s %d %s",
                    req.getMethod(), elapsed + "ms", resp.getStatus().code(), req.getUrl().getPath()));
        };
    }
}
