package com.isep.acme.domain.aggregates.votes;

import com.isep.acme.domain.enums.VoteEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Vote {

    private String userId;

    private String reviewId;

    private VoteEnum vote;

}
