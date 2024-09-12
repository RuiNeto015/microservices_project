package com.isep.acme.domain.reviewsRecommendations;

import com.isep.acme.applicationServices.interfaces.domain.IReviewsRecommendations;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.reviews.recommendation", havingValue = "implementation2")
public class ReviewsRecommendationImpl2 implements IReviewsRecommendations {

    private final IReviewRepository reviewsRepository;

    @Override
    public List<Review> getReviewsRecommendation(String userId, int limiter) {
        Map<String, Integer> relatableUsersTracker = new HashMap<>();
        List<Review> reviews = reviewsRepository.findWhereUserHasVoted(userId);
        List<Review> reviewsToReturn = new ArrayList<>();
        int totalVotes = reviews.size();

        for (Review r : reviews) {
            Map<String, VoteEnum> votes = r.getVotes();
            VoteEnum userVote = votes.get(userId);

            for (Map.Entry<String, VoteEnum> vote : votes.entrySet()) {
                if (userVote.equals(vote.getValue()) && !Objects.equals(vote.getKey(), userId)) {
                    if (!relatableUsersTracker.containsKey(vote.getKey())) {
                        relatableUsersTracker.put(vote.getKey(), 1);
                    } else {
                        relatableUsersTracker.put(vote.getKey(), relatableUsersTracker.get((vote.getKey())) + 1);
                    }
                }
            }
        }

        for (Map.Entry<String, Integer> user : relatableUsersTracker.entrySet()) {
            if(criteriaIsMet(user.getValue(), totalVotes)) {
                reviewsToReturn.addAll(this.reviewsRepository.findByUser(user.getKey()));
            }
        }
        return reviewsToReturn;
    }

    private boolean criteriaIsMet(int numOfEqualVotes, int totalNumOfVotes) {
        double percentage = ((double) numOfEqualVotes / totalNumOfVotes) * 100;
        return percentage >= 50;
    }
}
