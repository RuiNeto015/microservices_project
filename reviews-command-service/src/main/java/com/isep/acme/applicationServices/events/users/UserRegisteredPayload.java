package com.isep.acme.applicationServices.events.users;

import com.isep.acme.domain.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredPayload {

    private String id;
    private String email;
    private String password;
    private String fullName;
    private String nif;
    private String address;
    private RoleEnum role;

}
