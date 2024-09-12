package com.isep.acme.adapters.database.models.mongodb;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@AllArgsConstructor
public class ProductMongo {

    private String sku;

}
