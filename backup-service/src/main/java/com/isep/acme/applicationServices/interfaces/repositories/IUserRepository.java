package com.isep.acme.applicationServices.interfaces.repositories;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.domain.aggregates.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserRepository {

    @Transactional
    void create(User user) throws DatabaseException;

    @Transactional
    void update(User user) throws DatabaseException;

    @Transactional
    User findById(String id);

    @Transactional
    User findByEmail(String email);

    @Transactional
    List<User> findAll();
}
