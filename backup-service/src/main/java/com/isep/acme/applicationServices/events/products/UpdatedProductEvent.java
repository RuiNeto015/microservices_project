package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class UpdatedProductEvent extends EventMessage {

    private UpdatedProductPayload updatedProduct;

    public UpdatedProductEvent(UpdatedProductPayload createdProduct,
                               @Value("${rabbitmq.products.events.updated}") String updatedTopicName) {
        super(updatedTopicName);
        this.updatedProduct = createdProduct;
    }
}
