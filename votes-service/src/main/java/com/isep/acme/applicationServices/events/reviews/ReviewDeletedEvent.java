package com.isep.acme.applicationServices.events.reviews;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class ReviewDeletedEvent extends EventMessage {

    private static final String EVENT_TOPIC = "review.deleted";

    private String id;

    public ReviewDeletedEvent(String id) {
        super(EVENT_TOPIC);
        this.id = id;
    }
}
