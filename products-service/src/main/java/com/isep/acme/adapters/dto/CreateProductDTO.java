package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateProductDTO {

    @Setter
    private String sku;

    @NotNull
    @NotBlank
    private final String designation;

    @NotNull
    @NotBlank
    private final String description;
}
