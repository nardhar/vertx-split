package org.nardhar.vertxsplit.vertx.repository;

import io.vertx.core.Future;
import org.nardhar.vertxsplit.vertx.exception.ValidationException;

public interface Model {

    String getId();

    void setId(String id);

    default Future<ValidationException> validate() {
        return Future.succeededFuture(new ValidationException("", ""));
    };

}
