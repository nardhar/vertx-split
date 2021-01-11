package org.nardhar.vertxsplit.web.module;

import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.web.WebServer;

@Module
public class WebServerModule {

    private Vertx vertx;
    private JsonObject config;

    public WebServerModule(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    @Provides
    public WebServer provideWebServer() {
        return new WebServer(vertx, config);
    }

}
