package org.nardhar.vertxsplit.model;

import lombok.Builder;
import lombok.Data;
import org.nardhar.vertxsplit.vertx.repository.Model;

import java.util.Date;

@Data
@Builder
public class User implements Model {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String country;
    private String city;
    private Date birthDate;

}
