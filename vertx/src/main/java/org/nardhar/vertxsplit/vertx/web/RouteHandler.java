package org.nardhar.vertxsplit.vertx.web;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.function.Function;

public interface RouteHandler {

    Router getRouter();

    void init();

    void setRouter(Router router);

    default <T> void action(HttpMethod method, String path, Function<RoutingContext, Future<T>> caller) {
        getRouter()
            .route(method, path)
            .handler((ctx) -> caller.apply(ctx).setHandler((result) -> {
                if (!ctx.response().ended()) {
                    if (result.succeeded()) {
                        ctx.response()
                            .setStatusCode(ctx.request().method().equals(HttpMethod.POST) ? 201 : 200)
                            .end(Json.encode(result.result()));
                    } else {
                        // result.cause().printStackTrace();
                        ctx.response()
                            .setStatusCode(ctx.request().method().equals(HttpMethod.GET) ? 404 : 400)
                            .end(result.cause().getMessage());
                    }
                }
            }));
    }

    // shortcuts
    default void list(String path, Function<RoutingContext, Future<JsonArray>> caller) {
        action(HttpMethod.GET, path, caller);
    }

    default void get(String path, Function<RoutingContext, Future<JsonObject>> caller) {
        action(HttpMethod.GET, path, caller);
    }

    default void post(String path, Function<RoutingContext, Future<JsonObject>> caller) {
        action(HttpMethod.POST, path, caller);
    }

    default void put(String path, Function<RoutingContext, Future<JsonObject>> caller) {
        action(HttpMethod.PUT, path, caller);
    }

    default void delete(String path, Function<RoutingContext, Future<JsonObject>> caller) {
        action(HttpMethod.DELETE, path, caller);
    }

    default Future<JsonObject> getJsonBody(RoutingContext ctx) {
        Future<JsonObject> future = Future.future();

        ctx.request().bodyHandler((body) -> future.complete(body.toJsonObject()));

        return future;
    }

}
