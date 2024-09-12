package com.isep.acme.domain.aggregates.review;

import lombok.Getter;

@Getter
public class Rating {

    private double rating;

    public Rating(double rating) {
        this.setRating(rating);
    }

    private void setRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Rating must be a value between 0 and 5.");
        }
        this.rating = rating;
    }
}
