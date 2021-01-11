package org.nardhar.vertxsplit.vertx.repository;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.vertx.eventbus.ConsumerVerticle;
import org.nardhar.vertxsplit.vertx.repository.handler.RepositoryHandler;

import java.util.List;

public class RepositoryVerticle extends AbstractVerticle implements ConsumerVerticle {

    private RepositoryHandler repositoryHandler;
    private List<Future> consumers;

    public void start(Future<Void> startFuture) {
        registerConsumers();
        completeRegistering(startFuture);
    }

    @Override
    public void registerConsumers() {
        repositoryHandler = new RepositoryHandler(vertx, config());

        // adding endpoints
        addConsumer("repository.save", this::save);
        addConsumer("repository.insert", this::insert);
        addConsumer("repository.update", this::update);
        addConsumer("repository.updateMulti", this::updateMulti);
        addConsumer("repository.replace", this::replace);
        addConsumer("repository.findAll", this::findAll);
        addConsumer("repository.findOne", this::findOne);
        addConsumer("repository.delete", this::delete);
        addConsumer("repository.deleteAll", this::deleteAll);
        addConsumer("repository.count", this::count);

        addDeployFuture(repositoryHandler.getModelsLoaded());
    }

    public List<Future> getConsumers() {
        return consumers;
    }

    public void save(Message<JsonObject> message) {
        String model = message.headers().get("model");

        repositoryHandler.save(model, message.body())
            .setHandler(busRespond(message));
    }

    public void insert(Message<JsonObject> message) {
        String model = message.headers().get("model");

        repositoryHandler.insert(model, message.body())
            .setHandler(busRespond(message));
    }

    public void update(Message<JsonObject> message) {
        String model = message.headers().get("model");

        JsonObject modelData = message.body().copy();

        repositoryHandler.update(model, modelData, message.headers().contains("upsert"), message.headers().get("writeConcern"))
            .setHandler(busRespond(message));
    }

    public void updateMulti(Message<JsonObject> message) {
        String model = message.headers().get("model");

        JsonObject modelData = message.body().getJsonObject("data").copy();

        // remove the id from the modelData and put it in the query
        JsonObject query = message.body().getJsonObject("query");

        // update with the current modelData, id is removed so only updates the other data
        repositoryHandler.updateMulti(
            model,
            modelData,
            query,
            message.headers().contains("upsert"),
            message.headers().get("writeConcern")
        ).setHandler(busRespond(message));
    }

    public void replace(Message<JsonObject> message) {
        String model = message.headers().get("model");

        JsonObject modelData = message.body().getJsonObject("data").copy();

        // remove the id from the modelData and put it in the query
        JsonObject query = message.body().getJsonObject("query");

        // update with the current modelData, id is removed so only updates the other data
        repositoryHandler.replace(model, modelData, query)
            .setHandler(busRespond(message));
    }

    public void findAll(Message<JsonObject> message) {
        String model = message.headers().get("model");
        JsonObject query = message.body() != null ? message.body() : new JsonObject();

        repositoryHandler.findAll(model, query)
            .setHandler(busRespond(message));
    }

    public void findOne(Message<JsonObject> message) {
        String model = message.headers().get("model");
        JsonObject query = message.body() != null ? message.body() : new JsonObject();

        repositoryHandler.findOne(model, query)
            .setHandler(busRespond(message));
    }

    public void delete(Message<JsonObject> message) {
        String model = message.headers().get("model");

        JsonObject modelData = message.body().copy();

        // remove the id from the modelData and put it in the query
        repositoryHandler.delete(model, modelData)
            .setHandler(busRespond(message));
    }

    public void deleteAll(Message<JsonObject> message) {
        String model = message.headers().get("model");

        JsonObject query = message.body().copy();

        repositoryHandler.deleteAll(model, query)
            .setHandler(busRespond(message));
    }

    public void count(Message<JsonObject> message) {
        String model = message.headers().get("model");

        JsonObject query = message.body().copy();

        repositoryHandler.count(model, query)
            .setHandler(busRespond(message));
    }

}
