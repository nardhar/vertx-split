package org.nardhar.vertxsplit;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.VertxImpl;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.nardhar.vertxsplit.component.AppComponentBuilder;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> future) {
        JsonObject config = ((VertxImpl) vertx).getContext().config();

        config
            .put("mongoConnectionString", "mongodb://localhost:27017/f1nder")
            .put("models", new JsonArray()
                .add("com.f1nder.model.AccessToken")
                .add("com.f1nder.model.Service")
                .add("com.f1nder.model.ServiceType")
            );

        Logger logger = LoggerFactory.getLogger("verticle.main");

        AppComponentBuilder appComponentBuilder = new AppComponentBuilder(vertx, config);
        appComponentBuilder.build(future);
    }
}
