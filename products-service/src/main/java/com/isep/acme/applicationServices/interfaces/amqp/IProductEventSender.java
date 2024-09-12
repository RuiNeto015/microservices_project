package com.isep.acme.applicationServices.interfaces.amqp;

import com.isep.acme.applicationServices.events.products.CreatedProductPayload;
import com.isep.acme.applicationServices.events.products.UpdatedProductPayload;

public interface IProductEventSender {

    void notifyCreatedEvent(CreatedProductPayload productCreated);

    void notifyUpdatedEvent(UpdatedProductPayload productUpdated);

    void notifyDeletedEvent(String sku);

    void notifyApprovedEvent(String sku);

    void notifyRejectedEvent(String sku, String report);

}
