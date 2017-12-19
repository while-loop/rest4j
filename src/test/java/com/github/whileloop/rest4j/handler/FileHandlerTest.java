//package com.github.whileloop.rest4j.handler;
//
//import com.github.whileloop.rest4j.MockHttpExchange;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThat;
//
//public class FileHandlerTest {
//
//    @Test
//    public void testResourceDirNonJar() throws IOException, URISyntaxException {
//        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
//        FileHandler fh = new FileHandler(dir);
//        MockHttpExchange ex = new MockHttpExchange("GET", "http://localhost/index.html");
//        fh.handle(ex);
//
//        assertEquals(200, ex.statusCode);
//        String body = ex.getBody();
//        assertEquals("text/html", ex.getResponseHeaders().getFirst("Content-Type"));
//        assertThat(body, containsString("<title>Metasponse Trace Builder</title>"));
//    }
//
//    @Test
//    public void testResourceWithStrip() throws IOException, URISyntaxException {
//        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
//        FileHandler fh = new FileHandler(dir, "/new");
//        MockHttpExchange ex = new MockHttpExchange("GET", "http://localhost/new/index.html");
//        fh.handle(ex);
//
//        assertEquals(200, ex.statusCode);
//        String body = ex.getBody();
//        assertEquals("text/html", ex.getResponseHeaders().getFirst("Content-Type"));
//        assertThat(body, containsString("<title>Metasponse Trace Builder</title>"));
//    }
//
//    @Test
//    public void testIndexHtml() throws IOException, URISyntaxException {
//        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
//        FileHandler fh = new FileHandler(dir);
//        MockHttpExchange ex = new MockHttpExchange("GET", "http://localhost/");
//        fh.handle(ex);
//
//        assertEquals(200, ex.statusCode);
//        String body = ex.getBody();
//        assertThat(body, containsString("<title>Metasponse Trace Builder</title>"));
//        assertEquals("text/html", ex.getResponseHeaders().getFirst("Content-Type"));
//    }
//
//    @Test
//    public void testCss() throws IOException, URISyntaxException {
//        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
//        FileHandler fh = new FileHandler(dir);
//        MockHttpExchange ex = new MockHttpExchange("GET", "http://localhost/stylesheets/bootstrap.min.css");
//        fh.handle(ex);
//
//        assertEquals(200, ex.statusCode);
//        String body = ex.getBody();
//        assertThat(body, containsString("bootstrap.min.css.map"));
//        assertEquals("text/css", ex.getResponseHeaders().getFirst("Content-Type"));
//    }
//}