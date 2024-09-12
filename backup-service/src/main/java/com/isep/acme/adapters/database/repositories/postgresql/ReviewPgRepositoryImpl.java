package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.*;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.postgresql.mappers.ReviewMapper;
import com.isep.acme.adapters.database.springRepositories.postgresql.ProductRepositoryPostgresql;
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
    private final ProductRepositoryPostgresql productRepository;
    private final UserRepositoryPostgresql userRepository;
    private final VoteRepositoryPostgresql voteRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewPgRepositoryImpl(
            ReviewRepositoryPostgresql reviewRepository,
            UserRepositoryPostgresql userRepository,
            VoteRepositoryPostgresql voteRepository,
            ProductRepositoryPostgresql productRepository,
            ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.reviewMapper = reviewMapper;
        this.productRepository = productRepository;
    }

    @Override
    public List<Review> findBySku(String sku) {
        List<ReviewPg> reviews = this.reviewRepository.findBySku(sku);

        return reviewsToDomain(reviews);
    }

    @Override
    public void create(Review review) throws DatabaseException {
        Optional<UserPg> user = this.userRepository.findById(review.getUserId());
        Optional<ProductPg> productPg = this.productRepository.findBySku(review.getSku());

        if (user.isEmpty()) {
            throw new DatabaseException("User not found.");
        }

        if (productPg.isEmpty()) {
            throw new DatabaseException("Product not found.");
        }

        ReviewPg r = this.reviewMapper.toDatabaseObject(review, productPg.get(), user.get());
        this.reviewRepository.save(r);
    }


    @Override
    public void update(Review review) throws DatabaseException {
        Optional<ReviewPg> matchingReview = this.reviewRepository.findById(review.getId());

        if (matchingReview.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.votesListUpdate(matchingReview.get(), this.reviewMapper.votesToDomain(matchingReview.get().getVotes()),
                review.getVotes());
        this.reviewRepository.save(this.reviewMapper.toDatabaseObject(review, matchingReview.get().getProduct(),
                matchingReview.get().getUser()));
    }

    @Override
    public void delete(String id) throws DatabaseException {
        Optional<ReviewPg> matchingReview = this.reviewRepository.findById(id);

        if (matchingReview.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.reviewRepository.delete(matchingReview.get());
    }

    private void votesListUpdate(ReviewPg review,
                                 Map<String, VoteEnum> preStateVotes, Map<String, VoteEnum> postStateVotes) {

        for (String key : preStateVotes.keySet()) {
            if (!postStateVotes.containsKey(key)) { // delete vote
                this.voteRepository.delete(this.voteRepository.getById(new VoteKeyPg(key, review.getId())));
            }

            if (postStateVotes.containsKey(key) && postStateVotes.get(key) != preStateVotes.get(key)) {
                VotePg vote = this.voteRepository.getById(new VoteKeyPg(key, review.getId()));
                this.voteRepository.save(new VotePg(vote.getUser(), review, postStateVotes.get(key).name()));
            }
        }

        for (String key : postStateVotes.keySet()) {
            if (!preStateVotes.containsKey(key)) { // add vote
                Optional<UserPg> user = this.userRepository.findById(key);

                user.ifPresent(value ->
                        this.voteRepository.save(new VotePg(value, review, postStateVotes.get(key).name())));
            }
        }
    }

    public Review findById(String id) {
        Optional<ReviewPg> reviewData
                = this.reviewRepository.findById(id);

        return reviewData.map(this.reviewMapper::toDomainObject).orElse(null);
    }

    @Override
    public List<Review> findBySkuAndStatus(String sku, String status) {
        List<ReviewPg> reviews
                = this.reviewRepository.findBySkuAndStatus(sku, status);

        return reviewsToDomain(reviews);
    }

    @Override
    public List<Review> findByUser(String userId) {
        List<ReviewPg> reviews
                = this.reviewRepository.findByUser(userId);
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
