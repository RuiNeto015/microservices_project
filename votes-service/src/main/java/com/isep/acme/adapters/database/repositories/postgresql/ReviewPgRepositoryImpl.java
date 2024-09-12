package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.postgresql.mappers.ReviewMapper;
import com.isep.acme.adapters.database.springRepositories.postgresql.ReviewRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.UserRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.VoteRepositoryPostgresql;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ReviewRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class ReviewPgRepositoryImpl implements IReviewRepository {

    private final ReviewRepositoryPostgresql reviewRepository;
    private final UserRepositoryPostgresql userRepository;
    private final VoteRepositoryPostgresql voteRepository;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewPgRepositoryImpl(
            ReviewRepositoryPostgresql reviewRepository,
            UserRepositoryPostgresql userRepository,
            VoteRepositoryPostgresql voteRepository,
            ReviewMapper reviewMapper) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public void create(Review review) throws DatabaseException {
        ReviewPg r = this.reviewMapper.toDatabaseObject(review);
        this.reviewRepository.save(r);
    }

    @Override
    public void delete(String id) throws DatabaseException {
        Optional<ReviewPg> matchingReview = this.reviewRepository.findById(id);

        if (matchingReview.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.reviewRepository.delete(matchingReview.get());
    }

    @Override
    public void update(Review review) throws DatabaseException {
        Optional<ReviewPg> matchingReview = this.reviewRepository.findById(review.getId());

        if (matchingReview.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }


        this.reviewRepository.save(this.reviewMapper.toDatabaseObject(review));
    }

    @Override
    public Boolean exists(String id) {
        Optional<ReviewPg> review = this.reviewRepository.findById(id);
        return review.isPresent();
    }

    public Review findById(String id) {
        Optional<ReviewPg> reviewData
                = this.reviewRepository.findById(id);

        return reviewData.map(this.reviewMapper::toDomainObject).orElse(null);
    }

}
