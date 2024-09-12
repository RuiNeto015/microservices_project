package com.isep.acme.adapters.database.models.postgresql;

import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity(name = "vote_postgresql")
@Getter
public class VotePg implements Serializable {

    @EmbeddedId
    private VoteKeyPg id;

    @Column(nullable = false)
    private String vote;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("user_id")
    @JoinColumn(name = "user_id", nullable = false)
    private UserPg user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("review_id")
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewPg review;

    protected VotePg() {
    }

    public VotePg(UserPg user, ReviewPg review, String vote) {
        this.id = new VoteKeyPg(user.getId(), review.getId());
        this.user = user;
        this.review = review;
        this.vote = vote;
    }

}
