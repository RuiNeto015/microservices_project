package com.isep.acme.applicationServices.events.votes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteAddedPayload {

    private String userId;
    private String reviewId;
    private String voteType;

}
