package com.isep.acme.adapters.database.springRepositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public interface ReviewRepositoryMongodb extends CrudRepository<ReviewMongo, String> {
    @Query("{ 'sku' : ?0, 'approvalStatus': ?1 }")
    List<ReviewMongo> findBySkuAndStatus(String sku, String status);

    @Query("{ 'userId' : ?0 }")
    List<ReviewMongo> findByUser(String userId);

    @Query("{ 'approvalStatus' : 'Pending' }")
    List<ReviewMongo> findPending();

    @Query("{ 'approvalStatus' : 'Approved' }")
    List<ReviewMongo> findApproved();
}
