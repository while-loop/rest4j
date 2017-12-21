package com.github.whileloop.rest4j;

import com.github.whileloop.rest4j.test.RequestRecorder;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.*;

/**
 * Created by aalves on 12/18/17
 */
public class HttpRequestTest {

    @Test
    public void read() {
        HttpRequest req = new RequestRecorder();
    }
}