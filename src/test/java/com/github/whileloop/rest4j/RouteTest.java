package com.github.whileloop.rest4j;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}