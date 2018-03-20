package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.HttpResponse;
import com.github.whileloop.rest4j.test.RequestRecorder;
import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Test;

import java.util.Arrays;

import static com.github.whileloop.rest4j.HttpMethod.GET;
import static com.github.whileloop.rest4j.HttpStatus.OK;
import static org.junit.Assert.assertEquals;

public class CorsMiddlewareTest {

    @Test
    public void testCorsSetsBeforeHandler() throws Exception {
        CorsMiddleware c = new CorsMiddleware();

        ResponseRecorder rec = new ResponseRecorder();
        c.handle((req, resp) -> {
            assertCors(resp);
            resp.writeHeader(OK);
        }).handle(new RequestRecorder(GET, "http://localhost/"), rec);

        assertCors(rec);
    }

    private void assertCors(HttpResponse resp) {
        assertEquals("*", resp.getFirstHeader("Access-Control-Allow-Origin"));
        assertEquals("*", resp.getFirstHeader("Access-Control-Allow-Methods"));
        assertEquals(Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept"),
                resp.getHeader("Access-Control-Allow-Headers"));
    }
}
