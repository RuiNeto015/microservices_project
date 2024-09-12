package com.isep.acme.domain.aggregates.product;

import lombok.Getter;

@Getter
class Designation {

    private String designation;

    protected Designation(String designation) {
        setDesignation(designation);
    }

    protected void setDesignation(String designation) {
        if (designation == null || designation.isBlank()) {
            throw new IllegalArgumentException("Designation is a mandatory attribute of Product.");
        }
        if (designation.length() > 50) {
            throw new IllegalArgumentException("Designation must not be greater than 50 characters.");
        }
        this.designation = designation;
    }
}
