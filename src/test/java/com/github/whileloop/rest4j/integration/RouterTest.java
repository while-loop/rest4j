package com.github.whileloop.rest4j.integration;

import com.github.whileloop.rest4j.HttpStatus;
import com.github.whileloop.rest4j.Router;
import com.github.whileloop.rest4j.handler.FileHandler;
import com.github.whileloop.rest4j.middleware.JsonMiddleware;
import com.github.whileloop.rest4j.middleware.LoggerMiddleware;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Created by aalves on 3/20/18
 */
public abstract class RouterTest {
    public static Gson GSON = new Gson();

    private static String base;
    private static FileHandler fh;
    private static Router apiRouter;

    /**
     * before test
     *
     * @param apiRouter   /api router
     * @param fileHandler /* static file handler
     * @return address which to access the http server. http(s)://host:port
     */
    public abstract String before(Router apiRouter, FileHandler fileHandler) throws Exception;

    /**
     * after test
     */
    public abstract void after();

    @Before
    public void beforeTest() throws Exception {
        Unirest.setTimeouts(1000, 1000);
        UsersService us = new UsersService(new UsersService.Datastore());
        JobsService js = new JobsService();

        String dir = RouterTest.class.getClassLoader().getResource("www").toExternalForm();
        fh = new FileHandler(dir);

        apiRouter = new Router("/api/v1");
        apiRouter.use(new LoggerMiddleware(), new JsonMiddleware());
        apiRouter.handle("/jobs", js.getRoutes());
        apiRouter.handle("/users", us.getRoutes());

        base = before(apiRouter, fh).replaceAll("/*$", "");
        Thread.sleep(250);
    }

    @After
    public void afterTest() {
        fh.close();
        after();
    }

    @Test
    public void testJobsCRUD() throws Exception {
        HttpResponse<JsonNode> resp = Unirest.get(base + "/api/v1/jobs").asJson();
        assertEquals(200, resp.getStatus());

        assertEquals("application/json", getHeader(resp, "Content-Type"));
        assertEquals("[]", resp.getBody().toString());

        JsonObject job = new JsonObject();
        job.addProperty("name", "job1");
        job.addProperty("days", 90);
        resp = Unirest
                .post(base + "/api/v1/jobs")
                .header("Content-Type", "application/json")
                .body(job.toString())
                .asJson();

        assertTrue(resp.getBody().toString(), resp.getBody().getObject().has("uuid"));

        job.addProperty("uuid", resp.getBody().getObject().getString("uuid"));
        job.addProperty("name", "newname");
        resp = Unirest
                .put(base + "/api/v1/jobs/" + resp.getBody().getObject().getString("uuid"))
                .header("Content-Type", "application/json")
                .body(job.toString())
                .asJson();

        assertEquals("newname", resp.getBody().getObject().get("name"));

        resp = Unirest.get(base + "/api/v1/jobs").asJson();
        assertEquals(200, resp.getStatus());
        assertEquals("application/json", getHeader(resp, "Content-Type"));

        JSONArray arr = resp.getBody().getArray();
        assertEquals(1, arr.length());
        assertEquals(job.get("name").getAsString(), arr.getJSONObject(0).getString("name"));

        HttpResponse resp2 = Unirest
                .delete(base + "/api/v1/jobs/" + job.get("uuid").getAsString()).asString();

        assertEquals(HttpStatus.NO_CONTENT.code(), resp2.getStatus());

        afterTest();
        beforeTest();
        resp = Unirest.post(base + "/api/v1/jobs/123").asJson();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.code(), resp.getStatus());

        resp = Unirest.get(base + "/api/v1/jobs/123").asJson();
        assertEquals(HttpStatus.NOT_FOUND.code(), resp.getStatus());

        resp = Unirest.put(base + "/api/v1/jo1bs/").asJson();
        assertEquals(HttpStatus.NOT_FOUND.code(), resp.getStatus());
    }

    @Test
    public void testFileHandler() throws UnirestException {
        HttpResponse<String> resp = Unirest.post(base + "/index.html").asString();
        assertEquals(200, resp.getStatus());
        assertEquals("text/html", getHeader(resp, "Content-Type"));
        assertThat(resp.getBody(), containsString("<title>rest4j</title>"));

        resp = Unirest.post(base + "/stylesheets/bootstrap.min.css").asString();
        assertEquals(200, resp.getStatus());
        assertEquals("21", getHeader(resp, "Content-Length"));
        assertEquals("text/css", getHeader(resp, "Content-Type"));
        assertEquals("bootstrap.min.css.map", resp.getBody());

        resp = Unirest.post(base + "/nowhat.html").asString();
        assertEquals(404, resp.getStatus());
    }

    @Test
    public void testUsersService() throws UnirestException {
        JsonObject user = new JsonObject();
        user.addProperty("name", "anthony");
        user.addProperty("age", 90);
        HttpResponse<JsonNode> resp = Unirest
                .post(base + "/api/v1/users")
                .header("Content-Type", "application/json")
                .body(user.toString())
                .asJson();

        assertTrue(resp.getBody().toString(), resp.getBody().getObject().has("name"));

        resp = Unirest
                .get(base + "/api/v1/users")
                .asJson();
        assertEquals(1, resp.getBody().getArray().length());
    }


    private String getHeader(HttpResponse resp, String field) {
        String type = resp.getHeaders().getFirst(field);
        if (type == null) {
            type = resp.getHeaders().getFirst(normalize(field));
        }

        return type;
    }

    private String normalize(String var1) {
        if (var1 == null) {
            return null;
        } else {
            int var2 = var1.length();
            if (var2 == 0) {
                return var1;
            } else {
                char[] var3 = var1.toCharArray();
                if (var3[0] >= 'a' && var3[0] <= 'z') {
                    var3[0] = (char) (var3[0] - 32);
                }

                for (int var4 = 1; var4 < var2; ++var4) {
                    if (var3[var4] >= 'A' && var3[var4] <= 'Z') {
                        var3[var4] = (char) (var3[var4] + 32);
                    }
                }

                return new String(var3);
            }
        }
    }
}
