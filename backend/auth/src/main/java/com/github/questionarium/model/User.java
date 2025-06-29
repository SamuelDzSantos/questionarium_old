package com.github.questionarium.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.questionarium.types.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class User {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";

    @Id
    private Long id;
    @Indexed(unique = true)
    private String login;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private UserRole role;
    private Boolean active;
    private Boolean uses2Factor;

}
