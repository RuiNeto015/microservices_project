package com.isep.acme.domain.aggregates.user;

import lombok.Getter;

@Getter
class Nif {

    private String nif;

    protected Nif(String nif) {
        this.setNif(nif);
    }

    private void setNif(String nif) {
        this.nif = nif;
    }
}
