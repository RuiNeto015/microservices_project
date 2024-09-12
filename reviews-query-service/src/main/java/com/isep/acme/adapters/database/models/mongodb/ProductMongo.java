package com.isep.acme.adapters.database.models.mongodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@AllArgsConstructor
public class ProductMongo {

    private String sku;

    private String approvalStatus;

    private Integer numApprovals;

}
