package com.isep.acme.adapters.database.repositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import com.isep.acme.adapters.database.models.mongodb.ReviewMongo;
import com.isep.acme.adapters.database.models.mongodb.UserMongo;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.mongodb.mappers.ProductMapper;
import com.isep.acme.adapters.database.springRepositories.mongodb.ReviewRepositoryMongodb;
import com.isep.acme.adapters.database.springRepositories.mongodb.UserRepositoryMongodb;
import com.isep.acme.adapters.database.springRepositories.mongodb.ProductRepositoryMongodb;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.domain.aggregates.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("ProductRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public class ProductMongoRepositoryImpl implements IProductRepository {

    private final ProductRepositoryMongodb productRepository;

    private final ReviewRepositoryMongodb reviewRepository;

    private final UserRepositoryMongodb userRepository;
    private final ProductMapper mapper;

    @Autowired
    public ProductMongoRepositoryImpl(
            ProductRepositoryMongodb productRepository,
            ReviewRepositoryMongodb reviewRepository,
            UserRepositoryMongodb userRepository,
            ProductMapper mapper
    ) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public void create(Product product) throws DatabaseException {
        Optional<ProductMongo> productById = this.productRepository.findById(product.getSku());

        if (productById.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public void update(Product product) throws DatabaseException {
        Optional<ProductMongo> matchingProduct = this.productRepository.findById(product.getSku());

        if (matchingProduct.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public List<Product> findAll() {
        List<ProductMongo> databaseProducts = (List<ProductMongo>) this.productRepository.findAll();

        List<Product> domainProducts = new ArrayList<>();

        for (ProductMongo p : databaseProducts) {
            List<ReviewMongo> reviewMongo = this.reviewRepository.findBySku(p.getSku());
            domainProducts.add(this.mapper.toDomainObject(p, reviewMongo));
        }
        return domainProducts;
    }

    @Override
    public Product findBySku(String sku) {
        Optional<ProductMongo> matchingProduct = this.productRepository.findById(sku);

        if (matchingProduct.isEmpty()) {
            return null;
        }

        List<ReviewMongo> reviewsMongo = this.reviewRepository.findBySku(sku);
        return this.mapper.toDomainObject(matchingProduct.get(), reviewsMongo);
    }

    @Override
    public Product delete(String sku) throws DatabaseException {
        Optional<ProductMongo> matchingProductData = this.productRepository.findById(sku);

        if (matchingProductData.isPresent()) {
            List<ReviewMongo> productReviews = this.reviewRepository.findBySku(sku);

            for (ReviewMongo r : productReviews) {
                Optional<UserMongo> user = this.userRepository.findById(r.getUserId());

                if (user.isPresent()) {
                    user.get().getReviews().remove(r);
                    user.get().getVotes().remove(r.getId());
                    this.userRepository.save(user.get());
                }
            }
            this.reviewRepository.deleteAll(productReviews);
            this.productRepository.delete(matchingProductData.get());
            return this.mapper.toDomainObject(matchingProductData.get(), productReviews);
        } else {
            throw new DatabaseException("Product reference not found");
        }
    }
}
