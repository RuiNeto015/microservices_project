package com.isep.acme.adapters.database.repositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.ProductMongo;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.mongodb.mappers.ProductMapper;
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

    private final ProductMapper mapper;

    @Autowired
    public ProductMongoRepositoryImpl(ProductRepositoryMongodb productRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public void create(Product product) throws DatabaseException {
        Optional<ProductMongo> productById = this.productRepository.findBySku(product.getSku().getSku());

        if (productById.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public void update(Product product) throws DatabaseException {
        Optional<ProductMongo> matchingProduct = this.productRepository.findBySku(product.getSku().getSku());

        if (matchingProduct.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public Product findBySku(String sku) {
        Optional<ProductMongo> product = this.productRepository.findBySku(sku);
        return product.map(this.mapper::toDomainObject).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        List<ProductMongo> databaseProducts = (List<ProductMongo>) this.productRepository.findAll();

        List<Product> domainProducts = new ArrayList<>();

        for (ProductMongo p : databaseProducts) {
            domainProducts.add(this.mapper.toDomainObject(p));
        }
        return domainProducts;
    }

    @Override
    public List<Product> findByDesignation(String designation) {
        List<ProductMongo> databaseProducts = this.productRepository.findByDesignation(designation);

        List<Product> domainProducts = new ArrayList<>();

        for (ProductMongo p : databaseProducts) {
            domainProducts.add(this.mapper.toDomainObject(p));
        }
        return domainProducts;
    }

    @Override
    public Product delete(String sku) throws DatabaseException {
        Optional<ProductMongo> matchingProductData = this.productRepository.findBySku(sku);

        if (matchingProductData.isPresent()) {
            this.productRepository.delete(matchingProductData.get());
            return this.mapper.toDomainObject(matchingProductData.get());
        } else {
            throw new DatabaseException("Product reference not found");
        }
    }

    @Override
    public Boolean exists(String sku) {
        return this.productRepository.findBySku(sku).isPresent();
    }
}
