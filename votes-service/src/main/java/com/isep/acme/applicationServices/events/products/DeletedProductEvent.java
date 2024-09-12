package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class DeletedProductEvent extends EventMessage {

    private static final String EVENT_TOPIC = "product.deleted";

    private String sku;

    public DeletedProductEvent(String sku) {
        super(EVENT_TOPIC);
        this.sku = sku;
    }
}
