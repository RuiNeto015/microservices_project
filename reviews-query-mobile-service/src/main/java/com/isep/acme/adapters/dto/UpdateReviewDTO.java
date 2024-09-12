package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class UpdateReviewDTO{

    @NotNull
    @NotBlank
    private final String id;

    @NotNull
    @NotBlank
    private final String text;

    @NotNull
    private final double rating;
}
