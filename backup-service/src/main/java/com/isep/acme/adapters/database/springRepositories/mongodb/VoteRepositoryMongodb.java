package com.isep.acme.adapters.database.springRepositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.VoteMongo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.repository.MongoRepository;

@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "mongodb")
public interface VoteRepositoryMongodb extends MongoRepository<VoteMongo, String> {

    void deleteAllByReviewId(String reviewId);

}
