package com.github.whileloop.rest4j.middleware;

import com.github.whileloop.rest4j.HttpHeaders;
import com.github.whileloop.rest4j.HttpRequest;
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

        ResponseRecorder rec = new ResponseRecorder(null);
        c.handle((req, resp) -> {
            assertCors(resp.headers);
            resp.writeHeader(OK);
        }).handle(new HttpRequest(GET, "http://localhost/"), rec);

        assertCors(rec.headers);
    }

    private void assertCors(HttpHeaders headers){
        assertEquals("*", headers.getFirst("Access-Control-Allow-Origin"));
        assertEquals("*", headers.getFirst("Access-Control-Allow-Methods"));
        assertEquals(Arrays.asList("Origin", "X-Requested-With", "Content-Type", "Accept"),
                headers.get("Access-Control-Allow-Headers"));
    }
}
