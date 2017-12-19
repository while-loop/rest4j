package com.github.whileloop.rest4j;

public interface Middleware {
    Handler handle(Handler next);
}
