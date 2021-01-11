package org.nardhar.vertxsplit.component;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.domain.module.BookDomainHandlerModule;
import org.nardhar.vertxsplit.vertx.repository.handler.RepositoryHandlerModule;
import org.nardhar.vertxsplit.vertx.web.RouteHandler;
import org.nardhar.vertxsplit.web.WebServer;
import org.nardhar.vertxsplit.web.module.WebServerModule;

import java.util.ArrayList;
import java.util.List;

public class AppComponentBuilder {

    private Vertx vertx;
    private JsonObject config;

    public AppComponentBuilder(Vertx vertx, JsonObject config) {
        this.vertx = vertx;
        this.config = config;
    }

    public void build(Future<Void> future) {
        // module creation
        WebServerModule webServerModule = new WebServerModule(vertx, config);
        RepositoryHandlerModule repositoryHandlerModule = new RepositoryHandlerModule(vertx, config);
        BookDomainHandlerModule bookDomainHandlerModule = new BookDomainHandlerModule();

        // component building with the created modules
        AppComponent appComponent = DaggerAppComponent.builder()
            .webServerModule(webServerModule)
            .repositoryHandlerModule(repositoryHandlerModule)
            .bookDomainHandlerModule(bookDomainHandlerModule)
            .build();

        // http server start
        List<RouteHandler> controllers = new ArrayList<>();
        controllers.add(appComponent.getServiceController());
        controllers.add(appComponent.getApiController());

        WebServer webServer = appComponent.getWebServer();
        webServer.start(controllers, future);
    }

}
