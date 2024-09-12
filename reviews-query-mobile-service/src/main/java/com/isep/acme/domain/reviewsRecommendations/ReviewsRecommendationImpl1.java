package com.isep.acme.domain.reviewsRecommendations;

import com.isep.acme.applicationServices.interfaces.domain.IReviewsRecommendations;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.reviews.recommendation", havingValue = "implementation1")
public class ReviewsRecommendationImpl1 implements IReviewsRecommendations {

    private final IReviewRepository reviewsRepository;

    @Override
    public List<Review> getReviewsRecommendation(String user_Id, int limiter) {
        List<Review> reviews = reviewsRepository.findApproved();

        reviews = reviews.stream().filter(it -> it.getVotes().keySet().size() >= 4 &&
                        upVotesRate(it.getVotes()) >= 60).sorted(Comparator
                        .<Review, Integer>comparing(review -> countUpVotes(review.getVotes()))
                        .thenComparingDouble(review -> upVotesRate(review.getVotes())))
                .collect(Collectors.toList());
        Collections.reverse(reviews);
        return reviews;
    }

    private int countUpVotes(Map<String, VoteEnum> map) {
        int upVotesCounter = 0;

        for (VoteEnum voteEnum : map.values()) {
            if (voteEnum.equals(VoteEnum.UpVote)) {
                upVotesCounter++;
            }
        }

        return upVotesCounter;
    }

    private double upVotesRate(Map<String, VoteEnum> map) {
        int upVotesCounter = countUpVotes(map);
        return (((double) upVotesCounter / map.size()) * 100);
    }
}
