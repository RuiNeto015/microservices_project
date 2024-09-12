package com.isep.acme.applicationServices.interfaces.repositories;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.aggregates.votes.Vote;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IVoteRepository {

    @Transactional
    void create(Vote vote) throws DatabaseException;

    Vote findByUserAndReview(String userId, String reviewId);

    @Transactional
    void deleteAllByReview(String reviewId);

    @Transactional
    List<Vote> findByReview(String reviewId);
}
