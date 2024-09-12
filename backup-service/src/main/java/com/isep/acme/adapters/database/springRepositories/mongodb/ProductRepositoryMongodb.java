package com.isep.acme.adapters.database.springRepositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "mongodb")
public interface ProductRepositoryMongodb extends CrudRepository<ProductMongo, String> {
    @Query("{ 'designation' : ?0 }")
    List<ProductMongo> findByDesignation(String designation);
}
