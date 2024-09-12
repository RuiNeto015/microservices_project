package com.isep.acme.domain.aggregates.user;

import lombok.Getter;

@Getter
class Address {

    private String address;

    protected Address(String address) {
        this.setAddress(address);
    }

    private void setAddress(String address) {
        this.address = address;
    }
}
