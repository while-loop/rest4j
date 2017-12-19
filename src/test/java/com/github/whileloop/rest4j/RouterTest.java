//package com.github.whileloop.rest4j;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//
//import static com.github.whileloop.rest4j.HttpMethod.*;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//public class RouterTest {
//
//    private Router r;
//
//    @Before
//    public void before() {
//        r = new Router();
//    }
//
//    @Test
//    public void match() throws Exception {
//        r.handle("/", HandlerFunc)
//        r.handle("/", GET, ex -> new HttpResponse(1));
//        r.handle("/", POST, ex -> new HttpResponse(2));
//        r.handle("/where", POST, ex -> new HttpResponse(3));
//        r.handle("/are/you", GET, ex -> new HttpResponse<>(4, "yoo"));
//
//        Router j = new Router();
//        j.handle("/", GET, ex -> new HttpResponse(5));
//        j.handle("/", POST, ex -> new HttpResponse(6));
//        j.handle("/:uuid", GET, ex -> new HttpResponse(7));
//        j.handle("/:uuid", PUT, ex -> new HttpResponse(8));
//        j.handle("/:uuid", DELETE, ex -> {
//            String uuid = (String) ex.getAttribute("uuid");
//            boolean found = false;
//            if (uuid != null && ex.getAttribute("uuid").equals("8654ac51")) {
//                found = true;
//            }
//            return new HttpResponse<>(10, found);
//        });
//        r.handle("/jobs", j);
//
//        MockHttpExchange mocked = new MockHttpExchange("GET", "http://localhost/are/you");
//        r.handle(mocked);
//        assertEquals(4, mocked.statusCode);
//        assertEquals(3, mocked.contentLength);
//        assertEquals("yoo", mocked.getBody());
//
//
//        mocked = new MockHttpExchange("DELETE", "http://localhost/jobs/8654ac51");
//        r.handle(mocked);
//        assertEquals(10, mocked.statusCode);
//        assertEquals(4, mocked.contentLength);
//        assertEquals("true", mocked.getBody());
//    }
//
//    @Test
//    public void testStrictSlashes() throws IOException, URISyntaxException {
//        r.handle("/", GET, ex -> new HttpResponse(1));
//        r.handle("/yellow", POST, ex -> new HttpResponse(2));
//
//        assertEquals(4, r.routes.size());
//
//        MockHttpExchange mocked = new MockHttpExchange("GET", "http://localhost/");
//        r.handle(mocked);
//        assertEquals(1, mocked.statusCode);
//        mocked = new MockHttpExchange("GET", "http://localhost");
//        r.handle(mocked);
//        assertEquals(1, mocked.statusCode);
//
//        mocked = new MockHttpExchange("POST", "http://localhost/yellow/");
//        r.handle(mocked);
//        assertEquals(2, mocked.statusCode);
//        mocked = new MockHttpExchange("POST", "http://localhost/yellow");
//        r.handle(mocked);
//        assertEquals(2, mocked.statusCode);
//
//    }
//}