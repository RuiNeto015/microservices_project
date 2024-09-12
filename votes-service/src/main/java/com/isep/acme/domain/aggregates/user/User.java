package com.isep.acme.domain.aggregates.user;

import com.isep.acme.domain.enums.RoleEnum;
import lombok.Getter;

import java.util.UUID;

public class User {

    @Getter
    private final String id;

    private Email email;

    private Password password;

    private FullName fullName;

    private Nif nif;

    private Address address;

    @Getter
    private final RoleEnum role;

    public User(String id, String email, String password, String fullName, String nif, String address, RoleEnum role) {
        this.id = id;
        this.email = new Email(email);
        this.password = new Password(password);
        this.fullName = new FullName(fullName);
        this.nif = new Nif(nif);
        this.address = new Address(address);
        this.role = role;
    }

    public User(String email, String password, String fullName, String nif, String address, RoleEnum role) {
        this.id = UUID.randomUUID().toString();
        this.email = new Email(email);
        this.password = new Password(password);
        this.fullName = new FullName(fullName);
        this.nif = new Nif(nif);
        this.address = new Address(address);
        this.role = role;
    }

    public void setEmail(String email) {
        this.email = new Email(email);
    }

    public String getEmail() {
        return this.email.getEmail();
    }

    public void setPassword(String password) {
        this.password = new Password(password);
    }

    public String getPassword() {
        return this.password.getPassword();
    }

    public void setFullName(String fullName) {
        this.fullName = new FullName(fullName);
    }

    public String getFullName() {
        return this.fullName.getFullName();
    }

    public void setNif(String nif) {
        this.nif = new Nif(nif);
    }

    public String getNif() {
        return this.nif.getNif();
    }

    public void setAddress(String address) {
        this.address = new Address(address);
    }

    public String getAddress() {
        return this.address.getAddress();
    }
}
