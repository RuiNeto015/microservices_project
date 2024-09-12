package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductForReviewsServiceDTO {

    private final String sku;
    private final String approvalStatus;
    private final Integer numApprovals;
}
