package com.isep.acme.adapters.database.repositories.postgresql.mappers;

import com.isep.acme.adapters.database.models.postgresql.ProductPg;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class ProductMapper {

    public Product toDomainObject(ProductPg product) {
        return new Product(product.getSku(), ApprovalStatusEnum.valueOf(product.getApprovalStatus()),
                product.getDesignation(), product.getDescription(), product.getNumApprovals(),
                product.getAggregatedRating());
    }

    public ProductPg toDatabaseObject(Product product) {
        return new ProductPg(product.getSku().getSku(), product.getApprovalStatus().name(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription(),
                product.getAggregatedRating(), product.getNumApprovals());
    }
}
