package com.isep.acme.adapters.database.models.postgresql;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Entity(name = "review_postgresql")
public class ReviewPg {

    @Id
    private String id;

    @Column(nullable = false)
    private String approvalStatus;

    @Column(nullable = false)
    private String text;

    private String report;

    @Column(nullable = false)
    private LocalDate publishingDate;

    @Column(nullable = false)
    private String funFact;

    @Column(nullable = false)
    private double rating;

    @Column(nullable = false)
    private Integer numApprovals;

    @ManyToOne()
    @JoinColumn(name = "product_sku", nullable = false)
    private ProductPg product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserPg user;

    protected ReviewPg() {
    }

    public ReviewPg(final String id, final String approvalStatus, final String text, final String report,
                    final LocalDate publishingDate, final String funFact, final Double rating, final ProductPg product,
                    final UserPg user, Integer numApprovals) {

        this.id = id;
        this.approvalStatus = approvalStatus;
        this.text = text;
        this.report = report;
        this.publishingDate = publishingDate;
        this.funFact = funFact;
        this.rating = rating;
        this.product = product;
        this.user = user;
        this.numApprovals = numApprovals;
    }
}
