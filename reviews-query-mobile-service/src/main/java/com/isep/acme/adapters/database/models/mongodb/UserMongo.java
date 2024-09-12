package com.isep.acme.adapters.database.models.mongodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.*;

import org.springframework.data.annotation.Id;

@Document
@Getter
@AllArgsConstructor
public class UserMongo {

    @Id
    private String id;

    private final String email;

    private final String password;

    private final String fullName;

    private final String role;

    private final String nif;

    private final String address;

    @DBRef
    private final List<ReviewMongo> reviews;

    private final Map<String, String> votes;
}
