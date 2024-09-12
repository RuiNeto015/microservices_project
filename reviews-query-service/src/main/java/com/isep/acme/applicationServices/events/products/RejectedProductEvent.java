package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class RejectedProductEvent extends EventMessage {

    private static final String EVENT_TOPIC = "product.rejected";

    private String id;

    private String report;

    public RejectedProductEvent(String id, String report) {
        super(EVENT_TOPIC);
        this.id = id;
        this.report = report;
    }
}
