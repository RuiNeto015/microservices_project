package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewForReviewsServiceDTO {

    private final String id;
    private final String approvalStatus;
    private final String text;
    private final String report;
    private final String publishingDate;
    private final String funFact;
    private final double rating;
    private final String userId;
    private final String sku;
    private final Integer numApprovals;
}
