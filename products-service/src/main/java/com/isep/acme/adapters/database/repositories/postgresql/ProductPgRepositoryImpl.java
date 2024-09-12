package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.ProductPg;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.repositories.postgresql.mappers.ProductMapper;
import com.isep.acme.adapters.database.springRepositories.postgresql.ProductRepositoryPostgresql;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.domain.aggregates.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("ProductRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public class ProductPgRepositoryImpl implements IProductRepository {

    private final ProductRepositoryPostgresql productRepository;

    private final ProductMapper mapper;

    @Autowired
    public ProductPgRepositoryImpl(ProductRepositoryPostgresql productRepository, ProductMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public void create(Product product) throws DatabaseException {
        Optional<ProductPg> productById = this.productRepository.findBySku(product.getSku().getSku());

        if (productById.isPresent()) {
            throw new DatabaseException("Duplicate id violation.");
        }

        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public void update(Product product) throws DatabaseException {
        Optional<ProductPg> matchingProduct = this.productRepository.findBySku(product.getSku().getSku());

        if (matchingProduct.isEmpty()) {
            throw new DatabaseException("Update target does not exist.");
        }

        this.productRepository.save(this.mapper.toDatabaseObject(product));
    }

    @Override
    public Product findBySku(String sku) {
        Optional<ProductPg> product = this.productRepository.findBySku(sku);
        return product.map(this.mapper::toDomainObject).orElse(null);
    }

    @Override
    public List<Product> findAll() {
        List<ProductPg> databaseProducts = (List<ProductPg>) this.productRepository.findAll();

        List<Product> domainProducts = new ArrayList<>();

        for (ProductPg p : databaseProducts) {
            domainProducts.add(this.mapper.toDomainObject(p));
        }
        return domainProducts;
    }

    @Override
    public List<Product> findByDesignation(String designation) {
        List<ProductPg> databaseProducts = this.productRepository.findByDesignation(designation);

        List<Product> domainProducts = new ArrayList<>();

        for (ProductPg p : databaseProducts) {
            domainProducts.add(this.mapper.toDomainObject(p));
        }
        return domainProducts;
    }

    @Override
    public Product delete(String sku) throws DatabaseException {
        Optional<ProductPg> matchingProductData = this.productRepository.findBySku(sku);

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
