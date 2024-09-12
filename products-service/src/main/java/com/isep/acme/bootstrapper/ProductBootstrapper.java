package com.isep.acme.bootstrapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.clients.BackupWebClient;
import com.isep.acme.clients.dtos.ResponseProduct;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductBootstrapper {

    private final IProductRepository productRepo;

    @Autowired
    private BackupWebClient backupWebClient;

    public void run() throws IllegalArgumentException, DatabaseException {
        ResponseEntity<String> backupRequestResponse;
        try {
            backupRequestResponse = this.backupWebClient.requestProductsInit();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error communicating with the backup service!");
        }

        if (backupRequestResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Error communicating with the backup service! Response Status isn't OK");
        }

        Gson gson = new Gson();
        TypeToken<List<ResponseProduct>> token = new TypeToken<List<ResponseProduct>>() {
        };
        String responseBody = backupRequestResponse.getBody();
        List<ResponseProduct> productsList = gson.fromJson(responseBody, token.getType());

        if (productsList == null || productsList.isEmpty()) {
            System.out.println("No products on the backup service. Everything fine!");
        } else {
            for (ResponseProduct p : productsList) {
                Product product = new Product(p.getSku(), ApprovalStatusEnum.valueOf(p.getApprovalStatus()),
                        p.getDesignation(), p.getDescription(), p.getNumApprovals(), p.getAggregatedRating());
                if (!this.productRepo.exists(p.getSku())) {
                    this.productRepo.create(product);
                }
            }
        }
    }
}
