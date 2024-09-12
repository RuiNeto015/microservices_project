package com.isep.acme.adapters.dto;

import com.isep.acme.domain.enums.VoteEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@AllArgsConstructor
public class FetchReviewDTO {

    private final String id;

    private final String approvalStatus;

    private final String text;

    private final String report;

    private final LocalDate publishingDate;

    private final String funFact;

    private final double rating;

    private final String userId;

    private final String sku;

    private final Map<String, VoteEnum> votes;
}
