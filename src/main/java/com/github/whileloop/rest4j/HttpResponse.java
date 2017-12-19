package com.github.whileloop.rest4j;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by aalves on 12/18/17
 */
public abstract class HttpResponse extends OutputStream {
    public HttpHeaders headers = new HttpHeaders();

    /**
     * Write the HTTP Request Header line with the given HTTP status code
     *
     * @param status
     */
    public abstract void writeHeader(HttpStatus status);

    public void write(String content) throws IOException {
        write(content.getBytes());
    }

    public void error(HttpStatus status) throws IOException {
        error(status, "");
    }

    public void error(HttpStatus status, String message) throws IOException {
        writeHeader(status);
        write(message.getBytes());
    }
}
