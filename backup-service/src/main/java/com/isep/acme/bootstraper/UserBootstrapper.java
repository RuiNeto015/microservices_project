package com.isep.acme.bootstraper;

import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBootstrapper {

    private final IUserRepository userRepo;

    private final PasswordEncoder encoder;

    public void run() throws Exception {
        User admin = new User("admin@mail.com", encoder.encode("AdminPW1"),
                "Jose Antonio", "355489123", "Rua Um", RoleEnum.Admin);
        if (this.userRepo.findByEmail("admin@mail.com") == null) {
            this.userRepo.create(admin);
        }

        ////////

        User mod = new User("mod@mail.com", encoder.encode("AdminPW1"),
                "Antonio Jose", "355489123", "Rua Um", RoleEnum.Mod);
        if (this.userRepo.findByEmail("mod@mail.com") == null) {
            this.userRepo.create(mod);
        }

        ////////

        User user1 = new User("user1@mail.com", encoder.encode("userPW1"),
                "Nuno Miguel", "253647883", "Rua tres", RoleEnum.RegisteredUser);
        if (userRepo.findByEmail("user1@mail.com") == null) {
            this.userRepo.create(user1);
        }

        ////////

        User user2 = new User("user2@mail.com", encoder.encode("userPW2"),
                "Miguel Nuno", "253698854", "Rua quatro", RoleEnum.RegisteredUser);
        if (this.userRepo.findByEmail("user2@mail.com") == null) {
            this.userRepo.create(user2);
        }

        ////////

        User user3 = new User("user3@mail.com", encoder.encode("userPW3"),
                "Antonio Pedro", "254148863", "Rua vinte", RoleEnum.RegisteredUser);
        if (this.userRepo.findByEmail("user3@mail.com") == null) {
            this.userRepo.create(user3);
        }

        ////////

        User user4 = new User("user4@mail.com", encoder.encode("userPW4"),
                "Pedro Antonio", "452369871", "Rua cinco", RoleEnum.RegisteredUser);
        if (this.userRepo.findByEmail("user4@mail.com") == null) {
            this.userRepo.create(user4);
        }
    }
}
