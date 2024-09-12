package com.isep.acme.applicationServices.events.products;

import com.isep.acme.applicationServices.events.EventMessage;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
@Data
@ToString(callSuper = true)
public class CreatedProductEvent extends EventMessage {

    private CreatedProductPayload product;

    public CreatedProductEvent(CreatedProductPayload createdProduct,
                               @Value("${rabbitmq.products.events.created}") String createdTopicName) {
        super(createdTopicName);
        this.product = createdProduct;
    }
}
