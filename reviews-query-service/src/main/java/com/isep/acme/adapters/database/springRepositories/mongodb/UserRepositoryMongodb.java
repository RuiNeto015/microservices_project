package com.isep.acme.adapters.database.springRepositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.UserMongo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "mongodb")
public interface UserRepositoryMongodb extends CrudRepository<UserMongo, String> {

    @Query("{ 'email' : ?0 }")
    UserMongo findByEmail(String email);
}
