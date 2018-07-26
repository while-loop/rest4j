package com.github.whileloop.rest4j;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Created by aalves on 12/18/17
 */
public interface HttpResponse extends HttpContext {
    OutputStream getRawBody();

    default void write(Object object) throws IOException {
        write(JsonUtils.toJson(object));
    }

    void write(byte[] content) throws IOException;

    void writeHeader(HttpStatus status) throws IOException;

    HttpStatus getStatus();

    default void write(String content) throws IOException {
        write(content.getBytes());
    }

    default void error(HttpStatus status) throws IOException {
        error(status, "");
    }

    default void error(HttpStatus status, String message) throws IOException {
        writeHeader(status);
        write(message.getBytes());
    }
}
