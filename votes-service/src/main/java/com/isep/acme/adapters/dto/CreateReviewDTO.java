package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class CreateReviewDTO {

    @NotNull
    @NotBlank
    private final String sku;

    @NotNull
    @NotBlank
    private final String text;


    @NotNull
    private final double rating;
}
