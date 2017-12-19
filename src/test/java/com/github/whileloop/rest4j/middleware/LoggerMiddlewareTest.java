package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Test;
import org.slf4j.Logger;

import static com.github.whileloop.rest4j.HttpMethod.GET;
import static com.github.whileloop.rest4j.HttpStatus.OK;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class LoggerMiddlewareTest {

    @Test
    public void testSimpleLog() throws Exception {
        Logger mockedLogger = mock(Logger.class);
        LoggerMiddleware l = new LoggerMiddleware(mockedLogger);

        ResponseRecorder rec = new ResponseRecorder(null);

        l.handle((req, resp) -> resp.writeHeader(OK)).handle(new HttpRequest(GET, "http://localhost/"), rec);

        verify(mockedLogger).info(String.format("%-7s %-6s %d %s", "GET", "0ms", 200, "/"));
        assertEquals(OK, rec.status);
    }

    @Test
    public void testNullLogCantBeSet() throws Exception {
        LoggerMiddleware l = new LoggerMiddleware();
        l.handle((req, resp) -> resp.writeHeader(OK)).handle(new HttpRequest(), new ResponseRecorder(null));
    }
}
