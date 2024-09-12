package com.isep.acme.domain.aggregates.product;

import com.isep.acme.domain.aggregates.review.Rating;
import com.isep.acme.domain.aggregates.review.Report;
import com.isep.acme.domain.aggregates.review.Text;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class Review {

    @Getter
    private final String id;

    @Getter
    private final ApprovalStatusEnum approvalStatus;

    private final Text text;

    private final Report report;

    @Getter
    private final LocalDate publishingDate;

    @Getter
    private final String funFact;

    private final Rating rating;

    @Getter
    @Setter
    private Integer numApprovals;

    @Getter
    private final String userId;

    public Review(String id, ApprovalStatusEnum approvalStatus, String text, String report, LocalDate publishingDate,
                  String funFact, double rating, String userId, Integer numApprovals) {

        this.id = id;
        this.approvalStatus = approvalStatus;
        this.text = new Text(text);
        this.report = new Report(report);
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.rating = new Rating(rating);
        this.userId = userId;
        this.numApprovals = numApprovals;
    }

    public String getText() {
        return this.text.getText();
    }

    public String getReport() {
        return this.report.getReport();
    }

    public double getRating() {
        return this.rating.getRating();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Review review = (Review) obj;
        return id != null && id.equals(review.id);
    }
}
