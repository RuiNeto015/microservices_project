package com.isep.acme.adapters.database.repositories.mongodb.mappers;


import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import com.isep.acme.domain.enums.VoteEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public class ReviewMapper {

    public Review toDomainObject(ReviewMongo review, Map<String, VoteEnum> votes) {
        return new Review(review.getId(), ApprovalStatusEnum.valueOf(review.getApprovalStatus()),
                review.getText(), review.getReport(), review.getPublishingDate(), review.getFunFact(),
                review.getRating(), review.getUserId(), review.getSku(), votes, review.getNumApprovals());
    }

    public ReviewMongo toDatabaseObject(Review review) {
        return new ReviewMongo(review.getId(),
                review.getApprovalStatus().name(), review.getText(), review.getReport(), review.getPublishingDate(),
                review.getFunFact(), review.getRating(), review.getSku(), review.getUserId(), review.getNumApprovals());
    }
}
