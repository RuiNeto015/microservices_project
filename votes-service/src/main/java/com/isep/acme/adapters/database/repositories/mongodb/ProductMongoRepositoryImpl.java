package com.isep.acme.adapters.database.repositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.springRepositories.mongodb.ProductRepositoryMongodb;
import com.isep.acme.adapters.database.springRepositories.mongodb.ReviewRepositoryMongodb;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("ProductRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
@RequiredArgsConstructor
public class ProductMongoRepositoryImpl implements IProductRepository {

    private final ProductRepositoryMongodb productRepository;

    private final ReviewRepositoryMongodb reviewRepository;

    @Override
    public void create(String sku) throws DatabaseException {
        Optional<ProductMongo> product = this.productRepository.findBySku(sku);

        if (product.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        this.productRepository.save(new ProductMongo(sku));
    }

    @Override
    public Boolean exists(String sku) {
        Optional<ProductMongo> product = this.productRepository.findBySku(sku);
        return product.isPresent();
    }

    @Override
    public void delete(String sku) throws DatabaseException {
        Optional<ProductMongo> product = this.productRepository.findBySku(sku);

        if (product.isEmpty()) {
            throw new DatabaseException("Product not exists.");
        }

        this.productRepository.deleteBySku(sku);

        //TODO : HANDLE THE CASE IF THE PRODUCT HAS REVIEWS
    }
}
