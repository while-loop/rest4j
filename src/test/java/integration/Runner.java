package integration;

import com.github.whileloop.rest4j.HttpStatus;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

/**
 * Created by aalves on 3/20/18
 */
public class Runner {
    private String base;

    public Runner(String base) throws UnirestException {
        Unirest.setTimeouts(250, 250000);
        this.base = base;

        testJobsCRUD();
        testFileHandler();
        testUsersService();
    }

    public static void test(String base) throws UnirestException {
        new Runner(base);
    }

    public void testJobsCRUD() throws UnirestException {
        HttpResponse<JsonNode> resp = Unirest.get(base + "/api/v1/jobs").asJson();
        assertEquals(200, resp.getStatus());
        assertEquals("application/json", resp.getHeaders().getFirst("Content-type"));
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
        assertEquals("application/json", resp.getHeaders().getFirst("Content-type"));

        JSONArray arr = resp.getBody().getArray();
        assertEquals(1, arr.length());
        assertEquals(job.get("name").getAsString(), arr.getJSONObject(0).getString("name"));

        HttpResponse resp2 = Unirest
                .delete(base + "/api/v1/jobs/" + job.get("uuid").getAsString()).asString();

        assertEquals(HttpStatus.NO_CONTENT.code(), resp2.getStatus());

        resp = Unirest.post(base + "/api/v1/jobs/123").asJson();
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED.code(), resp.getStatus());

        resp = Unirest.get(base + "/api/v1/jobs/123").asJson();
        assertEquals(HttpStatus.NOT_FOUND.code(), resp.getStatus());

        resp = Unirest.put(base + "/api/v1/jo1bs/").asJson();
        assertEquals(HttpStatus.NOT_FOUND.code(), resp.getStatus());
    }

    public void testFileHandler() throws UnirestException {
        HttpResponse<String> resp = Unirest.post(base + "/index.html").asString();
        assertEquals(200, resp.getStatus());
        assertEquals("text/html", resp.getHeaders().getFirst("Content-type"));
        assertThat(resp.getBody(), containsString("<title>rest4j</title>"));

        resp = Unirest.post(base + "/stylesheets/bootstrap.min.css").asString();
        assertEquals(200, resp.getStatus());
        assertEquals("21", resp.getHeaders().getFirst("Content-length"));
        assertEquals("text/css", resp.getHeaders().getFirst("Content-type"));
        assertEquals("bootstrap.min.css.map", resp.getBody());

        resp = Unirest.post(base + "/nowhat.html").asString();
        assertEquals(404, resp.getStatus());
    }

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
}
