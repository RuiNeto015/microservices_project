package com.isep.acme.applicationServices.events.reviews;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreatedPayload {

    private String id;
    private String text;
    private String report;
    private LocalDate publishingDate;
    private String funFact;
    private double rating;
    private String userId;
    private String sku;
    private Integer numApprovals;

}
