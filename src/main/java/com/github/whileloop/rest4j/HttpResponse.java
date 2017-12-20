package com.github.whileloop.rest4j;

import java.io.IOException;
import java.io.OutputStream;

import static com.github.whileloop.rest4j.HttpStatus.OK;

/**
 * Created by aalves on 12/18/17
 */
public class HttpResponse {
    public HttpHeaders headers = new HttpHeaders();
    protected HttpStatus status = OK;
    protected OutputStream body;
    protected String protocol;

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
    public void writeHeader(HttpStatus status) throws IOException {
        this.write(String.format("%s %s %d\r\n", protocol, status.name(), status.code()));
    }

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

    public HttpStatus getStatus() {
        return status;
    }

    public static class Builder {
        private HttpHeaders headers = new HttpHeaders();
        private HttpStatus status = OK;
        private OutputStream body;
        private String protocol;

        public Builder setProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder setHeaders(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setBody(OutputStream body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            HttpResponse r = new HttpResponse();
            r.headers = headers;
            r.status = status;
            r.body = body;
            r.protocol = protocol;
            return r;
        }
    }
}
