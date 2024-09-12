package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class ApprovedProductEvent extends EventMessage {

    private static final String EVENT_TOPIC = "product.approved";

    private String id;

    public ApprovedProductEvent(String id) {
        super(EVENT_TOPIC);
        this.id = id;
    }
}
