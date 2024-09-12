package com.isep.acme.applicationServices;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateProductDTO;
import com.isep.acme.adapters.dto.FetchProductDTO;
import com.isep.acme.applicationServices.events.products.CreatedProductPayload;
import com.isep.acme.applicationServices.events.products.UpdatedProductPayload;
import com.isep.acme.applicationServices.interfaces.amqp.IProductEventSender;
import com.isep.acme.applicationServices.interfaces.domain.ISkuGenerator;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductService {

    private final IProductRepository productRepository;

    private final ISkuGenerator skuGenerator;

    private final IProductEventSender productEventSender;

    @Autowired
    public ProductService(IProductRepository productRepository, ISkuGenerator skuGenerator, IProductEventSender
            productEventSender) {
        this.productRepository = productRepository;
        this.skuGenerator = skuGenerator;
        this.productEventSender = productEventSender;
    }

    @Transactional
    public FetchProductDTO create(CreateProductDTO createProductDto) throws DatabaseException {
        if (createProductDto.getSku() == null) {
            createProductDto.setSku(skuGenerator.generateSku(createProductDto.getDesignation()));
        }

        Product product = new Product(createProductDto.getSku(), createProductDto.getDesignation(),
                createProductDto.getDescription());

        this.productRepository.create(product);

        //amqp
        CreatedProductPayload createdProductInfo = new CreatedProductPayload(product.getSku().getSku(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription(),
                product.getNumApprovals());
        this.productEventSender.notifyCreatedEvent(createdProductInfo);

        return new FetchProductDTO(product.getSku().getSku(), product.getApprovalStatus().name(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription(),
                product.getAggregatedRating(), product.getNumApprovals());
    }

    @Transactional
    public FetchProductDTO delete(String sku) throws DatabaseException {
        Product product = this.productRepository.delete(sku);

        //amqp
        this.productEventSender.notifyDeletedEvent(sku);

        return new FetchProductDTO(product.getSku().getSku(), product.getApprovalStatus().name(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription(),
                product.getAggregatedRating(), product.getNumApprovals());
    }

    @Transactional
    public FetchProductDTO update(CreateProductDTO createProductDto) throws DatabaseException {
        Product product = this.productRepository.findBySku(createProductDto.getSku());
        product.setDescription(createProductDto.getDescription());
        product.setDesignation(createProductDto.getDesignation());
        this.productRepository.update(product);

        //amqp
        UpdatedProductPayload updatedProductPayload = new UpdatedProductPayload(product.getSku().getSku(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription());
        this.productEventSender.notifyUpdatedEvent(updatedProductPayload);

        return new FetchProductDTO(product.getSku().getSku(), product.getApprovalStatus().name(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription(),
                product.getAggregatedRating(), product.getNumApprovals());
    }

    @Transactional
    public FetchProductDTO findBySku(String sku) {
        Product product = this.productRepository.findBySku(sku);
        return new FetchProductDTO(product.getSku().getSku(), product.getApprovalStatus().name(),
                product.getDesignation().getDesignation(), product.getDescription().getDescription(),
                product.getAggregatedRating(), product.getNumApprovals());
    }

    @Transactional
    public List<FetchProductDTO> findByDesignation(String designation) {
        List<Product> products = this.productRepository.findByDesignation(designation);
        return this.productsListToDTO(products);
    }

    @Transactional
    public List<FetchProductDTO> findAll() {
        List<Product> products = this.productRepository.findAll();
        return this.productsListToDTO(products);
    }

    private List<FetchProductDTO> productsListToDTO(List<Product> products) {
        List<FetchProductDTO> productDTOS = new ArrayList<>();

        for (Product p : products) {
            FetchProductDTO productDTO = new FetchProductDTO(p.getSku().getSku(), p.getApprovalStatus().name(),
                    p.getDesignation().getDesignation(), p.getDescription().getDescription(),
                    p.getAggregatedRating(), p.getNumApprovals());
            productDTOS.add(productDTO);
        }
        return productDTOS;
    }

    @Transactional
    public String approve(String sku) throws DatabaseException {
        Product product = this.productRepository.findBySku(sku);
        if (product != null && product.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
            throw new DatabaseException("The product is already approved!");
        }

        product.approve();
        this.productRepository.update(product);

        //amqp
        this.productEventSender.notifyApprovedEvent(sku);

        String response;
        if (product.getNumApprovals() == 1) {
            response = "Product approval with success! One approval left to became global acceptable";
        } else {
            response = "Product approved with success! The product is now public acceptable!";
        }
        return response;
    }

    @Transactional
    public void reject(String sku, String report) throws DatabaseException {
        Product product = this.productRepository.findBySku(sku);

        if (product != null && product.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
            throw new DatabaseException("The product is already approved!");
        }

        if (product != null && product.getApprovalStatus().equals(ApprovalStatusEnum.Rejected)) {
            throw new DatabaseException("The product is already rejected!");
        }

        product.reject(report);
        this.productRepository.update(product);

        //amqp
        this.productEventSender.notifyRejectedEvent(sku, report);
    }
}
