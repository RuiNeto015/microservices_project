package com.isep.acme.clients.dtos;

import lombok.Data;

@Data
public class ResponseReview {
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
