package com.isep.acme.adapters.rabbitmq.reviews;

import com.isep.acme.applicationServices.events.reviews.*;
import com.isep.acme.applicationServices.interfaces.amqp.IReviewEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReviewsEventSender implements IReviewEventSender {

    private final FanoutExchange reviewFanoutExchange;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void notifyCreatedEvent(ReviewCreatedPayload payload) {
        ReviewCreatedEvent reviewEvent = new ReviewCreatedEvent(payload);
        rabbitTemplate.convertAndSend(reviewFanoutExchange.getName(), "review.created", reviewEvent);
        log.info(String.format("[REVIEW.CREATED] sent -> %s", reviewEvent));
    }

    @Override
    public void notifyDeletedEvent(String id) {
        ReviewDeletedEvent reviewEvent = new ReviewDeletedEvent(id);
        rabbitTemplate.convertAndSend(reviewFanoutExchange.getName(), "review.deleted", reviewEvent);
        log.info(String.format("[REVIEW.DELETED] sent -> %s", reviewEvent));
    }

    @Override
    public void notifyApprovedEvent(String id) {
        ReviewApprovedEvent reviewEvent = new ReviewApprovedEvent(id);
        rabbitTemplate.convertAndSend(reviewFanoutExchange.getName(), "review.approved", reviewEvent);
        log.info(String.format("[REVIEW.APPROVED] sent -> %s", reviewEvent));
    }

    @Override
    public void notifyRejectedEvent(String id, String report) {
        ReviewRejectedEvent reviewEvent = new ReviewRejectedEvent(id, report);
        rabbitTemplate.convertAndSend(reviewFanoutExchange.getName(), "review.rejected", reviewEvent);
        log.info(String.format("[REVIEW.REJECTED] sent -> %s", reviewEvent));
    }

}
