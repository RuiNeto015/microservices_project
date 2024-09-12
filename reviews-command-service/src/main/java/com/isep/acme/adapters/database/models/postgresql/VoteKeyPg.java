package com.isep.acme.adapters.database.models.postgresql;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class VoteKeyPg implements Serializable {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "review_id")
    private String reviewId;
}
