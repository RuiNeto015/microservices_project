package com.isep.acme.adapters.database.models.mongodb;

import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document
@Getter
@AllArgsConstructor
public class ProductMongo {

    @Id
    private final String sku;

    private final String approvalStatus;

    private final String designation;

    private final String description;

    private final double aggregatedRating;

    private Integer numApprovals;
}
