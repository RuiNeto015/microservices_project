package com.isep.acme.adapters.database.repositories.postgresql.mappers;

import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class ReviewMapper {

    public Review toDomainObject(ReviewPg review) {
        return new Review(review.getId(), ApprovalStatusEnum.valueOf(review.getApprovalStatus()),
                review.getNumApprovals(), review.getUserId());
    }

    public ReviewPg toDatabaseObject(Review review) {
        return new ReviewPg(review.getId(),
                review.getApprovalStatus().name(), review.getNumApprovals(), review.getUserId());
    }

}
