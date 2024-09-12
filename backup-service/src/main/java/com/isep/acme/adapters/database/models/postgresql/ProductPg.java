package com.isep.acme.adapters.database.models.postgresql;

import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity(name = "product_postgresql")
@Getter
public class ProductPg {

    @Id
    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String approvalStatus;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double aggregatedRating;

//    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private ProdImage prodImage;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY)
    private List<ReviewPg> reviews;

    @Column(nullable = false)
    private Integer numApprovals;

    protected ProductPg() {
    }

    public ProductPg(final String sku, final String approvalStatus, final String designation, final String description,
                     final double aggregatedRating, final Integer numApprovals) {
        this.sku = sku;
        this.approvalStatus = approvalStatus;
        this.designation = designation;
        this.description = description;
        this.aggregatedRating = aggregatedRating;
        this.numApprovals = numApprovals;
    }
}
