package com.github.whileloop.rest4j;

import java.io.IOException;
import java.io.OutputStream;

import static com.github.whileloop.rest4j.HttpStatus.OK;

/**
 * Created by aalves on 12/18/17
 */
public abstract class HttpResponse {
    public HttpHeaders headers = new HttpHeaders();
    protected HttpStatus status = OK;
    private OutputStream body;

    public HttpResponse(OutputStream body) {
        this.body = body;
    }

    protected HttpResponse() {
    }

    public OutputStream getRawBody() {
        return body;
    }

    /**
     * Write the HTTP Request Header line with the given HTTP status code
     *
     * @param status
     */
    public abstract void writeHeader(HttpStatus status);

    public void write(String content) throws IOException {
        write(content.getBytes());
    }

    public void write(byte[] content) throws IOException {
        body.write(content);
    }

    public void error(HttpStatus status) throws IOException {
        error(status, "");
    }

    public void error(HttpStatus status, String message) throws IOException {
        writeHeader(status);
        write(message.getBytes());
    }

    public HttpStatus getStatus(){
        return status;
    }
}
