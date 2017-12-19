package com.github.whileloop.rest4j;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by aalves on 12/18/17
 */
public class HttpResponseTest {

    @Test
    public void write() {
        HttpResponse resp = new HttpResponse(null) {
            public void write(int b) throws IOException {

            }

            @Override
            public void writeHeader(HttpStatus status) {

            }
        };
    }
}