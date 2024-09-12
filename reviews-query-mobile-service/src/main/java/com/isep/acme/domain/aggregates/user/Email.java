package com.isep.acme.domain.aggregates.user;

import lombok.Getter;

@Getter
class Email {

    private String email;

    protected Email(String email) {
        this.setEmail(email);
    }

    private void setEmail(String email) {
        this.email = email;
    }
}
