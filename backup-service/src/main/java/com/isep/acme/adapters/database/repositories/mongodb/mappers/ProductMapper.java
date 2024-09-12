package com.isep.acme.adapters.database.repositories.mongodb.mappers;


import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.aggregates.product.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
@Component
public class ProductMapper {

    public Product toDomainObject(ProductMongo product, List<ReviewMongo> reviews) {
        return new Product(product.getSku(), ApprovalStatusEnum.valueOf(product.getApprovalStatus()),
                product.getDesignation(), product.getDescription(), product.getAggregatedRating(),
                this.reviewsToDomain(reviews), product.getNumApprovals());
    }

    public ProductMongo toDatabaseObject(Product product) {
        return new ProductMongo(product.getSku(), product.getApprovalStatus().name(), product.getDesignation(),
                product.getDescription(), product.getAggregatedRating(), product.getNumApprovals());
    }

    public List<Review> reviewsToDomain(List<ReviewMongo> databaseReviews) {
        List<com.isep.acme.domain.aggregates.product.Review> reviews = new ArrayList<>();

        for (ReviewMongo databaseReview : databaseReviews) {
            Review domainReview = new Review(databaseReview.getId(),
                    ApprovalStatusEnum.valueOf(databaseReview.getApprovalStatus()), databaseReview.getText(),
                    databaseReview.getReport(), databaseReview.getPublishingDate(), databaseReview.getFunFact(),
                    databaseReview.getRating(), databaseReview.getUserId(), databaseReview.getNumApprovals());
            reviews.add(domainReview);
        }
        return reviews;
    }
}
