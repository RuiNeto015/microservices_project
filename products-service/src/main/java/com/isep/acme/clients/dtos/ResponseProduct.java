package com.isep.acme.clients.dtos;

import lombok.Data;

@Data
public class ResponseProduct {
    private String sku;
    private String approvalStatus;
    private String designation;
    private String description;
    private double aggregatedRating;
    private int numApprovals;
}