package com.isep.acme.clients.dtos;

import lombok.Data;

@Data
public class ResponseProduct {
    private String sku;
    private String approvalStatus;
    private int numApprovals;
}
