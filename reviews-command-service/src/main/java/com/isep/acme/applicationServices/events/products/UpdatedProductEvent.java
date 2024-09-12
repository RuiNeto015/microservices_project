package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class UpdatedProductEvent extends EventMessage {

    private static final String EVENT_TOPIC = "product.updated";

    private UpdatedProductPayload updatedProduct;

    public UpdatedProductEvent(UpdatedProductPayload createdProduct) {
        super(EVENT_TOPIC);
        this.updatedProduct = createdProduct;
    }
}
