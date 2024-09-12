package com.isep.acme.domain.aggregates.user;

import lombok.Getter;

@Getter
class Password {

    private String password;

    protected Password(String password) {
        this.setPassword(password);
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
