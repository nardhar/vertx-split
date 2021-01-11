package org.nardhar.vertxsplit.vertx.eventbus;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;

import java.util.List;

public interface ConsumerVerticle extends BusConsumer {

    // Creating a list of Futures for tracking the register completion
    List<Future> getConsumers();
    Vertx getVertx();

    /**
     * Usual override of AbstractVerticle.start
     * @param startFuture
     */
    default void start(Future<Void> startFuture) {
        // calling to dummy method
        registerConsumers();

        // actual tracking of consumer registering
        completeRegistering(startFuture);
    }

    void registerConsumers();

    /**
     * Tracking of registering completion
     * @param startFuture
     */
    default void completeRegistering(Future<Void> startFuture) {
        // waiting for all the consumers to be completed
        CompositeFuture.all(getConsumers()).setHandler((ar) -> {
            // if all the consumers had been successfully registered, then startFuture is complete
            if (ar.succeeded()) startFuture.complete();
            // otherwise propagate the error
            else startFuture.fail(ar.cause());
        });
    }

    /**
     * EventBus Consumer adding with default wrapper for completing
     * @param address The eventBus address
     * @param handler The handler
     */
    default <T> void addConsumer(String address, Handler<Message<T>> handler) {
        // creating a future for adding to the consumer list
        Future<Void> completer = Future.future();
        // actual registering of the handler in the eventBus
        getVertx().eventBus().consumer(address, handler).completionHandler((ar) -> {
            // waiting for its registering to be completed
            if (ar.succeeded()) {
                System.out.println("Consumer registered at " + address);
                completer.complete();
            } else {
                completer.fail(ar.cause());
            }
        });
        // adding the completer future to the consumer list
        getConsumers().add(completer);
    }

    default void addDeployFuture(Future future) {
        getConsumers().add(future);
    }

}
