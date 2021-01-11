package org.nardhar.vertxsplit.domain.wrapper;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.domain.BookDomain;
import org.nardhar.vertxsplit.domain.BookDomainVerticle;
import org.nardhar.vertxsplit.model.Book;
import org.nardhar.vertxsplit.vertx.eventbus.BusSender;

import java.util.List;

public class BookDomainVerticleWrapper implements BookDomain, BusSender {

    private EventBus eventBus;

    public BookDomainVerticleWrapper(Vertx vertx) {
        eventBus = vertx.eventBus();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Future<List<Book>> list(JsonObject params) {
        return busGetArray(BookDomainVerticle.LIST, params)
            .compose(jsonToFutureClassList(Book.class));
    }

    public Future<Book> get(JsonObject params) {
        return busGetObject(BookDomainVerticle.GET, params)
            .compose(jsonToFutureClass(Book.class));
    }

    public Future<Book> save(JsonObject params) {
        return busGetObject(BookDomainVerticle.SAVE, params)
            .compose(jsonToFutureClass(Book.class));
    }

    public Future<Book> update(JsonObject params) {
        return busGetObject(BookDomainVerticle.UPDATE, params)
            .compose(jsonToFutureClass(Book.class));
    }

    public Future<Book> delete(JsonObject params) {
        return busGetObject(BookDomainVerticle.DELETE, params)
            .compose(jsonToFutureClass(Book.class));
    }

}
