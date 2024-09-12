package com.isep.acme.adapters.database.springRepositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.UserPg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "postgresql")
public interface UserRepositoryPostgresql extends CrudRepository<UserPg,String> {

    @Query("SELECT u FROM user_postgresql u WHERE u.email=:email")
    UserPg findByEmail(String email);
}
