package org.nardhar.vertxsplit.vertx.eventbus;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.vertx.converter.Converter;
import org.nardhar.vertxsplit.vertx.exception.ApplicationException;
import org.nardhar.vertxsplit.vertx.repository.Model;

import java.util.List;

public interface BusConsumer extends Converter {

    default void busRespondSuccess(Message<JsonObject> message, Object result) {
        message.reply(result);
    }

    default void busRespondFailure(Message<JsonObject> message, int statusCode, Throwable cause) {
        cause.printStackTrace();
        if (cause instanceof ApplicationException) {
            message.fail(statusCode, ((ApplicationException)cause).encode());
        } else {
            // cause.getMessage() could be an encoded ApplicationException
            // so we should try to parse it and convert to an instance of ApplicationException
            try {
                JsonObject exceptionJson = new JsonObject(cause.getMessage());
                message.fail(statusCode, new ApplicationException(
                    exceptionJson.getString("message"),
                    exceptionJson.getString("code")
                ).encode());
            } catch (DecodeException ex) {
                // otherwise
                message.fail(statusCode, new ApplicationException(cause, "undefined.error").encode());
            }
        }
    }

    /**
     * Returns a handler for a Future<JsonObject>
     * @param message The message from the eventBus to reply
     * @param <T> The type of the object to respond
     * @return The actual handler
     */
    default <T> Handler<AsyncResult<T>> busRespond(Message<JsonObject> message) {
        return (handler) -> {
            if (handler.succeeded()) {
                busRespondSuccess(message, handler.result());
            } else {
                busRespondFailure(message, 400, handler.cause());
            }
        };
    }

    default <T extends Model> Handler<AsyncResult<T>> busRespondModel(Message<JsonObject> message) {
        return (handler) -> {
            if (handler.succeeded()) {
                busRespondSuccess(message, toJsonObject(handler.result()));
            } else {
                busRespondFailure(message, 400, handler.cause());
            }
        };
    }

    default <T extends Model> Handler<AsyncResult<List<T>>> busRespondModelList(Message<JsonObject> message) {
        return (handler) -> {
            if (handler.succeeded()) {
                busRespondSuccess(message, toJsonArray(handler.result()));
            } else {
                busRespondFailure(message, 400, handler.cause());
            }
        };
    }

}
