package com.isep.acme.adapters.database.models.postgresql;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity(name = "review_postgresql")
public class ReviewPg {

    @Id
    private String id;

    @Column(nullable = false)
    private String approvalStatus;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Integer numApprovals;

    protected ReviewPg() {
    }

    public ReviewPg(final String id, final String approvalStatus, Integer numApprovals, String userId) {
        this.id = id;
        this.approvalStatus = approvalStatus;
        this.numApprovals = numApprovals;
        this.userId = userId;
    }
}
