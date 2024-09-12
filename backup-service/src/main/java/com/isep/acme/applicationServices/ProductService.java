package com.isep.acme.applicationServices;

import com.isep.acme.adapters.dto.FetchProductDTO;
import com.isep.acme.adapters.dto.ProductForReviewsServiceDTO;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.domain.aggregates.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductService {

    private final IProductRepository productRepository;

    @Transactional
    public List<FetchProductDTO> findAllForProductsService() {
        List<Product> products = this.productRepository.findAll();
        return this.productsListToProductsServiceDTO(products);
    }

    @Transactional
    public List<ProductForReviewsServiceDTO> findAllForReviewsService() {
        List<Product> products = this.productRepository.findAll();
        return this.productsListToReviewsServiceDTO(products);
    }

    private List<FetchProductDTO> productsListToProductsServiceDTO(List<Product> products) {
        List<FetchProductDTO> productDTOS = new ArrayList<>();

        for (Product p : products) {
            FetchProductDTO productDTO = new FetchProductDTO(p.getSku(), p.getApprovalStatus().name(), p.getDesignation(), p.getDescription(), p.getAggregatedRating(), p.getNumApprovals());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    private List<ProductForReviewsServiceDTO> productsListToReviewsServiceDTO(List<Product> products) {
        List<ProductForReviewsServiceDTO> productDTOS = new ArrayList<>();

        for (Product p : products) {
            ProductForReviewsServiceDTO productDTO = new ProductForReviewsServiceDTO(p.getSku(), p.getApprovalStatus().name(), p.getNumApprovals());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }
}
