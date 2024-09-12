package com.isep.acme.adapters.database.springRepositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.UserPg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "postgresql")
public interface UserRepositoryPostgresql extends CrudRepository<UserPg,String> {

    @Query("SELECT u FROM user_postgresql u WHERE u.email=:email")
    Optional<UserPg> findByEmail(String email);
}
