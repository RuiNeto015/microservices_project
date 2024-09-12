package com.isep.acme.applicationServices.interfaces.domain;

import com.isep.acme.domain.aggregates.review.Review;

import java.util.List;

public interface IReviewsRecommendations {

    List<Review> getReviewsRecommendation(String user_Id, int limiter);
}
