package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.test.RequestRecorder;
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

        ResponseRecorder rec = new ResponseRecorder();

        l.handle((req, resp) -> resp.writeHeader(OK)).handle(new RequestRecorder(GET, "http://localhost/"), rec);

        verify(mockedLogger).info(String.format("%-7s %-6s %d %s", "GET", "0ms", 200, "/"));
        assertEquals(OK, rec.getStatus());
    }

    @Test
    public void testNullLogCantBeSet() throws Exception {
        LoggerMiddleware l = new LoggerMiddleware();
        RequestRecorder rec = new RequestRecorder(GET, "http://localhost/");
        l.handle((req, resp) -> resp.writeHeader(OK)).handle(rec, new ResponseRecorder());
    }
}
