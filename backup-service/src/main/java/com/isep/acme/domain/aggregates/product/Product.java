package com.isep.acme.domain.aggregates.product;

import com.isep.acme.domain.aggregates.review.Report;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Product {

    private final Sku sku;

    @Getter
    private ApprovalStatusEnum approvalStatus;

    private Report report;

    private Designation designation;

    private Description description;

    private double aggregatedRating;

    @Getter
    private final List<Review> reviews;

    @Getter
    private Integer numApprovals;

    public Product(String sku, String designation, String description) {
        this.sku = new Sku(sku);
        this.approvalStatus = ApprovalStatusEnum.Pending;
        this.designation = new Designation(designation);
        this.description = new Description(description);
        this.aggregatedRating = 0;
        this.reviews = new ArrayList<>();
        this.numApprovals = 0;
    }

    public Product(String sku, ApprovalStatusEnum approvalStatus, String designation, String description, double aggregatedRating, List<Review> reviews, Integer numApprovals) {
        this.sku = new Sku(sku);
        this.approvalStatus = approvalStatus;
        this.designation = new Designation(designation);
        this.description = new Description(description);
        this.aggregatedRating = aggregatedRating;
        this.reviews = reviews;
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

    public String addReview(String text, String funFact, double rating, String userId)
            throws Exception {

        String reviewId = UUID.randomUUID().toString();

        if (!reviewIsValid(reviewId, userId)) {
            throw new Exception("Review conflicts with existing review");
        }

        Review r = new Review(reviewId, ApprovalStatusEnum.Pending, text, "", LocalDate.now(), funFact, rating,
                userId, 0);
        this.reviews.add(r);
        this.aggregatedRating = ((this.aggregatedRating * (this.reviews.size() - 1)) + rating) / this.reviews.size();
        return reviewId;
    }

    private boolean reviewIsValid(String reviewId, String userId) {
        for (Review r : this.reviews) {
            if (r.getId().equals(reviewId) || r.getUserId().equals(userId)) {
                return false;
            }
        }
        return true;
    }

    private Review findReview(String id) {
        for (Review r : this.reviews) {
            if (r.getId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public boolean removeReview(String reviewId) {
        return this.reviews.remove(this.findReview(reviewId));
    }

    public void setDesignation(String designation) {
        this.designation = new Designation(designation);
    }

    public void setDescription(String description) {
        this.description = new Description(description);
    }

    public String getSku() {
        return this.sku.getSku();
    }

    public String getDesignation() {
        return this.designation.getDesignation();
    }

    public String getDescription() {
        return this.description.getDescription();
    }

    public double getAggregatedRating() {
        return Math.round(this.aggregatedRating);
    }
}
