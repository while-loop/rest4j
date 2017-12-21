package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.test.RequestRecorder;
import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Test;

import static com.github.whileloop.rest4j.HttpMethod.GET;
import static com.github.whileloop.rest4j.HttpStatus.OK;
import static com.github.whileloop.rest4j.HttpStatus.UNSUPPORTED_MEDIA_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class JsonMiddlewareTest {
    private JsonMiddleware j = new JsonMiddleware();

    @Test
    public void testCLnotCTnotPasses() throws Exception {
        HttpResponse mocked = new ResponseRecorder();

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(new RequestRecorder(), mocked);

        assertEquals("application/json", mocked.getFirstHeader("Content-Type"));
        assertEquals(OK, mocked.getStatus());
    }

    @Test
    public void testCLsetCTnot() throws Exception {
        HttpResponse mocked = new ResponseRecorder();

        HttpRequest r = new RequestRecorder(GET, "http://localhost");
        r.setHeader("Content-Length", "5412");

        j.handle((req, resp) -> {/*empty handler*/}).handle(r, mocked);
        assertEquals(UNSUPPORTED_MEDIA_TYPE, mocked.getStatus());
    }

    @Test
    public void testCLsetCTsetFails() throws Exception {
        ResponseRecorder mocked = new ResponseRecorder();

        HttpRequest r = new RequestRecorder(GET, "http://localhost");
        r.setHeader("Content-Length", "5412");
        r.setHeader("Content-Type", "application/xml");

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(r, mocked);
        assertEquals(UNSUPPORTED_MEDIA_TYPE, mocked.getStatus());
        assertEquals("application/xml", mocked.getBody());
    }

    @Test
    public void testCLnotCTset() throws Exception {
        ResponseRecorder mocked = new ResponseRecorder();

        HttpRequest r = new RequestRecorder(GET, "http://localhost");
        r.setHeader("Content-Type", "application/xml");

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(r, mocked);
        assertEquals(mocked.getStatus(), UNSUPPORTED_MEDIA_TYPE);
        assertEquals(mocked.getBody(), "application/xml");
    }

    @Test
    public void testCLsetCTsetPasses() throws Exception {
        HttpResponse mocked = new ResponseRecorder();

        HttpRequest r = new RequestRecorder(GET, "http://localhost");
        r.setHeader("Content-Type", "application/json");
        r.setHeader("Content-Length", "5412");

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(r, mocked);

        assertEquals(OK, mocked.getStatus());
        assertEquals("application/json", mocked.getFirstHeader("Content-Type"));
    }
}
