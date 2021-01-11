package org.nardhar.vertxsplit.util;

import io.vertx.core.*;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.spi.cluster.ClusterManager;

/**
 * VerticleDeployHelper
 *
 * Customized version of
 * https://github.com/hakdogan/IntroduceToEclicpseVert.x/blob/master/Helper/src/main/java/com/kodcu/helper/VerticleDeployHelper.java
 */
public class VerticleDeployHelper {
    private VerticleDeployHelper() {}

    /**
     *
     * @param vertx vertx instance
     * @param name verticle name for deployment
     * @param config the configuration {@code JsonObject} object
     * @return {@code Future<Void>}
     */
    public static Future<Void> deploy(Vertx vertx, String name, JsonObject config, Logger log){
        final Future<Void> future = Future.future();

        DeploymentOptions options = new DeploymentOptions()
            .setConfig(config);

        vertx.deployVerticle(name, options, res -> {
            if(res.failed()){
                log.error("Failed to deploy verticle " + name);
                future.fail(res.cause());
            } else {
                log.info("Deployed verticle " + name);
                future.complete();
            }
        });

        return future;
    }

    /**
     *
     * @param manager
     * @param className
     * @return
     */
    public static Future<Void> deploy(ClusterManager manager, String className, Logger log){

        final Future<Void> future = Future.future();
        final ClusterManager mgr = manager;
        final VertxOptions options = new VertxOptions().setClusterManager(mgr);

        Vertx.clusteredVertx(options, cluster -> {
            if (cluster.succeeded()) {
                try {
                    cluster.result().deployVerticle((Verticle) Class.forName(className).newInstance(), res -> {
                        if(res.succeeded()){
                            log.info("Deployment id is: " + res.result());
                            future.complete();
                        } else {
                            log.error("Deployment failed!");
                            future.fail(res.cause());
                        }
                    });
                } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                    log.error("Verticle deploy failed {} ", e);
                }
            } else {
                log.error("Cluster up failed: " + cluster.cause());
                future.fail(cluster.cause());
            }
        });

        return future;
    }
}

