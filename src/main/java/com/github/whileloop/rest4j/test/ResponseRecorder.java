package com.github.whileloop.rest4j.test;

import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.HttpStatus;

import java.io.IOException;

/**
 * Created by aalves on 12/18/17
 */
public class ResponseRecorder extends HttpResponse {
    public HttpStatus status;
    private StringBuffer buf = new StringBuffer();


    @Override
    public void writeHeader(HttpStatus status) {
        this.status = status;
    }

    @Override
    public void error(HttpStatus status, String message) throws IOException {
        this.status = status;
        buf.append(message);
    }

    @Override
    public void write(int b) throws IOException {
        buf.append(b);
    }

    public String getBody(){
        String s = buf.toString();
        buf.setLength(0);
        return s;
    }
}
