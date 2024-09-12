package com.isep.acme.domain.aggregates.product;

import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.Getter;
import lombok.Setter;

public class Product {

    @Getter
    private final Sku sku;

    @Getter
    private ApprovalStatusEnum approvalStatus;

    @Getter
    private Designation designation;

    @Getter
    private Description description;

    private Report report;

    private final double aggregatedRating;

    @Getter
    @Setter
    private Integer numApprovals;

    public Product(String sku, String designation, String description) {
        this.sku = new Sku(sku);
        this.approvalStatus = ApprovalStatusEnum.Pending;
        this.designation = new Designation(designation);
        this.description = new Description(description);
        this.aggregatedRating = 0;
        this.numApprovals = 0;
    }

    public Product(String sku, ApprovalStatusEnum approvalStatus, String designation, String description,
                   Integer numApprovals) {
        this.sku = new Sku(sku);
        this.approvalStatus = approvalStatus;
        this.designation = new Designation(designation);
        this.description = new Description(description);
        this.aggregatedRating = 0;
        this.numApprovals = numApprovals;
    }

    public Product(String sku, ApprovalStatusEnum approvalStatus, String designation, String description,
                   Integer numApprovals, double aggregatedRating) {
        this.sku = new Sku(sku);
        this.approvalStatus = approvalStatus;
        this.designation = new Designation(designation);
        this.description = new Description(description);
        this.aggregatedRating = aggregatedRating;
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
        this.report = new Report(report);
        this.approvalStatus = ApprovalStatusEnum.Rejected;
    }

    public String getReport() {
        return this.report.getReport();
    }

    public void setDesignation(String designation) {
        this.designation = new Designation(designation);
    }

    public void setDescription(String description) {
        this.description = new Description(description);
    }

    public double getAggregatedRating() {
        return Math.round(this.aggregatedRating);
    }
}
