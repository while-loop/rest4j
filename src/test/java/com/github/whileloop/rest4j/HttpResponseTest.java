package com.github.whileloop.rest4j;

import com.github.whileloop.rest4j.test.RequestRecorder;
import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Test;

import java.io.IOException;

import static com.github.whileloop.rest4j.HttpRequest.GSON;
import static org.junit.Assert.*;

/**
 * Created by aalves on 12/18/17
 */
public class HttpResponseTest {

    @Test
    public void write() {
        HttpResponse resp = new ResponseRecorder();
    }


    @Test
    public void testJsonRespObj() throws IOException {
        ResponseRecorder resp = new ResponseRecorder();
        resp.write(new HttpRequestTest.User("Dwight", "Schrute"));


        assertEquals("{\"first\":\"Dwight\",\"last\":\"Schrute\"}", resp.getBody());
    }
}