package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class FetchProductDTO {

    private final String sku;

    private final String approvalStatus;

    private final String designation;

    private final String description;

    private final double aggregatedRating;

    private final Integer numApprovals;
}
