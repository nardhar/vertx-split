package org.nardhar.vertxsplit.vertx.repository.handler;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
import io.vertx.ext.mongo.WriteOption;
import org.nardhar.vertxsplit.vertx.exception.ApplicationException;
import org.nardhar.vertxsplit.vertx.repository.Model;
import org.nardhar.vertxsplit.vertx.repository.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class RepositoryHandler implements Repository {

    private MongoClient mongoClient;

    private Map<String, Class<? extends Model>> modelClass;
    private Map<String, String> modelCollection;

    private Future<CompositeFuture> modelsLoaded;

    @SuppressWarnings("unchecked")
    @Inject
    public RepositoryHandler(Vertx vertx, JsonObject config) {
        modelsLoaded = Future.future();

        mongoClient = MongoClient.createShared(vertx, new JsonObject()
            .put("connection_string", config.getString("mongoConnectionString"))
        );

        List<Class<? extends Model>> clazzModels = new ArrayList<>();

        // TODO: we could get all @Model annotated classes instead of passing a configuration variable
        config.getJsonArray("models")
            .forEach((clazz) -> {
                try {
                    clazzModels.add((Class<Model>)Class.forName((String)clazz));
                } catch (ClassNotFoundException ex) {
                    System.out.println("Could not find model " + clazz);
                }
            });

        // registering model classes
        modelClass = new HashMap<>();

        clazzModels.forEach((clazz) -> modelClass.put(clazz.getName(), clazz));

        // registering model collection names for mongodb
        modelCollection = new HashMap<>();

        CompositeFuture.all(clazzModels.stream().map((clazz) -> {
            Future<Void> future = Future.future();
            try {
                Field field = clazz.getDeclaredField("collection");
                field.setAccessible(true);
                modelCollection.put(clazz.getName(), (String)field.get(null));
                future.complete();
            } catch (NoSuchFieldException ex) {
                future.fail("No collection property for model " + clazz.getName());
            } catch (IllegalAccessException ex) {
                future.fail("Collection property not readable for model " + clazz.getName());
            }
            return future;
        }).collect(Collectors.toList()))
        .setHandler(modelsLoaded.completer());
    }

    public Future<CompositeFuture> getModelsLoaded() {
        return modelsLoaded;
    }

    public Future<JsonObject> save(String model, JsonObject params) {
        Future<JsonObject> future = Future.future();

        mongoClient.save(modelCollection.get(model), params, res -> {
            if (res.succeeded()) {
                // returning an object copy with its id modified and removing the _id from mongo
                JsonObject record = params.copy();
                record.put("id", res.result())
                    .remove("_id");

                future.complete(record);
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repository.save.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonObject> insert(String model, JsonObject params) {
        Future<JsonObject> future = Future.future();

        mongoClient.insert(modelCollection.get(model), params, res -> {
            if (res.succeeded()) {
                JsonObject record = params.copy();
                record.put("id", res.result())
                    .remove("_id");

                future.complete(record);
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repositoryHandler.save.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonObject> update(String model, JsonObject modelData) {
        return update(model, modelData, false, null);
    }

    public Future<JsonObject> update(String model, JsonObject modelData, Boolean upsert) {
        return update(model, modelData, upsert, null);
    }

    public Future<JsonObject> update(String model, JsonObject modelData, String writeConcern) {
        return update(model, modelData, false, writeConcern);
    }

    public Future<JsonObject> update(String model, JsonObject modelData, Boolean upsert, String writeConcern) {
        Future<JsonObject> future = Future.future();

        // remove the id from the modelData and put it in the query
        JsonObject query = new JsonObject()
            .put("_id", modelData.remove("id"));
        // update with the current modelData, id is removed so only updates the other data
        JsonObject update = new JsonObject().put("$set", modelData);

        if (upsert || (writeConcern != null && !writeConcern.isEmpty())) {
            UpdateOptions options = new UpdateOptions();
            if (upsert) {
                options.setUpsert(true);
            }
            if (writeConcern != null && !writeConcern.isEmpty()) {
                options.setWriteOption(WriteOption.valueOf(writeConcern.toUpperCase()));
            }
            mongoClient.updateCollectionWithOptions(modelCollection.get(model), query, update, options, res -> {
                if (res.succeeded()) {
                    future.complete(modelData.put("id", query.getString("_id")));
                } else {
                    future.fail(new ApplicationException(
                        res.cause(),
                        "repositoryHandler.update.error"
                    ));
                }
            });
        } else {
            mongoClient.updateCollection(modelCollection.get(model), query, update, res -> {
                if (res.succeeded()) {
                    future.complete(modelData.put("id", query.getString("_id")));
                } else {
                    future.fail(new ApplicationException(
                        res.cause(),
                        "repositoryHandler.update.error"
                    ));
                }
            });
        }

        return future;
    }

    public Future<JsonObject> updateMulti(String model, JsonObject modelData, JsonObject query) {
        return updateMulti(model, modelData, query, false, null);
    }

    public Future<JsonObject> updateMulti(String model, JsonObject modelData, JsonObject query, Boolean upsert) {
        return updateMulti(model, modelData, query, upsert, null);
    }

    public Future<JsonObject> updateMulti(String model, JsonObject modelData, JsonObject query, String writeConcern) {
        return updateMulti(model, modelData, query, false, writeConcern);
    }

    public Future<JsonObject> updateMulti(String model, JsonObject modelData, JsonObject query, Boolean upsert, String writeConcern) {
        Future<JsonObject> future = Future.future();

        // update with the current modelData, id is removed so only updates the other data
        JsonObject update = new JsonObject().put("$set", modelData);

        UpdateOptions options = new UpdateOptions();
        if (upsert) {
            options.setUpsert(true);
        }
        if (writeConcern != null && !writeConcern.isEmpty()) {
            options.setWriteOption(WriteOption.valueOf(writeConcern.toUpperCase()));
        }
        mongoClient.updateCollectionWithOptions(modelCollection.get(model), query, update, options, res -> {
            if (res.succeeded()) {
                future.complete(new JsonObject().put("success", true));
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repositoryHandler.update.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonObject> replace(String model, JsonObject modelData, JsonObject query) {
        Future<JsonObject> future = Future.future();

        JsonObject replace = new JsonObject().put("$set", modelData);

        mongoClient.replaceDocuments(modelCollection.get(model), query, replace, res -> {
            if (res.succeeded()) {
                future.complete(new JsonObject().put("success", true));
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repositoryHandler.replace.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonArray> findAll(String model, JsonObject params) {
        Future<JsonArray> future = Future.future();

        JsonObject query = params != null ? params : new JsonObject();

        mongoClient.find(modelCollection.get(model), query, res -> {
            if (res.succeeded()) {
                future.complete(new JsonArray(
                    res.result()
                        .stream()
                        .map((record) -> record.put("id", record.remove("_id")))
                        .collect(Collectors.toList())
                ));
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repository.findAll.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonObject> findOne(String model, JsonObject params) {
        Future<JsonObject> future = Future.future();

        JsonObject query = params != null ? params : new JsonObject();

        mongoClient.findOne(modelCollection.get(model), query, null, res -> {
            if (res.succeeded()) {
                if (res.result() == null) {
                    future.fail(
                        new ApplicationException(
                            modelClass.get(model).getSimpleName() + " Not Found",
                            "repository.notFound.error"
                        )
                    );
                } else {
                    JsonObject object = res.result().copy();
                    object.put("id", object.remove("_id"));

                    future.complete(object);
                }
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repository.findOne.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonObject> delete(String model, JsonObject data) {
        Future<JsonObject> future = Future.future();

        JsonObject query = new JsonObject()
            .put("_id", data.getString("id"));

        mongoClient.removeDocument(modelCollection.get(model), query, res -> {
            if (res.succeeded()) {
                future.complete(data);
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repositoryHandler.delete.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonObject> deleteAll(String model, JsonObject params) {
        Future<JsonObject> future = Future.future();

        JsonObject query = params != null ? params : new JsonObject();

        mongoClient.removeDocuments(modelCollection.get(model), query, res -> {
            if (res.succeeded()) {
                future.complete(new JsonObject().put("success", true));
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repositoryHandler.deleteAll.error"
                ));
            }
        });

        return future;
    }

    public Future<JsonObject> count(String model, JsonObject params) {
        Future<JsonObject> future = Future.future();

        JsonObject query = params != null ? params : new JsonObject();

        mongoClient.count(modelCollection.get(model), query, res -> {
            if (res.succeeded()) {
                future.complete(new JsonObject().put("count", res.result()));
            } else {
                future.fail(new ApplicationException(
                    res.cause(),
                    "repositoryHandler.count.error"
                ));
            }
        });

        return future;
    }

}
