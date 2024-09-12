package com.isep.acme.domain.aggregates.product;

import lombok.Getter;

@Getter
class Description {

    private String description;

    protected Description(String description) {
        setDescription(description);
    }

    protected void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is a mandatory attribute of Product.");
        }

        if (description.length() > 1200) {
            throw new IllegalArgumentException("Description must not be greater than 1200 characters.");
        }
        this.description = description;
    }
}
