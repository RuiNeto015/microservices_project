package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class AuthRequestDTO {
    @NotNull
    @Email
    String email;

    @NotNull
    String password;
}
