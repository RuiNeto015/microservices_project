package com.isep.acme.bootstrapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.clients.BackupWebClient;
import com.isep.acme.clients.dtos.ResponseUser;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserBootstrapper {

    private final IUserRepository userRepo;

    @Autowired
    private BackupWebClient backupWebClient;

    public void run() throws IllegalArgumentException, DatabaseException {

        Gson gson = new Gson();

        //Save users

        ResponseEntity<String> backupUserRequestResponse;
        try {
            backupUserRequestResponse = this.backupWebClient.requestUsersInit();
        } catch (Exception e) {
            System.out.println("\n\n\n" + e);
            throw new IllegalArgumentException("Error communicating with the backup service!");
        }

        if (backupUserRequestResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Error communicating with the backup service! Response Status isn't OK");
        }

        TypeToken<List<ResponseUser>> token = new TypeToken<List<ResponseUser>>() {
        };
        String usersResponseBody = backupUserRequestResponse.getBody();
        List<ResponseUser> usersList = gson.fromJson(usersResponseBody, token.getType());

        if (usersList == null || usersList.isEmpty()) {
            System.out.println("No users on the backup service. Everything fine!");
        } else {
            for (ResponseUser u : usersList) {
                User user = new User(u.getId(), u.getEmail(), u.getPassword(), u.getFullName(), u.getNif(),
                        u.getAddress(), RoleEnum.valueOf(u.getRole()));
                if (!this.userRepo.exists(u.getId())) {
                    this.userRepo.create(user);
                }
            }
        }
    }
}
