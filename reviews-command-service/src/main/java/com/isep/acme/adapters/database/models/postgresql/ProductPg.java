package com.isep.acme.adapters.database.models.postgresql;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "product_postgresql")
public class ProductPg {

    @Id
    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(nullable = false)
    private String approvalStatus;

    @Column(nullable = false)
    private Integer numApprovals;
}
