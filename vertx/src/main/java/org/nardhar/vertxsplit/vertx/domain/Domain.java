package org.nardhar.vertxsplit.vertx.domain;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.vertx.converter.Converter;
import org.nardhar.vertxsplit.vertx.exception.ValidationException;
import org.nardhar.vertxsplit.vertx.repository.Model;

import java.util.List;
import java.util.function.Function;

public interface Domain<T> extends Converter {

    Future<List<T>> list(JsonObject params);
    Future<T> get(JsonObject params);
    Future<T> save(JsonObject params);
    Future<T> update(JsonObject params);
    Future<T> delete(JsonObject params);

    /**
     * Returns a function which in turn verifies if a ValidationException has any errors or if should continue with the validated model
     * @param modelInstance The model to respond
     * @param <T> The type of the model
     * @return The function to be executed for chaining
     */
    default <T extends Model> Function<ValidationException, Future<T>> verifyErrors(T modelInstance) {
        return (validationException) -> {
            if (validationException.hasErrors()) {
                return Future.failedFuture(validationException);
            }
            return Future.succeededFuture(modelInstance);
        };
    }
}
