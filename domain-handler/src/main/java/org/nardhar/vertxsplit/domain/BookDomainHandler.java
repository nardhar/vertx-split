package org.nardhar.vertxsplit.domain;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.nardhar.vertxsplit.model.Book;
import org.nardhar.vertxsplit.vertx.repository.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class BookDomainHandler implements BookDomain {

    private Repository repository;

    @Inject
    public BookDomainHandler(Repository repository) {
        this.repository = repository;
    }

    public Future<List<Book>> list(JsonObject params) {
        return repository.findAll(Book.class.getName(), params)
            .compose(jsonToFutureClassList(Book.class));
    }

    public Future<Book> get(JsonObject params) {
        return repository.findOne(Book.class.getName(), params)
            .compose(jsonToFutureClass(Book.class));
    }

    public Future<Book> save(JsonObject params) {
        // build the model
        Book book = Book.builder()
            .name(params.getString("name").toUpperCase())
            .description(params.getString("description"))
            .type(params.getInteger("type"))
            .build();

        // validate and save futures chain
        return book.validate()
            // could add more validations from bus
            // error verifying
            .compose(verifyErrors(book))
            // actual save in the repository
            .compose(validatedBook ->
                repository.save(Book.class.getName(), JsonObject.mapFrom(validatedBook))
            )
            .compose(jsonToFutureClass(Book.class));
    }

    public Future<Book> update(JsonObject params) {
        // build the model
        Book book = Book.builder()
            .id(params.getString("id"))
            .name(params.getString("name").toUpperCase())
            .description(params.getString("description"))
            .type(params.getInteger("type"))
            .build();

        // validate and save futures chain
        return book.validate()
            // could add more validations from bus
            // error verifying
            .compose(verifyErrors(book))
            // actual save in the repository
            .compose(validatedBook ->
                repository.update(Book.class.getName(), JsonObject.mapFrom(validatedBook))
            )
            .compose(jsonToFutureClass(Book.class));
    }

    public Future<Book> delete(JsonObject params) {
        return repository.delete(Book.class.getName(), new JsonObject()
            .put("id", params.getString("id"))
        ).compose(jsonToFutureClass(Book.class));
    }

}
