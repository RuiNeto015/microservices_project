package com.isep.acme.domain.aggregates.review;

import com.isep.acme.domain.enums.ApprovalStatusEnum;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Review {

    @Getter
    private final String id;

    @Getter
    private ApprovalStatusEnum approvalStatus;

    private Text text;

    private Report report;

    @Getter
    private final LocalDate publishingDate;

    @Getter
    private final String funFact;

    private Rating rating;

    @Getter
    private final String userId;

    @Getter
    private final String sku;

    @Getter
    @Setter
    private Integer numApprovals;

    @Getter
    private final Map<String, VoteEnum> votes;

    public Review(String id, ApprovalStatusEnum approvalStatus, String text, String report, LocalDate publishingDate,
                  String funFact, double rating, String userId, String sku, Map<String, VoteEnum> votes,
                  Integer numApproves) {

        this.id = id;
        this.approvalStatus = approvalStatus;
        this.text = new Text(text);
        this.report = new Report(report);
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.rating = new Rating(rating);
        this.userId = userId;
        this.sku = sku;
        this.votes = votes;
        this.numApprovals = numApproves;
    }

    public Review(String text, String report, String funFact, double rating, String userId,
                  String sku) {
        this.id = String.valueOf(UUID.randomUUID());
        this.approvalStatus = ApprovalStatusEnum.Pending;
        this.text = new Text(text);
        this.report = new Report(report);
        this.publishingDate = LocalDate.now();
        this.funFact = funFact;
        this.rating = new Rating(rating);
        this.userId = userId;
        this.sku = sku;
        this.votes = new HashMap<>();
        this.numApprovals = 0;
    }

    public void vote(String userId, VoteEnum vote) throws Exception {
        if (!this.approvalStatus.equals(ApprovalStatusEnum.Approved)) {
            throw new Exception("Review state does not support votes");
        }
        votes.put(userId, vote);
    }

    public boolean removeVote(String userId) throws Exception {
        if (!this.approvalStatus.equals(ApprovalStatusEnum.Approved)) {
            throw new Exception("Review state does not support votes");
        }
        if (votes.containsKey(userId)) {
            votes.remove(userId);
            return true;
        }
        return false;
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

    public void setText(String text) {
        this.text = new Text(text);
    }

    public String getText() {
        return this.text.getText();
    }

    public String getReport() {
        return this.report.getReport();
    }

    public void setRating(double rating) {
        this.rating = new Rating(rating);
    }

    public double getRating() {
        return this.rating.getRating();
    }
}
