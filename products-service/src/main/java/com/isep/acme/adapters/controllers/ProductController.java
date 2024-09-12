package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.controllers.http.HateoasUtils;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateProductDTO;
import com.isep.acme.adapters.dto.FetchProductDTO;
import com.isep.acme.applicationServices.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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
public class ProductController {

    private final ProductService service;

    @Operation(summary = "gets catalog, i.e. all products")
    @GetMapping
    public ResponseEntity<List<FetchProductDTO>> getCatalog() {
        List<FetchProductDTO> products = this.service.findAll();
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "finds product by sku")
    @GetMapping(value = "/{sku}")
    public ResponseEntity<?> getBySku(@PathVariable("sku") final String sku) {
        FetchProductDTO product = this.service.findBySku(sku);

        if (product == null) {
            return (ResponseEntity<?>) ResponseEntity.notFound();
        }

        HateoasUtils.addLinks(product);
        return ResponseEntity.ok().body(product);
    }

    @Operation(summary = "finds product by designation")
    @GetMapping(value = "/designation/{designation}")
    public ResponseEntity<List<FetchProductDTO>> getAllByDesignation(@PathVariable("designation") final String designation) {
        List<FetchProductDTO> products = this.service.findByDesignation(designation);

        HateoasUtils.addLinks(products);
        return ResponseEntity.ok().body(products);
    }

    @Operation(summary = "creates a product")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchProductDTO> create(@RequestBody CreateProductDTO createProductDTO) throws DatabaseException {
        FetchProductDTO product = this.service.create(createProductDTO);

        HateoasUtils.addLinks(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @Operation(summary = "updates a product")
    @PatchMapping(value = "/{sku}")
    public ResponseEntity<FetchProductDTO> update(@RequestBody final CreateProductDTO createProductDTO) throws DatabaseException {
        FetchProductDTO product = this.service.update(createProductDTO);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
    }

    @Operation(summary = "deletes a product")
    @DeleteMapping(value = "/{sku}")
    public ResponseEntity<FetchProductDTO> delete(@PathVariable("sku") final String sku) throws DatabaseException {
        FetchProductDTO product = this.service.delete(sku);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(product);
    }

    @Operation(summary = "approves a product")
    @PutMapping("/approve/{productSku}")
    public ResponseEntity<String> approve(@PathVariable(value = "productSku") final String productSku) throws DatabaseException {
        String response = this.service.approve(productSku);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(summary = "rejects a product")
    @PutMapping("/reject/{productSku}")
    public ResponseEntity<String> reject(@PathVariable(value = "productSku") final String productSku,
                                         @RequestBody String report) throws DatabaseException {
        this.service.reject(productSku, report);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Product rejected with success");
    }
}