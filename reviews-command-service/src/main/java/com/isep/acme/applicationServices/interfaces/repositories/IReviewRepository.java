package com.isep.acme.applicationServices.interfaces.repositories;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.domain.aggregates.review.Review;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IReviewRepository {

    @Transactional
    void create(Review review) throws DatabaseException;

    @Transactional
    void update(Review review) throws DatabaseException;

    @Transactional
    void delete(String id) throws DatabaseException;

    @Transactional
    Review findById(String id);

    @Transactional
    List<Review> findBySku(String sku);

    @Transactional
    List<Review> findByUser(String userId);

    @Transactional
    Boolean exists(String id);

    @Transactional
    List<Review> findWhereUserHasVoted(String userId);

    @Transactional
    List<Review> findApproved();
}
