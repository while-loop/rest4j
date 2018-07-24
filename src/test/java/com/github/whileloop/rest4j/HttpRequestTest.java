package com.github.whileloop.rest4j;

import com.github.whileloop.rest4j.test.RequestRecorder;
import org.junit.Test;

import java.net.MalformedURLException;

import static com.github.whileloop.rest4j.HttpRequest.GSON;
import static org.junit.Assert.*;

/**
 * Created by aalves on 12/18/17
 */
public class HttpRequestTest {

    @Test
    public void read() {
        HttpRequest req = new RequestRecorder();
    }

    @Test
    public void testJsonReqObj() {
        RequestRecorder req = new RequestRecorder();
        req.write(GSON.toJson(new User("Dwight", "Schrute")));

        User user = req.asObject(User.class);
        assertEquals("Dwight", user.first);
        assertEquals("Schrute", user.last);
    }

    public static class User {
        public String first;
        public String last;

        public User(String first, String last) {
            this.first = first;
            this.last = last;
        }
    }
}