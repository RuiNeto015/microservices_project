package com.isep.acme.clients.dtos;

import com.isep.acme.domain.enums.VoteEnum;
import lombok.Data;

@Data
public class ResponseVote {
    private String userId;
    private String reviewId;
    private VoteEnum vote;
}
