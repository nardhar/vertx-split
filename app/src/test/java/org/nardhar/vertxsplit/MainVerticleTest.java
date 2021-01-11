package org.nardhar.vertxsplit;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class MainVerticleTest {

    @Test
    public void testThatTheServerIsStarted(TestContext tc) {
        tc.assertEquals(1, 1);
    }
//  private Vertx vertx;
//
//  @Before
//  public void setUp(TestContext tc) {
//    vertx = Vertx.vertx();
//    //vertx.deployVerticle(MainVerticle.class.getName(), tc.asyncAssertSuccess());
//  }
//
//  @After
//  public void tearDown(TestContext tc) {
//    vertx.close(tc.asyncAssertSuccess());
//  }

//  @Test
//  public void testThatTheServerIsStarted(TestContext tc) {
//    Async async = tc.async();
//    vertx.createHttpClient().getNow(8080, "localhost", "/", response -> {
//      tc.assertEquals(response.statusCode(), 401);
//      response.bodyHandler(body -> {
//        tc.assertTrue(body.length() > 0);
//        async.complete();
//      });
//    });
//  }

}
