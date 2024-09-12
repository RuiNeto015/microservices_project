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
        updateVotes(r, review.getVotes());
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

    private void updateVotes(ReviewMongo review, Map<String, VoteEnum> votes)
            throws DatabaseException {

        for (String userId : votes.keySet()) {
            Optional<UserMongo> userData = this.userRepository.findById(userId);

            if (userData.isEmpty()) {
                throw new DatabaseException("User reference not found");
            }

            userData.get().getVotes().put(review.getId(), votes.get(userId).name());
            this.userRepository.save(userData.get());
        }
    }

    public Review findById(String id) {
        Optional<ReviewMongo> reviewData
                = this.reviewRepository.findById(id);

        return reviewData.map(review -> this.reviewMapper.toDomainObject(review, joinVotes(review))).orElse(null);
    }

    @Override
    public List<Review> findBySkuAndStatus(String sku, String status) {
        List<ReviewMongo> reviews
                = this.reviewRepository.findBySkuAndStatus(sku, status);

        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findBySku(String sku) {
        return null;
    }

    @Override
    public List<Review> findByUser(String userId) {
        List<ReviewMongo> reviews = this.reviewRepository.findByUser(userId);
        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findAll() {
        List<ReviewMongo> reviews
                = (List<ReviewMongo>) this.reviewRepository.findAll();

        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findPending() {
        List<ReviewMongo> reviews = this.reviewRepository.findPending();
        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findWhereUserHasVoted(String userId) {
        Optional<UserMongo> user = this.userRepository.findById(userId);
        List<ReviewMongo> reviews = new ArrayList<>();

        if (user.isPresent()) {
            Set<String> reviewIds = user.get().getVotes().keySet();

            for (String reviewId : reviewIds) {
                Optional<ReviewMongo> review = this.reviewRepository.findById(reviewId);
                review.ifPresent(reviews::add);
            }
        }
        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findApproved() {
        return reviewsToDomain(this.reviewRepository.findApproved());
    }

    private List<Review> reviewsToDomain(List<ReviewMongo> databaseReviews) {
        List<Review> domainReviews = new ArrayList<>();
        for (ReviewMongo r : databaseReviews) {
            domainReviews.add(this.reviewMapper.toDomainObject(r, joinVotes(r)));
        }

        return domainReviews;
    }

    private Map<String, VoteEnum> joinVotes(ReviewMongo r) {
        Map<String, VoteEnum> votes = new HashMap<>();
        List<UserMongo> users = (List<UserMongo>) this.userRepository.findAll();

        for (UserMongo u : users) {
            if (u.getVotes().containsKey(r.getId())) {
                votes.put(u.getId(), VoteEnum.valueOf(u.getVotes().get(r.getId())));
            }
        }
        return votes;
    }
}
