package com.isep.acme.adapters.database.repositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import com.isep.acme.adapters.database.models.mongodb.UserMongo;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.mongodb.mappers.ReviewMapper;
import com.isep.acme.adapters.database.springRepositories.mongodb.ReviewRepositoryMongodb;
import com.isep.acme.adapters.database.springRepositories.mongodb.UserRepositoryMongodb;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.VoteEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("ReviewRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public class ReviewMongoRepositoryImpl implements IReviewRepository {

    private final ReviewRepositoryMongodb reviewRepository;

    private final UserRepositoryMongodb userRepository;

    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewMongoRepositoryImpl(
            ReviewRepositoryMongodb reviewRepository,
            UserRepositoryMongodb userRepository,
            ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public void create(Review review) throws DatabaseException {
        ReviewMongo r = this.reviewMapper.toDatabaseObject(review);
        this.reviewRepository.save(r);
    }

    @Override
    public void update(Review review) throws DatabaseException {
        ReviewMongo r = this.reviewMapper.toDatabaseObject(review);
        this.reviewRepository.save(r);
    }

    @Override
    public Boolean exists(String id) {
        Optional<ReviewMongo> review = this.reviewRepository.findById(id);
        return review.isPresent();
    }

    @Override
    public void delete(String id) throws DatabaseException {
        this.reviewRepository.deleteById(id);
    }

    public Review findById(String id) {
        Optional<ReviewMongo> reviewData = this.reviewRepository.findById(id);

        return reviewData.map(this.reviewMapper::toDomainObject).orElse(null);
    }


}
