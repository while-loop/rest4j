package com.github.whileloop.rest4j;

import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Before;
import org.junit.Test;

import static com.github.whileloop.rest4j.HttpMethod.*;
import static com.github.whileloop.rest4j.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class RouterTest {

    private Router r;

    @Before
    public void before() {
        r = new Router();
    }

    @Test
    public void match() throws Exception {
        r.handle("/", (req, resp) -> resp.writeHeader(CONTINUE));
        r.handle("/", (req, resp) -> resp.writeHeader(SWITCHING_PROTOCOLS)).setMethods(POST);
        r.handle("/where", (req, resp) -> resp.writeHeader(PROCESSING)).setMethods(POST);
        r.handle("/are/you", (req, resp) -> {
            resp.writeHeader(OK);
            resp.write("yoo");
        });

        Router j = new Router();
        j.handle("/", (req, resp) -> resp.writeHeader(CREATED));
        j.handle("/", (req, resp) -> resp.writeHeader(ACCEPTED)).setMethods(POST);
        j.handle("/:uuid", (req, resp) -> resp.writeHeader(NONAUTHORITATIVE_INFO));
        j.handle("/:uuid", (req, resp) -> resp.writeHeader(NO_CONTENT)).setMethods(PUT);
        j.handle("/:uuid", (req, resp) -> {
            String uuid = req.getParam("uuid");
            boolean found = false;
            if (uuid != null && uuid.equals("8654ac51")) {
                found = true;
            }

            resp.writeHeader(RESET_CONTENT);
            resp.write(found + "");
        }).setMethods(DELETE);
        r.handle("/jobs", j);

        HttpRequest req = new HttpRequest(GET, "http://localhost/are/you");
        ResponseRecorder rec = new ResponseRecorder();
        r.handle(req, rec);
        assertEquals(OK, rec.getStatus());
        //  assertEquals("3", rec.headers.getFirst("Content-Length")); TODO
        assertEquals("yoo", rec.getBody());

        req = new HttpRequest(DELETE, "http://localhost/jobs/8654ac51");
        rec = new ResponseRecorder();
        r.handle(req, rec);
        assertEquals(RESET_CONTENT, rec.getStatus());
        //   assertEquals("4", rec.headers.getFirst("Content-Length")); TODO
        assertEquals("true", rec.getBody());
    }

    @Test
    public void testStrictSlashes() throws Exception {
        r.handle("/", (req, resp) -> resp.writeHeader(PARTIAL_CONTENT));
        r.handle("/yellow", (req, resp) -> resp.writeHeader(MULTISTATUS)).setMethods(POST);
        assertEquals(2, r.routes.size());

        HttpRequest req = new HttpRequest(GET, "http://localhost/");
        ResponseRecorder rec = new ResponseRecorder();
        r.handle(req, rec);
        assertEquals(PARTIAL_CONTENT, rec.getStatus());

        req = new HttpRequest(GET, "http://localhost");
        r.handle(req, rec);
        assertEquals(PARTIAL_CONTENT, rec.getStatus());

        req = new HttpRequest(POST, "http://localhost/yellow/");
        rec = new ResponseRecorder();
        r.handle(req, rec);
        assertEquals(MULTISTATUS, rec.getStatus());

        req = new HttpRequest(POST, "http://localhost/yellow");
        rec = new ResponseRecorder();
        r.handle(req, rec);
        assertEquals(MULTISTATUS, rec.getStatus());
    }
}