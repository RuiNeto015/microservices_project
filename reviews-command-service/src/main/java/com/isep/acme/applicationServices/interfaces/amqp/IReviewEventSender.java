package com.isep.acme.applicationServices.interfaces.amqp;

import com.isep.acme.applicationServices.events.reviews.ReviewCreatedPayload;

public interface IReviewEventSender {

    void notifyCreatedEvent(ReviewCreatedPayload reviewCreatedPayload);

    void notifyDeletedEvent(String id);

    void notifyApprovedEvent(String id);

    void notifyRejectedEvent(String id, String report);

}
