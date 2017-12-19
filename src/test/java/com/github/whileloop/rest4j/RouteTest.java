package com.github.whileloop.rest4j;

import org.junit.Test;

import java.util.HashMap;

import static com.github.whileloop.rest4j.HttpMethod.GET;
import static org.junit.Assert.*;

public class RouteTest {
    @Test
    public void buildVars() throws Exception {
        Route r = new Route("/v:version/jobs/:uuid", null);

        boolean matched = r.buildVars("/v2/jobs/a258bf18");
        assertTrue(matched);
        assertEquals(new HashMap<String, String>(){{
            put("uuid", "a258bf18");
            put("version", "2");
        }}, r.vars);
    }

    @Test
    public void sdf(){
         class T {
            Router r;
            public T(){
                r.handle("", this::getChores).setMethods(GET);
            }
            public void getChores(HttpRequest req, HttpResponse resp) {
            }
        }

        T t = new T();

        Route r = new Route("", t::getChores);
    }
}