package com.isep.acme.clients.dtos;

import lombok.Data;

@Data
public class ResponseReview {
    private final String id;
    private final String approvalStatus;
    private final String userId;
    private final Integer numApprovals;
}
