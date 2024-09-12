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
    Boolean exists(String id);

    @Transactional
    void delete(String id) throws DatabaseException;

    @Transactional
    Review findById(String id);

}
