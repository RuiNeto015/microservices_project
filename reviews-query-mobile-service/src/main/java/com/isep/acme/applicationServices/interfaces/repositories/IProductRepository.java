package com.isep.acme.applicationServices.interfaces.repositories;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.domain.aggregates.product.Product;
import org.springframework.transaction.annotation.Transactional;

public interface IProductRepository {

    @Transactional
    void create(Product product) throws DatabaseException;

    @Transactional
    Boolean exists(String sku);

    @Transactional
    void update(Product product) throws DatabaseException;

    @Transactional
    void delete(String sku) throws DatabaseException;

    @Transactional
    Product findBySku(String sku);
}
