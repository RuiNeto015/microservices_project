package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class CreatedProductEvent extends EventMessage {

    private static final String EVENT_TOPIC = "product.created";

    private CreatedProductPayload product;

    public CreatedProductEvent(CreatedProductPayload createdProduct) {
        super(EVENT_TOPIC);
        this.product = createdProduct;
    }
}
