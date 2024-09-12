package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class UpdateUserDTO {

    @NotNull
    @NotBlank
    private final String id;

    @NotNull
    @NotBlank
    private final String email;

    @NotNull
    @NotBlank
    private final String password;

    @NotNull
    @NotBlank
    private final String fullName;

    @NotNull
    @NotBlank
    private final String nif;

    @NotNull
    @NotBlank
    private final String address;
}
