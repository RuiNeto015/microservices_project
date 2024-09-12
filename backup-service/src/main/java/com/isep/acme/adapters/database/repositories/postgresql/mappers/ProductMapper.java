package com.isep.acme.adapters.database.repositories.postgresql.mappers;

import com.isep.acme.adapters.database.models.postgresql.ProductPg;
import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.aggregates.product.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(value="spring.repositories.targetPackage", havingValue = "postgresql")
public class ProductMapper {

    public Product toDomainObject(ProductPg product) {
        return new Product(product.getSku(), ApprovalStatusEnum.valueOf(product.getApprovalStatus()),
                product.getDesignation(), product.getDescription(), product.getAggregatedRating(),
                this.reviewsToDomain(product.getReviews()), product.getNumApprovals());
    }

    public ProductPg toDatabaseObject(Product product) {
        return new ProductPg(product.getSku(), product.getApprovalStatus().name(),
                product.getDesignation(), product.getDescription(), product.getAggregatedRating(), product.getNumApprovals());
    }

    public List<Review> reviewsToDomain(List<ReviewPg> databaseReviews) {
        List<Review> reviews = new ArrayList<>();

        for(ReviewPg databaseReview : databaseReviews) {
            Review domainReview = new Review(databaseReview.getId(),
                    ApprovalStatusEnum.valueOf(databaseReview.getApprovalStatus()), databaseReview.getText(),
                    databaseReview.getReport(), databaseReview.getPublishingDate(), databaseReview.getFunFact(),
                    databaseReview.getRating(), databaseReview.getUser().getId(), databaseReview.getNumApprovals());
            reviews.add(domainReview);
        }
        return reviews;
    }
}
