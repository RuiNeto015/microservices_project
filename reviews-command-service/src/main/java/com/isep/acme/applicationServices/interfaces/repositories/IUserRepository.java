package com.isep.acme.applicationServices.interfaces.repositories;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.domain.aggregates.user.User;
import org.springframework.transaction.annotation.Transactional;

public interface IUserRepository {

    @Transactional
    void create(User user) throws DatabaseException;

    @Transactional
    void update(User user) throws DatabaseException;

    @Transactional
    Boolean exists(String id);

    @Transactional
    User findById(String id);

    @Transactional
    User findByEmail(String email);
}
