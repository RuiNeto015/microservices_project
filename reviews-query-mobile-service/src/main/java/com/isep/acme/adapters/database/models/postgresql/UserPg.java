package com.isep.acme.adapters.database.models.postgresql;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity(name = "user_postgresql")
@Getter
public class UserPg {

    @Id
    private String id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String role; //why multiple roles?

    @Column(nullable = false, unique = true)
    private String nif;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ReviewPg> reviews;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<VotePg> votes;

    protected UserPg() {
    }

    public UserPg(final String id, final String email, final String password, final String fullName,
                  final String role, final String nif, final String address) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.nif = nif;
        this.address = address;
    }
}

