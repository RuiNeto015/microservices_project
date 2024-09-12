package com.isep.acme.adapters.rabbitmq.products;

import com.isep.acme.applicationServices.events.products.*;
import com.isep.acme.applicationServices.interfaces.amqp.IProductEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventSender implements IProductEventSender {

    private final FanoutExchange productFanoutExchange;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.products.events.created}")
    private String createdTopicName;

    @Value("${rabbitmq.products.events.updated}")
    private String updatedTopicName;

    @Value("${rabbitmq.products.events.deleted}")
    private String deletedTopicName;

    @Override
    public void notifyCreatedEvent(CreatedProductPayload productCreated) {
        CreatedProductEvent productCreatedEvent = new CreatedProductEvent(productCreated);
        rabbitTemplate.convertAndSend(productFanoutExchange.getName(), createdTopicName, productCreatedEvent);
        log.info(String.format("[PRODUCT.CREATED] sent -> %s", productCreatedEvent));
    }

    @Override
    public void notifyUpdatedEvent(UpdatedProductPayload productUpdated) {
        UpdatedProductEvent productUpdatedEvent = new UpdatedProductEvent(productUpdated);
        rabbitTemplate.convertAndSend(productFanoutExchange.getName(), updatedTopicName, productUpdatedEvent);
        log.info(String.format("[PRODUCT.UPDATED] sent -> %s", productUpdatedEvent));
    }

    @Override
    public void notifyDeletedEvent(String sku) {
        DeletedProductEvent deletedProductEvent = new DeletedProductEvent(sku);
        rabbitTemplate.convertAndSend(productFanoutExchange.getName(), deletedTopicName, deletedProductEvent);
        log.info(String.format("[PRODUCT.DELETED] sent -> %s", deletedProductEvent));
    }

    @Override
    public void notifyApprovedEvent(String sku) {
        ApprovedProductEvent productEvent = new ApprovedProductEvent(sku);
        rabbitTemplate.convertAndSend(productFanoutExchange.getName(), "product.approved", productEvent);
        log.info(String.format("[PRODUCT.APPROVED] sent -> %s", productEvent));
    }

    @Override
    public void notifyRejectedEvent(String sku, String report) {
        RejectedProductEvent productEvent = new RejectedProductEvent(sku, report);
        rabbitTemplate.convertAndSend(productFanoutExchange.getName(), "product.rejected", productEvent);
        log.info(String.format("[PRODUCT.REJECTED] sent -> %s", productEvent));
    }
}
