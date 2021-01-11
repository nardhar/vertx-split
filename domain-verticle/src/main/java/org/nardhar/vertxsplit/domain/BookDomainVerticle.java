package org.nardhar.vertxsplit.domain;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.vertx.eventbus.ConsumerVerticle;
import org.nardhar.vertxsplit.vertx.repository.RepositorySender;

import javax.inject.Inject;
import java.util.List;

public class BookDomainVerticle extends AbstractVerticle implements ConsumerVerticle, RepositorySender {

    public static final String LIST = "domain.service.list";
    public static final String GET = "domain.service.get";
    public static final String SAVE = "domain.service.save";
    public static final String UPDATE = "domain.service.update";
    public static final String DELETE = "domain.service.delete";

    private List<Future> consumers;

    @Inject
    public BookDomainHandler bookDomainHandler;

    public List<Future> getConsumers() {
        return consumers;
    }

    public EventBus getEventBus() {
        return vertx.eventBus();
    }

    public void start(Future<Void> startFuture) {
        // calling to dummy method
        registerConsumers();

        // actual tracking of consumer registering
        completeRegistering(startFuture);
    }

    @Override
    public void registerConsumers() {
        // serviceDomainHandler = new ServiceDomainHandler();

        addConsumer(LIST, this::list);
        addConsumer(GET, this::get);
        addConsumer(SAVE, this::save);
        addConsumer(UPDATE, this::update);
        addConsumer(DELETE, this::delete);
    }

    public void list(Message<JsonObject> message) {
        bookDomainHandler.list(message.body())
            .setHandler(busRespondModelList(message));
    }

    public void get(Message<JsonObject> message) {
        bookDomainHandler.get(message.body())
            .setHandler(busRespondModel(message));
    }

    public void save(Message<JsonObject> message) {
        bookDomainHandler.save(message.body())
            .setHandler(busRespondModel(message));
    }

    public void update(Message<JsonObject> message) {
        bookDomainHandler.update(message.body())
            .setHandler(busRespondModel(message));
    }

    public void delete(Message<JsonObject> message) {
        bookDomainHandler.delete(message.body())
            .setHandler(busRespondModel(message));
    }

}
