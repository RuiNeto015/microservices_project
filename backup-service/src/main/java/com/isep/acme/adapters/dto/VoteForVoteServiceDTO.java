package com.isep.acme.adapters.dto;

import com.isep.acme.domain.enums.VoteEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteForVoteServiceDTO {

    private String userId;
    private String reviewId;
    private VoteEnum vote;
}