package org.nardhar.vertxsplit.vertx.converter;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface Converter {

    default <T> JsonObject toJsonObject(T object) {
        return JsonObject.mapFrom(object);
    }

    default <T> Future<JsonObject> toFutureJsonObject(T object) {
        return Future.succeededFuture(toJsonObject(object));
    }

    default <T> JsonArray toJsonArray(List<T> list) {
        return new JsonArray(
            list.stream()
                .map(this::toJsonObject)
                .collect(Collectors.toList())
        );
    }

    default <T> Future<JsonArray> toFutureJsonArray(List<T> list) {
        return Future.succeededFuture(toJsonArray(list));
    }

    default <T, U> Function<T, Future<U>> toFutureJsonObject(Function<T, U> fun) {
        return (obj) -> Future.succeededFuture(fun.apply(obj));
    }

    default <T> Function<JsonArray, Future<JsonArray>> toFutureJsonArray(Function<T, JsonObject> fun) {
        return (array) -> Future.succeededFuture(new JsonArray(
            array.stream()
                .map(obj -> fun.apply((T) obj))
                .collect(Collectors.toList())
        ));
    }

    default <T> Function<List<T>, Future<JsonArray>> listToFutureJsonArray(Function<T, JsonObject> fun) {
        return (array) -> Future.succeededFuture(new JsonArray(
            array.stream()
                .map(fun)
                .collect(Collectors.toList())
        ));
    }

    default <T> Function<JsonObject, Future<T>> jsonToFutureClass(Class<T> clazz) {
        return (json) -> Future.succeededFuture(json.mapTo(clazz));
    }

    default <T> Function<JsonArray, Future<List<T>>> jsonToFutureClassList(Class<T> clazz) {
        return (jsonArray) -> Future.succeededFuture(
            jsonArray.stream()
                .map(json -> ((JsonObject)json).mapTo(clazz))
                .collect(Collectors.toList())
        );
    }

}
