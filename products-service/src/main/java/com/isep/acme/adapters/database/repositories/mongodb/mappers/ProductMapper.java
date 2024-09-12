package com.isep.acme.adapters.database.repositories.mongodb.mappers;


import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
@Component
public class ProductMapper {

    public Product toDomainObject(ProductMongo product) {
        return new Product(product.getSku(), ApprovalStatusEnum.valueOf(product.getApprovalStatus()),
                product.getDesignation(), product.getDescription(), product.getNumApprovals(),
                product.getAggregatedRating());
    }

    public ProductMongo toDatabaseObject(
            Product product
    ) {
        return new ProductMongo(product.getSku().getSku(), product.getApprovalStatus().name(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription(),
                product.getAggregatedRating(), product.getNumApprovals());
    }
}
