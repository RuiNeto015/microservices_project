/*
package com.isep.acme.integrationTests;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateUserDTO;
import com.isep.acme.adapters.dto.FetchUserDTO;
import com.isep.acme.applicationServices.UserService;
import com.isep.acme.domain.enums.RoleEnum;
import com.isep.acme.integrationTests.config.ContainersEnvironment;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest extends ContainersEnvironment {

    @Autowired
    UserService userService;

    private String userId;

    @BeforeAll
    void init() throws Exception {
        CreateUserDTO user1 = new CreateUserDTO("simao@gmail.com", "abcde",
                "Simao Santos", "123456789", "R. xpto", RoleEnum.RegisteredUser.name());
        this.userService.create(user1);
    }

    @Test
    @Order(1)
    void createUserWithExistingEmail() throws DatabaseException {
        CreateUserDTO user1 = new CreateUserDTO("simao@gmail.com", "abcde",
                "Simao Santos", "123456788", "R. xpto", RoleEnum.RegisteredUser.name());
        Assertions.assertThrows(DatabaseException.class, () -> this.userService.create(user1));
    }

    @Test
    @Order(2)
    void findByEmail() {
        FetchUserDTO user = this.userService.findByEmail("simao@gmail.com");
        this.userId = user.getId();
        assert user.getEmail().equals("simao@gmail.com");
    }

    @Test
    @Order(3)
    void findById() {
        assert this.userService.findById(this.userId).getId().equals(this.userId);
    }
}
*/
