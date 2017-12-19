//package com.github.whileloop.rest4j;
//
//import com.sun.net.httpserver.HttpExchange;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//
//import static com.github.whileloop.rest4j.HttpMethod.GET;
//import static org.junit.Assert.assertEquals;
//
//public class MiddlewareTest {
//
//
//    @Test
//    public void testOrder() throws IOException, URISyntaxException {
//        Router r = new Router();
//        r.use(
//                new TestMiddleware("A"),
//                new TestMiddleware("B"),
//                new TestMiddleware("C")
//        );
//        r.handle("/", GET, (ex) -> {
//            ex.setAttribute("before", getOrEmpty(ex.getAttribute("before")) + "D");
//            ex.setAttribute("after", getOrEmpty(ex.getAttribute("after")) + "D");
//            return HttpResponse.n(Http.STATUS_OK, "got it");
//        });
//
//        MockHttpExchange mocked = new MockHttpExchange("GET", "http://localhost/");
//        r.handle(mocked);
//        assertEquals(Http.STATUS_OK, mocked.statusCode);
//        assertEquals("got it".length(), mocked.contentLength);
//        assertEquals("got it", mocked.getBody());
//        assertEquals("ABCD", mocked.getAttribute("before"));
//        assertEquals("DCBA", mocked.getAttribute("after"));
//
//        mocked = new MockHttpExchange("GET", "http://localhost/");
//        r.handle(mocked);
//        assertEquals(Http.STATUS_OK, mocked.statusCode);
//        assertEquals("got it".length(), mocked.contentLength);
//        assertEquals("got it", mocked.getBody());
//        assertEquals("ABCD", mocked.getAttribute("before"));
//        assertEquals("DCBA", mocked.getAttribute("after"));
//    }
//
//    private class TestMiddleware implements Middleware {
//
//        private String name;
//
//        public TestMiddleware(String name) {
//            this.name = name;
//        }
//
//        @Override
//        public HttpResponse handle(Chain next, HttpExchange ex) throws IOException {
//            ex.setAttribute("before", getOrEmpty(ex.getAttribute("before")) + name);
//            HttpResponse resp = next.call(ex);
//            ex.setAttribute("after", getOrEmpty(ex.getAttribute("after")) + name);
//            return resp;
//        }
//    }
//}