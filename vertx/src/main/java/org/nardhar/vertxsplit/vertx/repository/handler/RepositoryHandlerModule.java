package org.nardhar.vertxsplit.vertx.repository.handler;

import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.vertx.repository.Repository;

@Module
public class RepositoryHandlerModule {

    private Vertx vertx;
    private JsonObject config;

    public RepositoryHandlerModule(Vertx vextx, JsonObject config) {
        this.vertx = vextx;
        this.config = config;
    }

    @Provides
    public Repository provideRepository() {
        return new RepositoryHandler(vertx, config);
    }

}
