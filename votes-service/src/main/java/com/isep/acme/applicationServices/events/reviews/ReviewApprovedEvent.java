package com.isep.acme.applicationServices.events.reviews;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class ReviewApprovedEvent extends EventMessage {

    private static final String EVENT_TOPIC = "review.approved";

    private String id;

    public ReviewApprovedEvent(String id) {
        super(EVENT_TOPIC);
        this.id = id;
    }
}
