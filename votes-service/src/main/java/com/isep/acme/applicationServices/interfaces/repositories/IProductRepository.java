package com.isep.acme.applicationServices.interfaces.repositories;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import org.springframework.transaction.annotation.Transactional;

public interface IProductRepository {

    @Transactional
    void create(String sku) throws DatabaseException;

    @Transactional
    Boolean exists(String sku);

    @Transactional
    void delete(String sku) throws DatabaseException;
}
