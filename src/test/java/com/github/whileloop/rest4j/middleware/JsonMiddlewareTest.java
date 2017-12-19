package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.*;
import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;

import static com.github.whileloop.rest4j.HttpMethod.GET;
import static com.github.whileloop.rest4j.HttpStatus.OK;
import static com.github.whileloop.rest4j.HttpStatus.UNSUPPORTED_MEDIA_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class JsonMiddlewareTest {
    private JsonMiddleware j = new JsonMiddleware();

    @Test
    public void testCLnotCTnotPasses() throws Exception {
        HttpResponse mocked = mock(HttpResponse.class);
        mocked.headers = new HttpHeaders();

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(new HttpRequest(), mocked);

        assertEquals("application/json", mocked.headers.getFirst("Content-Type"));
        verify(mocked).writeHeader(OK);
    }

    @Test
    public void testCLsetCTnot() throws Exception {
        HttpResponse mocked = mock(HttpResponse.class);
        mocked.headers = new HttpHeaders();

        HttpRequest r = new HttpRequest(GET, "http://localhost");
        r.headers.set("Content-Length", "5412");

        j.handle((req, resp) -> {/*empty handler*/}).handle(r, mocked);
        verify(mocked, times(1)).error(UNSUPPORTED_MEDIA_TYPE, null);
    }

    @Test
    public void testCLsetCTsetFails() throws Exception {
        HttpResponse mocked = mock(HttpResponse.class);
        mocked.headers = new HttpHeaders();

        HttpRequest r = new HttpRequest(GET, "http://localhost");
        r.headers.set("Content-Length", "5412");
        r.headers.set("Content-Type", "application/xml");

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(r, mocked);
        verify(mocked, times(1)).error(UNSUPPORTED_MEDIA_TYPE, "application/xml");
    }

    @Test
    public void testCLnotCTset() throws Exception {
        HttpResponse mocked = mock(HttpResponse.class);
        mocked.headers = new HttpHeaders();

        HttpRequest r = new HttpRequest(GET, "http://localhost");
        r.headers.set("Content-Type", "application/xml");

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(r, mocked);
        verify(mocked, times(1)).error(UNSUPPORTED_MEDIA_TYPE, "application/xml");
    }

    @Test
    public void testCLsetCTsetPasses() throws Exception {
        HttpResponse mocked = mock(HttpResponse.class);
        mocked.headers = new HttpHeaders();

        HttpRequest r = new HttpRequest(GET, "http://localhost");
        r.headers.set("Content-Type", "application/json");
        r.headers.set("Content-Length", "5412");

        j.handle((req, resp) -> resp.writeHeader(OK)).handle(r, mocked);

        verify(mocked, times(1)).writeHeader(OK);
        assertEquals("application/json", mocked.headers.getFirst("Content-Type"));
    }
}
