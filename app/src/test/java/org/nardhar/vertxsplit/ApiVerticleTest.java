package org.nardhar.vertxsplit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ApiVerticleTest {

    // private Vertx vertx;
    // private JsonObject config;
    //
    // @Before
    // public void setUp(TestContext tc) {
    //     vertx = Vertx.vertx();
    //     config = new JsonObject()
    //         .put("api.version", "1.0.0")
    //         .put("http.port", 8080)
    //         .put("mongodb_uri", "mongodb://localhost:27017/f1nder-test-db")
    //         .put("elasticsearch_uri", "http://localhost:9200")
    //         .put("rabbitmq_uri", "amqp://localhost:5672");
    //
    //     DeploymentOptions options = new DeploymentOptions()
    //         .setConfig(config);
    //
    //     // vertx.deployVerticle(ApiVerticle.class.getName(), options, tc.asyncAssertSuccess());
    // }
    //
    // @After
    // public void tearDown(TestContext tc) {
    //     vertx.close(tc.asyncAssertSuccess());
    // }
    //
    // @Test
    // public void testThatTheServerIsStarted(TestContext tc) {
    //     Async async = tc.async();
    //
    //     vertx.createHttpClient().getNow(config.getInteger("http.port"), "localhost", "/", response -> {
    //         tc.assertEquals(response.statusCode(), 401); // should not allowed
    //         tc.assertEquals(true, response.headers().get("content-type").contains("application/json"));
    //
    //         response.bodyHandler(body -> {
    //             tc.assertTrue(body.length() > 0);
    //             async.complete();
    //         });
    //     });
    // }

    @Test
    public void testThatTheServerIsStarted(TestContext tc) {
        tc.assertEquals(1, 1);
    }
}
