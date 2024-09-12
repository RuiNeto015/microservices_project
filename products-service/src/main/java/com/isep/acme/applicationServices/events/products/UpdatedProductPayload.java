package com.isep.acme.applicationServices.events.products;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedProductPayload {

    private String sku;
    private String designation;
    private String description;
}
