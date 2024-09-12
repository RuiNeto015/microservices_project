package com.isep.acme.clients.dtos;

import lombok.Data;

@Data
public class ResponseUser {
    private final String id;
    private final String email;
    private final String password;
    private final String fullName;
    private final String nif;
    private final String address;
    private final String role;
}
