package com.isep.acme.adapters.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@ToString
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
