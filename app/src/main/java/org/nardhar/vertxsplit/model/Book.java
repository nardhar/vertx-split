package org.nardhar.vertxsplit.model;

import lombok.Builder;
import lombok.Data;
import org.nardhar.vertxsplit.vertx.repository.Model;

@Data
@Builder
public class Book implements Model {
    private String id;
    private String name;
    private String description;
    private int type;

    public static final String collection = "service";
}
