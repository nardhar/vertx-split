package org.nardhar.vertxsplit.vertx.repository;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface Repository {

    Future<CompositeFuture> getModelsLoaded();

    Future<JsonObject> save(String model, JsonObject params);

    Future<JsonObject> insert(String model, JsonObject params);

    Future<JsonObject> update(String model, JsonObject data);

    Future<JsonObject> update(String model, JsonObject data, Boolean upsert);

    Future<JsonObject> update(String model, JsonObject data, String writeConcern);

    Future<JsonObject> update(String model, JsonObject data, Boolean upsert, String writeConcern);

    Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query);

    Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query, Boolean upsert);

    Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query, String writeConcern);

    Future<JsonObject> updateMulti(String model, JsonObject data, JsonObject query, Boolean upsert, String writeConcern);

    Future<JsonObject> replace(String model, JsonObject data, JsonObject query);

    Future<JsonArray> findAll(String model, JsonObject params);

    Future<JsonObject> findOne(String model, JsonObject params);

    Future<JsonObject> delete(String model, JsonObject data);

    Future<JsonObject> deleteAll(String model, JsonObject query);

    Future<JsonObject> count(String model, JsonObject query);

}
