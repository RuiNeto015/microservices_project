package com.isep.acme.adapters.rabbitmq.products;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.events.EventMessage;
import com.isep.acme.applicationServices.events.products.*;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.config.ApplicationContextProvider;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Slf4j
public class ProductEventConsumer {

    private final IProductRepository productRepository;

    private final Map<String, Consumer<EventMessage>> productEventHandler;

    public ProductEventConsumer(IProductRepository productRepository, @Value("${rabbitmq.products.events.created}")
    String createdTopicName, @Value("${rabbitmq.products.events.deleted}") String deletedTopicName) {
        this.productRepository = productRepository;

        productEventHandler = Map.of(
                "product.created", this::handleProductCreated,
                "product.deleted", this::handleProductDeleted,
                "product.approved", this::handleApproved,
                "product.rejected", this::handleRejected);

    }

    @RabbitListener(queues = "#{queueProduct.name}")
    private void receiveQueueProduct(EventMessage productMessage) {
        String eventType = productMessage.getTopic();
        if (eventType == null || productMessage.getSender() == null) {
            log.error("Message is malformed, topic or sender missing: " + productMessage);
        } else {
            if (productMessage.getSender().equals(ApplicationContextProvider.getContext().getBean(UUID.class)
                    .toString())) {
                log.info("Message is being ignored. Sender and consumer is matching: " + productMessage);
            } else {
                productEventHandler.getOrDefault(eventType, this::handleUnknownEventType).accept(productMessage);
            }
        }
    }

    private void handleProductCreated(EventMessage productMessage) {
        CreatedProductEvent createdEvent = (CreatedProductEvent) productMessage;
        log.info("Handling product created event: " + createdEvent);

        if (!this.productRepository.exists(createdEvent.getProduct().getSku())) {
            try {
                CreatedProductPayload payload = createdEvent.getProduct();
                Product product = new Product(payload.getSku(), ApprovalStatusEnum.Pending, payload.getDesignation(),
                        payload.getDescription(), payload.getNumApprovals());
                this.productRepository.create(product);
            } catch (DatabaseException ignored) {
                log.error("Error handling product created event: " + createdEvent);
            }
        }
    }

    private void handleProductDeleted(EventMessage productMessage) {
        DeletedProductEvent deletedEvent = (DeletedProductEvent) productMessage;
        log.info("Handling product deleted event: " + productMessage);

        try {
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
