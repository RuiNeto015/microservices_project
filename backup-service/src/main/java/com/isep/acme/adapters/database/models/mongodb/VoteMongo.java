package com.isep.acme.adapters.database.models.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class VoteMongo {

    private String vote;

    private String userId;

    private String reviewId;

}
