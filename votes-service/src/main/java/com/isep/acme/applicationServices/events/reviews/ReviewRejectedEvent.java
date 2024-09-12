package com.isep.acme.applicationServices.events.reviews;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class ReviewRejectedEvent extends EventMessage {

    private static final String EVENT_TOPIC = "review.rejected";

    private String id;

    private String report;

    public ReviewRejectedEvent(String id, String report) {
        super(EVENT_TOPIC);
        this.id = id;
        this.report = report;
    }
}
