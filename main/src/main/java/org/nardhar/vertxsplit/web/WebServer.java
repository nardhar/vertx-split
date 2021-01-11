package org.nardhar.vertxsplit.web;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.nardhar.vertxsplit.vertx.web.RouteHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class WebServer {

    private Vertx vertx;
    private JsonObject config;
    private Router router;

    @SuppressWarnings("unchecked")
    @Inject
    public WebServer(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
        router = Router.router(vertx);

        // importing controllers manually
        // serviceController.init();

        // TODO: we could get all @RouteHandler annotated classes instead of passing a configuration variable

        // config.getJsonArray("handlers")
        //     .forEach((routeHandlerClass) -> {
        //         System.out.println((String)routeHandlerClass);
        //         try {
        //             RouteHandler routeHandler = ((Class<RouteHandler>) Class.forName((String)routeHandlerClass)).newInstance();
        //             routeHandler.setRouter(router);
        //             routeHandler.init();
        //         } catch (IllegalAccessException|InstantiationException|ClassNotFoundException ex) {
        //             // TODO: add a proper logger
        //             System.out.println("Could not deploy handler: " + routeHandlerClass);
        //         }
        //     });
    }

    public void start(List<RouteHandler> controllers, Future<Void> startFuture) {
        controllers.forEach(controller -> {
            controller.setRouter(router);
            controller.init();
        });
        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(config.getInteger("http.port"), (ar) -> {
                if (ar.succeeded()) {
                    startFuture.complete();
                } else {
                    startFuture.fail(ar.cause());
                }
            });
    }

}
