package org.nardhar.vertxsplit.controller;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.Data;
import org.nardhar.vertxsplit.domain.BookDomain;
import org.nardhar.vertxsplit.model.Book;
import org.nardhar.vertxsplit.vertx.web.AbstractRouteHandler;

import javax.inject.Inject;
import javax.inject.Singleton;

@Data
@Singleton
public class BookController implements AbstractRouteHandler {

    private Router router;
    private BookDomain bookDomain;

    @Inject
    public BookController(BookDomain bookDomain) {
        this.bookDomain = bookDomain;
    }

    public void init() {
        list("/services", this::list);
        get("/services/:id", this::get);
        post("/services", this::post);
        put("/services/:id", this::put);
        delete("/services/:id", this::delete);
    }

    public Future<JsonArray> list(RoutingContext ctx) {
        return bookDomain.list(null)
            .compose(listToFutureJsonArray(BookController::serviceToJson));
    }

    public Future<JsonObject> get(RoutingContext ctx) {
        return bookDomain.get(new JsonObject()
            .put("id", ctx.request().getParam("id"))
        ).compose(toFutureJsonObject(BookController::serviceToJson));
    }

    public Future<JsonObject> post(RoutingContext ctx) {
        return getJsonBody(ctx)
            .compose((body) -> bookDomain.save(serviceReader(body)))
            .compose(toFutureJsonObject(BookController::serviceToJson));
    }

    public Future<JsonObject> put(RoutingContext ctx) {
        return getJsonBody(ctx)
            .compose((body) -> bookDomain.update(serviceReader(body)
                .put("id", ctx.request().getParam("id"))
            ))
            .compose(toFutureJsonObject(BookController::serviceToJson));
    }

    public Future<JsonObject> delete(RoutingContext ctx) {
        return bookDomain.delete(new JsonObject()
            .put("id", ctx.request().getParam("id"))
        )
        .compose(toFutureJsonObject(BookController::serviceToJson));
    }

    /**
     * Converter from domain verticle result to a json handler result, it is static for reuse among handlers
     * @param object The object to convert
     * @return The converted object
     */
    public static JsonObject serviceToJson(Book object) {
        return JsonObject.mapFrom(object);
        // return new JsonObject()
        //     .put("id", object.getString("id"))
        //     .put("name", object.getString("name"))
        //     .put("description", object.getString("description"))
        //     .put("type", object.getInteger("type"));
    }

    public static JsonObject serviceReader(JsonObject object) {
        return new JsonObject()
            .put("name", object.getString("name"))
            .put("description", object.getString("description"))
            .put("type", object.getInteger("type"));
    }

}
