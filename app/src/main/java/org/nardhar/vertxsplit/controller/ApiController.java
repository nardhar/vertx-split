package org.nardhar.vertxsplit.controller;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import org.nardhar.vertxsplit.vertx.web.AbstractRouteHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
@Singleton
public class ApiController implements AbstractRouteHandler {

    private GitRepositoryState gitState;
    private Router router;
    private EventBus eventBus;

    @Inject
    public ApiController() {
        // loading git.properties
        final Properties properties = new Properties();

        // TODO: check if we should make an async loading of git.properties
        InputStream rs = getClass().getResourceAsStream("/git.properties");

        if (rs != null) {
            try {
                properties.load(rs);
                rs.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        gitState = new GitRepositoryState(properties);
    }

    public void init() {
        get("/status", this::status);
    }

    private Future<JsonObject> status(RoutingContext ctx) {
        JsonObject status = new JsonObject()
            .put("os.name", System.getProperty("os.name"))
            .put("os.arch", System.getProperty("os.arch"))
            .put("os.version", System.getProperty("os.version"))
            .put("java.version", System.getProperty("java.version"))
            .put("java.vm.version", System.getProperty("java.vm.version"))
            .put("java.vm.name", System.getProperty("java.vm.name"))
            .put("java.compiler", System.getProperty("java.compiler"))
            .put("git.status", new JsonObject()
                .put("branch", gitState.getBranch())
                .put("commit", gitState.getCommitIdAbbrev())
                .put("user", gitState.getBuildUserName())
                .put("time", gitState.getCommitTime())
                .put("message", gitState.getCommitMessageShort())
            );

        // TODO: load elasticsearch and rabbitmq status too
        return Future.succeededFuture(status);
    }

}
