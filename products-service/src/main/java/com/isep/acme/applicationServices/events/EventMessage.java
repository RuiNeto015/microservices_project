package com.isep.acme.applicationServices.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.isep.acme.applicationServices.events.products.*;
import com.isep.acme.applicationServices.events.users.UserRegisteredEvent;
import com.isep.acme.config.ApplicationContextProvider;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@ToString
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "topic")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CreatedProductEvent.class, name = "product.created"),
        @JsonSubTypes.Type(value = UpdatedProductEvent.class, name = "product.updated"),
        @JsonSubTypes.Type(value = DeletedProductEvent.class, name = "product.deleted"),
        @JsonSubTypes.Type(value = ApprovedProductEvent.class, name = "product.approved"),
        @JsonSubTypes.Type(value = RejectedProductEvent.class, name = "product.rejected"),

        @JsonSubTypes.Type(value = UserRegisteredEvent.class, name = "user.registered")
})
public class EventMessage {

    private String messageId;
    private LocalDateTime timestamp;
    private String topic;
    private String sender;

    public EventMessage(String topic) {
        this.messageId = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.topic = topic;
        this.sender = ApplicationContextProvider.getContext().getBean(UUID.class).toString();
    }
}
