package com.github.whileloop.rest4j;

import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Test;

import java.io.IOException;

import static com.github.whileloop.rest4j.HttpMethod.GET;
import static com.github.whileloop.rest4j.HttpStatus.OK;
import static org.junit.Assert.assertEquals;

public class MiddlewareTest {


    @Test
    public void testOrder() throws IOException {
        Router r = new Router();
        r.use(
                new TestMiddleware("A"),
                new TestMiddleware("B"),
                new TestMiddleware("C")
        );
        r.handle("/", (req, resp) -> {
            req.setParam("before", getOrEmpty(req.getParam("before")) + "D");
            req.setParam("after", getOrEmpty(req.getParam("after")) + "D");
            resp.writeHeader(OK);
            resp.write("got it");
        });

        HttpRequest req = new HttpRequest(GET, "http://localhost/");
        ResponseRecorder rec = new ResponseRecorder();
        r.handle(req, rec);
        assertEquals(OK, rec.getStatus());
        //    assertEquals("got it".length(), rec.contentLength); TODO
        assertEquals("got it", rec.getBody());
        assertEquals("ABCD", req.getParam("before"));
        assertEquals("DCBA", req.getParam("after"));

        req = new HttpRequest(GET, "http://localhost/");
        rec = new ResponseRecorder();
        r.handle(req, rec);
        assertEquals(OK, rec.getStatus());
        //assertEquals("got it".length(), mocked.contentLength); TODO
        assertEquals("got it", rec.getBody());
        assertEquals("ABCD", req.getParam("before"));
        assertEquals("DCBA", req.getParam("after"));
    }

    private class TestMiddleware implements Middleware {

        private String name;

        public TestMiddleware(String name) {
            this.name = name;
        }

        @Override
        public Handler handle(Handler next) {
            return (req, resp) -> {
                req.setParam("before", getOrEmpty(req.getParam("before")) + name);
                next.handle(req, resp);
                req.setParam("after", getOrEmpty(req.getParam("after")) + name);
            };
        }
    }

    private String getOrEmpty(String s) {
        return (s == null) ? "" : s;
    }
}