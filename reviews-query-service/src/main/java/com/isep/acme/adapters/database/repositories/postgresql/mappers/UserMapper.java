package com.isep.acme.adapters.database.repositories.postgresql.mappers;

import com.isep.acme.adapters.database.models.postgresql.UserPg;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.enums.RoleEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "postgresql")
public class UserMapper {

    public User toDomainObject(UserPg user) {
        return new User(user.getId(), user.getEmail(), user.getPassword(), user.getFullName(), user.getNif(),
                user.getAddress(), RoleEnum.valueOf(user.getRole()));
    }

    public UserPg toDatabaseObject(User user) {

        return new UserPg(user.getId(), user.getEmail(),
                user.getPassword(), user.getFullName(), user.getRole().name(), user.getNif(), user.getAddress());
    }
}
