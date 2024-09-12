package com.isep.acme.adapters.database.springRepositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public interface ReviewRepositoryMongodb extends CrudRepository<ReviewMongo, String> {

    @Query("{ 'userId' : ?0 }")
    List<ReviewMongo> findByUser(String userId);

    @Query("{ 'approvalStatus' : 'Approved' }")
    List<ReviewMongo> findApproved();

    void deleteAllBySku(String sku);

}
