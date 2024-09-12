package com.isep.acme.applicationServices.events.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedProductPayload {

    private String sku;
    private String designation;
    private String description;
    private Integer numApprovals;

}
