package com.isep.acme.adapters.database.springRepositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.ProductPg;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql")
public interface ProductRepositoryPostgresql extends CrudRepository<ProductPg, String> {

    @Query("SELECT p FROM product_postgresql p WHERE p.sku = :sku")
    Optional<ProductPg> findBySku(String sku);

    @Modifying
    @Query("DELETE FROM product_postgresql p WHERE p.sku = :sku")
    void deleteBySku(String sku);

    @Query("SELECT p FROM product_postgresql p WHERE p.designation = :designation")
    List<ProductPg> findByDesignation(String designation);
}
