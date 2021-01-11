package org.nardhar.vertxsplit.vertx.repository.module;

import dagger.Module;
import dagger.Provides;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.vertx.repository.Repository;
import org.nardhar.vertxsplit.vertx.repository.RepositoryVerticle;
import org.nardhar.vertxsplit.vertx.repository.wrapper.RepositoryVerticleWrapper;

@Module
public class RepositoryVerticleModule {

    @Provides
    Repository provideRepository(Vertx vertx, JsonObject config) {
        DeploymentOptions options = new DeploymentOptions()
            .setConfig(config);

        vertx.deployVerticle(new RepositoryVerticle(), options);

        return new RepositoryVerticleWrapper(vertx);
    }
}
