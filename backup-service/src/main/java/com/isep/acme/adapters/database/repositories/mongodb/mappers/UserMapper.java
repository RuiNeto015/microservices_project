package com.isep.acme.adapters.database.repositories.mongodb.mappers;

import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import com.isep.acme.adapters.database.models.mongodb.UserMongo;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.enums.RoleEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "mongodb")
public class UserMapper {

    public User toDomainObject(UserMongo user) {
        return new User(user.getId(), user.getEmail(), user.getPassword(), user.getFullName(), user.getNif(),
                user.getAddress(), RoleEnum.valueOf(user.getRole()));
    }

    public UserMongo toDatabaseObject(User user, List<ReviewMongo> reviews,
                                      Map<String, String> votes) {

        return new UserMongo(user.getId(), user.getEmail(), user.getPassword(),
                user.getFullName(), user.getRole().name(), user.getNif(), user.getAddress(), reviews, votes);
    }


}
