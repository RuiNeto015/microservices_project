package com.isep.acme.adapters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewForVotesServiceDTO {

    private final String id;
    private final String approvalStatus;
    private final String userId;
    private final Integer numApprovals;
}
