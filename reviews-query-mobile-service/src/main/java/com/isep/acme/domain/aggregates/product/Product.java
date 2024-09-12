package com.isep.acme.domain.aggregates.product;

import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.Data;
import lombok.Getter;

@Data
public class Product {

    private final String sku;

    @Getter
    private ApprovalStatusEnum approvalStatus;

    @Getter
    private Integer numApprovals;

    public Product(String sku) {
        this.numApprovals = 0;
        this.approvalStatus = ApprovalStatusEnum.Pending;
        this.sku = sku;
    }

    public Product(String sku, ApprovalStatusEnum approvalStatus, Integer numApprovals) {
        this.sku = sku;
        this.approvalStatus = approvalStatus;
        this.numApprovals = numApprovals;
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
