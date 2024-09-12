package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@AllArgsConstructor
public class FetchUserDTO {
    private final String id;

    private final String email;

    private final String fullName;

    private final String nif;

    private final String address;

    private final String role;

}
