package com.isep.acme.adapters.database.repositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.springRepositories.mongodb.ProductRepositoryMongodb;
import com.isep.acme.adapters.database.springRepositories.mongodb.ReviewRepositoryMongodb;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ProductRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
@RequiredArgsConstructor
public class ProductMongoRepositoryImpl implements IProductRepository {

    private final ProductRepositoryMongodb productRepository;

    @Override
    public void create(Product product) throws DatabaseException {
        Optional<ProductMongo> matchingProduct = this.productRepository.findBySku(product.getSku());

        if (matchingProduct.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        ProductMongo productMongo = new ProductMongo(product.getSku(), product.getApprovalStatus().toString(), product.getNumApprovals());
        this.productRepository.save(productMongo);
    }

    @Override
    public Boolean exists(String sku) {
        Optional<ProductMongo> product = this.productRepository.findBySku(sku);
        return product.isPresent();
    }

    @Override
    public void update(Product product) throws DatabaseException {
        Optional<ProductMongo> matchingProduct = this.productRepository.findById(product.getSku());

        if (matchingProduct.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        ProductMongo updated = matchingProduct.get();
        updated.setNumApprovals(product.getNumApprovals());
        updated.setApprovalStatus(product.getApprovalStatus().toString());
        this.productRepository.save(updated);
    }

    @Override
    public void delete(String sku) throws DatabaseException {
        Optional<ProductMongo> product = this.productRepository.findBySku(sku);

        if (product.isEmpty()) {
            throw new DatabaseException("Product not exists.");
        }

        this.productRepository.deleteBySku(sku);
    }

    @Override
    public Product findBySku(String sku) {
        Optional<ProductMongo> product = this.productRepository.findBySku(sku);

        return product.map(productMongo -> new Product(productMongo.getSku(), ApprovalStatusEnum.valueOf(productMongo.getApprovalStatus()),
                productMongo.getNumApprovals())).orElse(null);

    }
}
