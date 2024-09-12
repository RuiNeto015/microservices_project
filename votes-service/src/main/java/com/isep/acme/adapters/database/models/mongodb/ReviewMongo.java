package com.isep.acme.adapters.database.models.mongodb;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

@Document
@Getter
@AllArgsConstructor
public class ReviewMongo {
    @Id
    private String id;

    private final String approvalStatus;

    private Integer numApprovals;

}
