package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.dto.FetchProductDTO;
import com.isep.acme.adapters.dto.ProductForReviewsServiceDTO;
import com.isep.acme.applicationServices.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Product", description = "Endpoints for managing  products")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
class ProductController {

    private final ProductService productService;

    @GetMapping("/init/productsService")
    public ResponseEntity<?> findAllForProductsService() {
        List<FetchProductDTO> productsToSend = this.productService.findAllForProductsService();
        return ResponseEntity.status(HttpStatus.OK).body(productsToSend);
    }

    @GetMapping("/init/reviewsService")
    public ResponseEntity<?> findAllForReviewsService() {
        List<ProductForReviewsServiceDTO> productsToSend = this.productService.findAllForReviewsService();
        return ResponseEntity.status(HttpStatus.OK).body(productsToSend);
    }
}