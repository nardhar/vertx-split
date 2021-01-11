package org.nardhar.vertxsplit.vertx.repository.wrapper;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.vertx.eventbus.BusSender;
import org.nardhar.vertxsplit.vertx.repository.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class RepositoryVerticleWrapper implements Repository, BusSender {

    private EventBus eventBus;
    private Future<CompositeFuture> modelsLoaded;

    @Inject
    public RepositoryVerticleWrapper(Vertx vertx) {
        eventBus = vertx.eventBus();
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public Future<CompositeFuture> getModelsLoaded() {
        return Future.succeededFuture();
    }

    public Future<JsonObject> save(String model, JsonObject params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.save", headers, params);
    }

    public Future<JsonObject> insert(String model, JsonObject data) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.insert", headers, data);
    }

    public Future<JsonObject> update(String model, JsonObject data) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.update", headers, data);
    }

    public Future<JsonObject> update(String model, JsonObject data, Boolean upsert) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);
        headers.put("upsert", upsert.toString());

        return busGetObject("repository.update", headers, data);
    }

    public Future<JsonObject> update(String model, JsonObject data, String concern) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);
        headers.put("concern", concern);

        return busGetObject("repository.update", headers, data);
    }

    public Future<JsonObject> update(String model, JsonObject data, Boolean upsert, String concern) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);
        headers.put("upsert", upsert.toString());
        headers.put("concern", concern);

        return busGetObject("repository.update", headers, data);
    }

    public Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.updateMulti", headers, data);
    }

    public Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query, Boolean upsert) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);
        headers.put("upsert", upsert.toString());

        return busGetObject("repository.updateMulti", headers, data);
    }

    public Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query, String concern) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);
        headers.put("concern", concern);

        return busGetObject("repository.updateMulti", headers, data);
    }

    public Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query, Boolean upsert, String concern) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);
        headers.put("upsert", upsert.toString());
        headers.put("concern", concern);

        return busGetObject("repository.updateMulti", headers, data);
    }

    public Future<JsonObject> replace(String model, JsonObject data, JsonObject query) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.replace", headers, new JsonObject()
            .put("data", data)
            .put("query", query)
        );
    }

    public Future<JsonArray> findAll(String model, JsonObject params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetArray("repository.findAll", headers, params);
    }

    public Future<JsonObject> findOne(String model, JsonObject params) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.findOne", headers, params);
    }

    public Future<JsonObject> delete(String model, JsonObject data) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.delete", headers, data);
    }

    public Future<JsonObject> deleteAll(String model, JsonObject query) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.deleteAll", headers, query);
    }

    public Future<JsonObject> count(String model, JsonObject query) {
        Map<String, String> headers = new HashMap<>();
        headers.put("model", model);

        return busGetObject("repository.count", headers, query);
    }

}
