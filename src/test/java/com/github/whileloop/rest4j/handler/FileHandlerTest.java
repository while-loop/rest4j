package com.github.whileloop.rest4j.handler;

import com.github.whileloop.rest4j.HttpRequest;
import com.github.whileloop.rest4j.test.ResponseRecorder;
import org.junit.Test;

import static com.github.whileloop.rest4j.HttpMethod.GET;
import static com.github.whileloop.rest4j.HttpStatus.NOT_FOUND;
import static com.github.whileloop.rest4j.HttpStatus.OK;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class FileHandlerTest {

    @Test
    public void testResourceDirNonJar() throws Exception {
        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
        FileHandler fh = new FileHandler(dir);

        HttpRequest r = new HttpRequest(GET, "http://localhost/index.html");
        ResponseRecorder rec = new ResponseRecorder(null);
        fh.handle(r, rec);

        assertEquals(OK, rec.getStatus());
        String body = rec.getBody();
        assertEquals("text/html", rec.headers.getFirst("Content-Type"));
        assertThat(body, containsString("<title>rest4j</title>"));
        fh.close();
    }

    @Test
    public void testResourceWithStrip() throws Exception {
        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
        FileHandler fh = new FileHandler(dir, "/new");
        HttpRequest r = new HttpRequest(GET, "http://localhost/new/index.html");
        ResponseRecorder rec = new ResponseRecorder(null);
        fh.handle(r, rec);

        assertEquals(OK, rec.getStatus());
        String body = rec.getBody();
        assertEquals("text/html", rec.headers.getFirst("Content-Type"));
        assertThat(body, containsString("<title>rest4j</title>"));
    }

    @Test
    public void testIndexHtmlRootPath() throws Exception {
        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
        FileHandler fh = new FileHandler(dir);
        HttpRequest r = new HttpRequest(GET, "http://localhost/");
        ResponseRecorder rec = new ResponseRecorder();
        fh.handle(r, rec);

        assertEquals(OK, rec.getStatus());
        String body = rec.getBody();
        assertEquals("text/html", rec.headers.getFirst("Content-Type"));
        assertThat(body, containsString("<title>rest4j</title>"));
    }

    @Test
    public void testNotFound() throws Exception {
        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
        FileHandler fh = new FileHandler(dir);
        HttpRequest r = new HttpRequest(GET, "http://localhost/fakedir/index.html");
        ResponseRecorder rec = new ResponseRecorder(null);
        fh.handle(r, rec);

        assertEquals(NOT_FOUND, rec.getStatus());
        String body = rec.getBody();
        assertEquals("not found: /fakedir/index.html", body);
    }

    @Test
    public void testNoSlashRoot() throws Exception {
        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
        FileHandler fh = new FileHandler(dir);
        HttpRequest r = new HttpRequest(GET, "http://localhost");
        ResponseRecorder rec = new ResponseRecorder(null);
        fh.handle(r, rec);

        assertEquals(OK, rec.getStatus());
        String body = rec.getBody();
        assertThat(body, containsString("<title>rest4j</title>"));
    }

    @Test
    public void testCss() throws Exception {
        String dir = getClass().getClassLoader().getResource("www").toExternalForm();
        FileHandler fh = new FileHandler(dir);
        HttpRequest r = new HttpRequest(GET, "http://localhost/stylesheets/bootstrap.min.css");
        ResponseRecorder rec = new ResponseRecorder(null);
        fh.handle(r, rec);

        assertEquals(OK, rec.getStatus());
        String body = rec.getBody();
        assertThat(body, containsString("bootstrap.min.css.map"));
        assertEquals("text/css", rec.headers.getFirst("Content-Type"));
    }
}