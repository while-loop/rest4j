package com.github.whileloop.rest4j;

import com.github.whileloop.rest4j.test.RequestRecorder;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.HashMap;

import static org.junit.Assert.*;

public class RouteTest {
    @Test
    public void buildVars() {
        Route r = new Route("/v:version/jobs/:uuid", null);

        boolean matched = r.buildVars("/v2/jobs/a258bf18");
        assertTrue(matched);
        assertEquals(new HashMap<String, String>() {{
            put("uuid", "a258bf18");
            put("version", "2");
        }}, r.vars);
    }

    @Test
    public void sdf() {
        class T {
            public void getChores(HttpRequest req, HttpResponse resp) {
            }
        }

        T t = new T();

        Route r = new Route("", t::getChores);
    }

    @Test
    public void testLengthNotOutOfBoundsParam() throws MalformedURLException {
        Route r = new Route("/some/very/long/:path", (a, b) -> {
        });

        assertFalse(r.matches(new RequestRecorder(HttpMethod.GET, "http://localhost/short")));

        assertFalse(r.matches(new RequestRecorder(HttpMethod.GET, "http://localhost/some/very/long/")));
        assertTrue(r.matches(new RequestRecorder(HttpMethod.GET, "http://localhost/some/very/long/123")));
    }

    @Test
    public void testLengthNotOutOfBounds() throws MalformedURLException {
        Route r = new Route("/some/very/long/path", (a, b) -> {
        });

        assertFalse(r.matches(new RequestRecorder(HttpMethod.GET, "http://localhost/short")));

        assertFalse(r.matches(new RequestRecorder(HttpMethod.GET, "http://localhost/some/very/long/")));
        assertTrue(r.matches(new RequestRecorder(HttpMethod.GET, "http://localhost/some/very/long/path")));
    }
}