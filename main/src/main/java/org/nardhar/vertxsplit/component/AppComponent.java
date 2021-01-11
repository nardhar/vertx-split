package org.nardhar.vertxsplit.component;

import dagger.Component;
import org.nardhar.vertxsplit.controller.ApiController;
import org.nardhar.vertxsplit.controller.BookController;
import org.nardhar.vertxsplit.controller.module.ApiControllerModule;
import org.nardhar.vertxsplit.controller.module.BookControllerModule;
import org.nardhar.vertxsplit.domain.BookDomain;
import org.nardhar.vertxsplit.domain.module.BookDomainHandlerModule;
import org.nardhar.vertxsplit.vertx.repository.Repository;
import org.nardhar.vertxsplit.vertx.repository.handler.RepositoryHandlerModule;
import org.nardhar.vertxsplit.web.WebServer;
import org.nardhar.vertxsplit.web.module.WebServerModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
    WebServerModule.class,
    RepositoryHandlerModule.class,
    BookDomainHandlerModule.class,
    BookControllerModule.class,
    ApiControllerModule.class
})
public interface AppComponent {

    WebServer getWebServer();
    Repository getRepository();

    // Injectable Domains
    BookDomain getServiceDomain();

    // Not injectable but instanciable Controllers
    ApiController getApiController();
    BookController getServiceController();

}
