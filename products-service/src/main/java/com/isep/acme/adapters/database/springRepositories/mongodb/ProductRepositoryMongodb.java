package com.isep.acme.adapters.database.springRepositories.mongodb;


import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public interface ProductRepositoryMongodb extends MongoRepository<ProductMongo, String> {

    Optional<ProductMongo> findBySku(String sku);

    void deleteBySku(String sku);

    List<ProductMongo> findByDesignation(String designation);
}
