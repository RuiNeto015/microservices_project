package com.isep.acme.adapters.database.repositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.UserMongo;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.springRepositories.mongodb.UserRepositoryMongodb;
import com.isep.acme.adapters.database.repositories.mongodb.mappers.UserMapper;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.domain.aggregates.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("UserRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public class UserMongoRepositoryImpl implements IUserRepository {

    private final UserRepositoryMongodb userRepository;

    private final UserMapper mapper;

    @Autowired
    public UserMongoRepositoryImpl(UserRepositoryMongodb userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void create(User user) throws DatabaseException {
        Optional<UserMongo> userById = this.userRepository.findById(user.getId());

        if (userById.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        UserMongo userByEmail
                = this.userRepository.findByEmail(user.getEmail());

        if (userByEmail != null) {
            throw new DatabaseException("Duplicate email violation.");
        }

        this.userRepository.save(this.mapper.toDatabaseObject(user, new ArrayList<>(), new HashMap<>()));
    }

    @Override
    public void update(User user) throws DatabaseException {
        Optional<UserMongo> matchingUser
                = this.userRepository.findById(user.getId());

        if (matchingUser.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }
        this.userRepository.save(this.mapper.toDatabaseObject(user, matchingUser.get().getReviews(),
                matchingUser.get().getVotes()));
    }

    @Override
    public Boolean exists(String id) {
        Optional<UserMongo> user = this.userRepository.findById(id);
        return user.isPresent();
    }

    @Override
    public User findById(String id) {
        try {
            Optional<UserMongo> user = this.userRepository.findById(id);
            return user.map(this.mapper::toDomainObject).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            return this.mapper.toDomainObject(this.userRepository.findByEmail(email));
        } catch (Exception e) {
            return null;
        }
    }
}
