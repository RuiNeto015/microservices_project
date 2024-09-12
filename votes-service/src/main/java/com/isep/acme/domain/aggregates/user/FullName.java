package com.isep.acme.domain.aggregates.user;

import lombok.Getter;

@Getter
class FullName {

    private String fullName;

    protected FullName(String fullName) {
        this.setFullName(fullName);
    }

    private void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
