package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import com.isep.acme.adapters.database.models.postgresql.UserPg;
import com.isep.acme.adapters.database.models.postgresql.VoteKeyPg;
import com.isep.acme.adapters.database.models.postgresql.VotePg;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.postgresql.mappers.ReviewMapper;
import com.isep.acme.adapters.database.springRepositories.postgresql.ReviewRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.UserRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.VoteRepositoryPostgresql;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.VoteEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("ReviewRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class ReviewPgRepositoryImpl implements IReviewRepository {

    private final ReviewRepositoryPostgresql reviewRepository;
    private final UserRepositoryPostgresql userRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewPgRepositoryImpl(
            ReviewRepositoryPostgresql reviewRepository,
            UserRepositoryPostgresql userRepository,
            ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public void create(Review review) throws DatabaseException {
        Optional<UserPg> user = this.userRepository.findById(review.getUserId());

        if (user.isEmpty()) {
            throw new DatabaseException("User not found.");
        }

        ReviewPg r = this.reviewMapper.toDatabaseObject(review, user.get());
        this.reviewRepository.save(r);
    }

    @Override
    public void update(Review review) throws DatabaseException {
        Optional<ReviewPg> matchingReview = this.reviewRepository.findById(review.getId());

        if (matchingReview.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.reviewRepository.save(this.reviewMapper.toDatabaseObject(review, matchingReview.get().getUser()));
    }

    @Override
    public Boolean exists(String id) {
        Optional<ReviewPg> review = this.reviewRepository.findById(id);
        return review.isPresent();
    }

    @Override
    public void delete(String id) throws DatabaseException {
        Optional<ReviewPg> matchingReview = this.reviewRepository.findById(id);

        if (matchingReview.isEmpty()) {
            throw new DatabaseException("Review does not exist.");
        }

        this.reviewRepository.delete(matchingReview.get());
    }

    public Review findById(String id) {
        Optional<ReviewPg> reviewData = this.reviewRepository.findById(id);

        return reviewData.map(this.reviewMapper::toDomainObject).orElse(null);
    }

    @Override
    public List<Review> findBySkuAndStatus(String sku, String status) {
        List<ReviewPg> reviews = this.reviewRepository.findBySkuAndStatus(sku, status);

        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findBySku(String sku) {
        List<ReviewPg> reviews = this.reviewRepository.findBySku(sku);

        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findByUser(String userId) {
        List<ReviewPg> reviews = this.reviewRepository.findByUser(userId);
        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findAll() {
        List<ReviewPg> reviews
                = (List<ReviewPg>) this.reviewRepository.findAll();

        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findPending() {
        List<ReviewPg> reviews = this.reviewRepository.findPending();
        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findWhereUserHasVoted(String userId) {
        Optional<UserPg> user = this.userRepository.findById(userId);
        List<ReviewPg> reviews = new ArrayList<>();

        if (user.isPresent()) {
            List<VotePg> votes = user.get().getVotes();

            for (VotePg v : votes) {
                reviews.add(v.getReview());
            }
        }
        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findApproved() {
        return reviewsToDomain(this.reviewRepository.findApproved());
    }

    private List<Review> reviewsToDomain(List<ReviewPg> databaseReviews) {
        List<Review> domainReviews = new ArrayList<>();

        for (ReviewPg r : databaseReviews) {
            domainReviews.add(this.reviewMapper.toDomainObject(r));
        }
        return domainReviews;
    }
}
