package com.isep.acme.domain.aggregates.review;

import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Review {

    @Getter
    private final String id;

    @Getter
    private final String userId;

    @Getter
    private ApprovalStatusEnum approvalStatus;

    @Getter
    @Setter
    private Integer numApprovals;

    public Review(String id, ApprovalStatusEnum approvalStatus, Integer numApproves, String userId) {

        this.id = id;
        this.approvalStatus = approvalStatus;
        this.numApprovals = numApproves;
        this.userId = userId;
    }

    public Review(String userId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.approvalStatus = ApprovalStatusEnum.Pending;
        this.numApprovals = 0;
        this.userId = userId;
    }

    public void approve() {
        if (this.numApprovals == 0) {
            this.numApprovals++;
        } else if (this.numApprovals == 1) {
            this.numApprovals++;
            this.approvalStatus = ApprovalStatusEnum.Approved;
        }
    }

    public void reject(String report) {
        this.approvalStatus = ApprovalStatusEnum.Rejected;
    }

}
