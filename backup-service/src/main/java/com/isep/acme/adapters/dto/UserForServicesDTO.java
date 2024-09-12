package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserForServicesDTO {

    private final String id;
    private final String email;
    private final String password;
    private final String fullName;
    private final String nif;
    private final String address;
    private final String role;
}
