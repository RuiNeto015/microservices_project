package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.UserPg;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.postgresql.mappers.UserMapper;
import com.isep.acme.adapters.database.springRepositories.postgresql.UserRepositoryPostgresql;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.domain.aggregates.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("UserRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class UserPgRepositoryImpl implements IUserRepository {

    private final UserRepositoryPostgresql userRepository;
    private final UserMapper mapper;

    @Autowired
    public UserPgRepositoryImpl(UserRepositoryPostgresql userRepository, UserMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void create(User user) throws DatabaseException {
        Optional<UserPg> userById
                = this.userRepository.findById(user.getId());

        if (userById.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        Optional<UserPg> userByEmail = this.userRepository.findByEmail(user.getEmail());

        if (userByEmail.isPresent()) {
            throw new DatabaseException("Duplicate email violation.");
        }

        this.userRepository.save(this.mapper.toDatabaseObject(user));
    }

    @Override
    public void update(User user) throws DatabaseException {
        Optional<UserPg> matchingUser
                = this.userRepository.findById(user.getId());

        if (matchingUser.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }
        this.userRepository.save(this.mapper.toDatabaseObject(user));
    }

    @Override
    public Boolean exists(String id) {
        Optional<UserPg> user = this.userRepository.findById(id);
        return user.isPresent();
    }

    @Override
    public User findById(String id) {
        try {
            Optional<UserPg> user = this.userRepository.findById(id);
            return user.map(this.mapper::toDomainObject).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User findByEmail(String email) {
        try {
            Optional<UserPg> user = this.userRepository.findByEmail(email);
            return user.map(this.mapper::toDomainObject).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
