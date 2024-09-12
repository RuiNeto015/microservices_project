package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import com.isep.acme.adapters.database.models.postgresql.ProductPg;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.springRepositories.postgresql.ProductRepositoryPostgresql;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ProductRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
@RequiredArgsConstructor
public class ProductPgRepositoryImpl implements IProductRepository {

    private final ProductRepositoryPostgresql productRepository;

    @Override
    public void create(Product product) throws DatabaseException {
        Optional<ProductPg> matchingProduct = this.productRepository.findBySku(product.getSku());

        if (matchingProduct.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        ProductPg productPg = new ProductPg();
        productPg.setSku(product.getSku());
        productPg.setNumApprovals(product.getNumApprovals());
        productPg.setApprovalStatus(product.getApprovalStatus().toString());
        this.productRepository.save(productPg);
    }

    @Override
    public Boolean exists(String sku) {
        Optional<ProductPg> product = this.productRepository.findBySku(sku);
        return product.isPresent();
    }

    @Override
    public void update(Product product) throws DatabaseException {
        Optional<ProductPg> matchingProduct = this.productRepository.findById(product.getSku());

        if (matchingProduct.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        ProductPg updated = matchingProduct.get();
        updated.setNumApprovals(product.getNumApprovals());
        updated.setApprovalStatus(product.getApprovalStatus().toString());
        this.productRepository.save(updated);
    }

    @Override
    public void delete(String sku) throws DatabaseException {
        Optional<ProductPg> product = this.productRepository.findBySku(sku);

        if (product.isEmpty()) {
            throw new DatabaseException("The product not exists.");
        }

        this.productRepository.deleteBySku(sku);
    }

    @Override
    public Product findBySku(String sku) {
        Optional<ProductPg> product = this.productRepository.findBySku(sku);

        return product.map(productMongo -> new Product(productMongo.getSku(), ApprovalStatusEnum.valueOf(productMongo.getApprovalStatus()),
                productMongo.getNumApprovals())).orElse(null);

    }
}
