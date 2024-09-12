package com.isep.acme.adapters.rabbitmq.products;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.events.EventMessage;
import com.isep.acme.applicationServices.events.products.ApprovedProductEvent;
import com.isep.acme.applicationServices.events.products.CreatedProductEvent;
import com.isep.acme.applicationServices.events.products.DeletedProductEvent;
import com.isep.acme.applicationServices.events.products.RejectedProductEvent;
import com.isep.acme.applicationServices.interfaces.amqp.IReviewEventSender;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.config.ApplicationContextProvider;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Slf4j
public class ProductEventConsumer {

    private final IProductRepository productRepository;

    private final IReviewRepository reviewRepository;

    private final IReviewEventSender eventSender;

    private final Map<String, Consumer<EventMessage>> productEventHandler;

    public ProductEventConsumer(IProductRepository productRepository,
                                IReviewRepository reviewRepository,
                                IReviewEventSender eventSender) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.eventSender = eventSender;

        productEventHandler = Map.of(
                "product.created", this::handleProductCreated,
                "product.deleted", this::handleProductDeleted,
                "product.approved", this::handleApproved,
                "product.rejected", this::handleRejected
        );
    }

    @RabbitListener(queues = "#{queueProduct.name}")
    private void receiveQueueProduct(EventMessage productMessage) {
        String eventType = productMessage.getTopic();
        if (eventType == null || productMessage.getSender() == null) {
            log.error("Message is malformed, topic or sender missing: " + productMessage);
        } else {
            if (productMessage.getSender().equals(ApplicationContextProvider.getContext().getBean(UUID.class).toString())) {
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
                Product product = new Product(createdEvent.getProduct().getSku());
                this.productRepository.create(product);
            } catch (DatabaseException ignored) {
                log.error("Error handling product created event: " + createdEvent);
            }
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
                product.reject();
                this.productRepository.update(product);
            } catch (DatabaseException ignored) {
                log.error("Error handling product approved event: " + event);
            }
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
                    //amqp
                    eventSender.notifyDeletedEvent(r.getId());
                }
            }
            this.productRepository.delete(deletedEvent.getSku());
        } catch (DatabaseException ignored) {
            log.error("Error handling product deleted event: " + deletedEvent);
        }
    }

    private void handleUnknownEventType(EventMessage productMessage) {
        log.error("Unknown event type in message: " + productMessage);
    }
}
