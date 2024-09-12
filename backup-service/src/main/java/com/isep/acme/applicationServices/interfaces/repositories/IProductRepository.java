package com.isep.acme.applicationServices.interfaces.repositories;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.domain.aggregates.product.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductRepository {

    @Transactional
    void create(Product product) throws DatabaseException;

    @Transactional
    void update(Product product) throws DatabaseException;

    @Transactional
    Product findBySku(String sku);

    @Transactional
    List<Product> findAll();

    @Transactional
    Product delete(String sku) throws DatabaseException;
}
