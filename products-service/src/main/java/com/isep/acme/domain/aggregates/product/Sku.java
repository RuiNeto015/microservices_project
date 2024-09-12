package com.isep.acme.domain.aggregates.product;

import lombok.Getter;

@Getter
public class Sku {

    private String sku;

    protected Sku(String sku){
        setSku(sku);
    }

    protected void setSku(String sku) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU is a mandatory attribute of Product.");
        }
        if (sku.length() > 12 || sku.length() < 8) {
            throw new IllegalArgumentException("SKU must  between 8 and 12 characters long.");
        }
        this.sku = sku;
    }
}
