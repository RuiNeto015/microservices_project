package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class DeletedProductEvent extends EventMessage {

    private String sku;

    public DeletedProductEvent(String sku,
                               @Value("${rabbitmq.products.events.deleted}") String deletedTopicName) {
        super(deletedTopicName);
        this.sku = sku;
    }
}
