package com.isep.acme.adapters.rabbitmq.products;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.events.EventMessage;
import com.isep.acme.applicationServices.events.products.*;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import com.isep.acme.domain.aggregates.review.Review;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
@Slf4j
public class ProductEventConsumer {

    private final IProductRepository productRepository;

    private final IReviewRepository reviewRepository;

    private final Map<String, Consumer<EventMessage>> productEventHandler;

    public ProductEventConsumer(IProductRepository productRepository,
                                IReviewRepository reviewRepository,
                                @Value("${rabbitmq.products.events.created}") String createdTopicName,
                                @Value("${rabbitmq.products.events.updated}") String updatedTopicName,
                                @Value("${rabbitmq.products.events.deleted}") String deletedTopicName) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;

        productEventHandler = Map.of(
                "product.created", this::handleProductCreated,
                "product.updated", this::handleProductEdited,
                "product.deleted", this::handleProductDeleted,
                "product.approved", this::handleApproved,
                "product.rejected", this::handleRejected
        );
    }

    @RabbitListener(queues = "#{queueProduct.name}")
    private void receiveQueueProduct(EventMessage message) {
        String eventType = message.getTopic();
        if (eventType == null) {
            log.error("Message is malformed, no topic found: " + message);
        } else {
            productEventHandler.getOrDefault(eventType, this::handleUnknownEventType).accept(message);
        }
    }

    private void handleProductCreated(EventMessage productMessage) {
        CreatedProductEvent createdEvent = (CreatedProductEvent) productMessage;
        log.info("Handling product created event: " + createdEvent);

        try {
            Product productDb = this.productRepository.findBySku(createdEvent.getProduct().getSku());
            if (productDb == null) {
                Product newProduct = new Product(createdEvent.getProduct().getSku(),
                        createdEvent.getProduct().getDesignation(),
                        createdEvent.getProduct().getDescription());
                this.productRepository.create(newProduct);
            }
        } catch (DatabaseException ignored) {
            log.error("Error handling product created event: " + createdEvent);
        }
    }

    private void handleProductEdited(EventMessage productMessage) {
        UpdatedProductEvent updatedEvent = (UpdatedProductEvent) productMessage;
        log.info("Handling product updated event: " + updatedEvent);

        try {
            Product productDb = this.productRepository.findBySku(updatedEvent.getUpdatedProduct().getSku());
            if (productDb != null) {
                productDb.setDescription(updatedEvent.getUpdatedProduct().getDescription());
                productDb.setDesignation(updatedEvent.getUpdatedProduct().getDesignation());
                this.productRepository.update(productDb);
            } else {
                log.error("Product not found on handling updated product event: " + updatedEvent);
            }
        } catch (DatabaseException ignored) {
            log.error("Error handling product created event: " + updatedEvent);
        }
    }

    private void handleProductDeleted(EventMessage productMessage) {
        DeletedProductEvent deletedEvent = (DeletedProductEvent) productMessage;
        log.info("Handling product deleted event: " + productMessage);

        try {
            List<Review> reviews = this.reviewRepository.findBySku(deletedEvent.getSku());
            if (!reviews.isEmpty()) {
                for (Review r : reviews) {
                    this.reviewRepository.delete(r.getId());
                }
            }
            this.productRepository.delete(deletedEvent.getSku());
        } catch (DatabaseException ignored) {
            log.error("Error handling product deleted event: " + deletedEvent);
        }
    }

    private void handleApproved(EventMessage productMessage) {
        ApprovedProductEvent event = (ApprovedProductEvent) productMessage;

        log.info("Handling product approved event: " + event);

        Product product = this.productRepository.findBySku(event.getId());
        if (product != null && !product.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
            try {
                product.approve();
                this.productRepository.update(product);
            } catch (DatabaseException ignored) {
                log.error("Error handling product approved event: " + event);
            }
        }
    }

    private void handleRejected(EventMessage productMessage) {
        RejectedProductEvent event = (RejectedProductEvent) productMessage;

        log.info("Handling product rejected event: " + event);

        Product product = this.productRepository.findBySku(event.getId());
        if (product != null && product.getApprovalStatus().equals(ApprovalStatusEnum.Pending)) {
            try {
                product.reject(event.getReport());
                this.productRepository.update(product);
            } catch (DatabaseException ignored) {
                log.error("Error handling product approved event: " + event);
            }
        }
    }

    private void handleUnknownEventType(EventMessage productMessage) {
        log.error("Unknown event type in message: " + productMessage);
    }
}
