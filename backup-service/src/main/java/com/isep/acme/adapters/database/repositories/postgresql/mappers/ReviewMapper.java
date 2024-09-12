package com.isep.acme.adapters.database.repositories.postgresql.mappers;

import com.isep.acme.adapters.database.models.postgresql.ProductPg;
import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import com.isep.acme.adapters.database.models.postgresql.UserPg;
import com.isep.acme.adapters.database.models.postgresql.VotePg;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import com.isep.acme.domain.enums.VoteEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class ReviewMapper {

    public Review toDomainObject(ReviewPg review) {

        if (review.getVotes()!=null) {
            return new Review(review.getId(), ApprovalStatusEnum.valueOf(review.getApprovalStatus()),
                    review.getText(), review.getReport(), review.getPublishingDate(), review.getFunFact(),
                    review.getRating(), review.getUser().getId(), review.getProduct().getSku(),
                    this.votesToDomain(review.getVotes()), review.getNumApprovals());
        } else {
            return new Review(review.getId(), ApprovalStatusEnum.valueOf(review.getApprovalStatus()),
                    review.getText(), review.getReport(), review.getPublishingDate(), review.getFunFact(),
                    review.getRating(), review.getUser().getId(), review.getProduct().getSku(),
                    new HashMap<>(), review.getNumApprovals());
        }
    }

    public ReviewPg toDatabaseObject(Review review, ProductPg product,
                                     UserPg user) {
        return new ReviewPg(review.getId(),
                review.getApprovalStatus().name(), review.getText(), review.getReport(), review.getPublishingDate(),
                review.getFunFact(), review.getRating(), product, user, review.getNumApprovals());
    }

    public Map<String, VoteEnum> votesToDomain(List<VotePg> databaseVotes) {
        Map<String, VoteEnum> domainVotes = new HashMap<>();

        for (VotePg v : databaseVotes) {
            domainVotes.put(v.getUser().getId(), VoteEnum.valueOf(v.getVote()));
        }
        return domainVotes;
    }
}
