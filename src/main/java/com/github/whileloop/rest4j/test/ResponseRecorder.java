package com.github.whileloop.rest4j.test;

import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by aalves on 12/18/17
 */
public class ResponseRecorder extends HttpResponse {
    private StringBuffer buf = new StringBuffer();

    public ResponseRecorder() {

    }

    public ResponseRecorder(OutputStream body) {
        super(body);
    }

    @Override
    public void writeHeader(HttpStatus status) {
        this.status = status;
    }

    @Override
    public void error(HttpStatus status, String message) {
        this.status = status;
        buf.append(message);
    }

    @Override
    public void write(byte[] content) {
        buf.append(new String(content));
    }

    public String getBody(){
        String s = buf.toString();
        buf.setLength(0);
        return s;
    }
}
