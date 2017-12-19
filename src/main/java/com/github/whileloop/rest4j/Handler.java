package com.github.whileloop.rest4j;

/**
 * Created by aalves on 12/18/17
 */
public interface Handler {
    void handle(HttpRequest req, HttpResponse resp) throws Exception;
}
