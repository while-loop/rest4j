package com.github.whileloop.rest4j;

import com.github.whileloop.rest4j.test.RequestRecorder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;

import java.util.*;

import static com.github.whileloop.rest4j.integration.RouterTest.GSON;
import static org.junit.Assert.assertEquals;

/**
 * Created by aalves on 12/18/17
 */
public class HttpRequestTest {
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

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof User)) return false;
            User that = (User) obj;
            return Objects.equals(first, that.first) && Objects.equals(last, that.last);
        }
    }

    @Test
    public void testRequestToString() {
        RequestRecorder req = new RequestRecorder();
        req.write(GSON.toJson(new User("Dwight", "Schrute")));
        assertEquals("{\"first\":\"Dwight\",\"last\":\"Schrute\"}", req.asString());
    }

    @Test
    public void testRequestArrayList() {
        RequestRecorder req = new RequestRecorder();
        List<User> l = Collections.singletonList(new User("Dwight", "Schrute"));
        req.write(GSON.toJson(l));
        assertEquals(l, req.asObject(new TypeToken<List<User>>() {
        }.getType()));
    }

    @Test
    public void testRequestAsObject() {
        RequestRecorder req = new RequestRecorder();
        User u = new User("Dwight", "Schrute");

        req.write(GSON.toJson(u));
        assertEquals(u, req.asObject(User.class));
    }
}