package com.isep.acme.applicationServices.events.reviews;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class ReviewCreatedEvent extends EventMessage {

    private static final String EVENT_TOPIC = "review.created";

    private ReviewCreatedPayload review;

    public ReviewCreatedEvent(ReviewCreatedPayload reviewCreated) {
        super(EVENT_TOPIC);
        this.review = reviewCreated;
    }
}
