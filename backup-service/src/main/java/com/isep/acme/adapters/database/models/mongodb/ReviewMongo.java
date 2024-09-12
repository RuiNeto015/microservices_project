package com.isep.acme.adapters.database.models.mongodb;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

@Document
@Data
@Getter
@AllArgsConstructor
public class ReviewMongo {
    @Id
    private String id;

    private final String approvalStatus;

    private final String text;

    private final String report;

    private final LocalDate publishingDate;

    private final String funFact;

    private final double rating;

    private final String sku;

    private final String userId;

    private Integer numApprovals;

}