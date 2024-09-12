package com.isep.acme.applicationServices.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.isep.acme.applicationServices.events.products.CreatedProductEvent;
import com.isep.acme.applicationServices.events.products.DeletedProductEvent;
import com.isep.acme.applicationServices.events.products.UpdatedProductEvent;
import com.isep.acme.applicationServices.events.reviews.ReviewApprovedEvent;
import com.isep.acme.applicationServices.events.reviews.ReviewCreatedEvent;
import com.isep.acme.applicationServices.events.reviews.ReviewDeletedEvent;
import com.isep.acme.applicationServices.events.reviews.ReviewRejectedEvent;
import com.isep.acme.applicationServices.events.users.UserRegisteredEvent;
import com.isep.acme.applicationServices.events.votes.VoteAddedEvent;
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

        @JsonSubTypes.Type(value = ReviewCreatedEvent.class, name = "review.created"),
        @JsonSubTypes.Type(value = ReviewDeletedEvent.class, name = "review.deleted"),
        @JsonSubTypes.Type(value = ReviewApprovedEvent.class, name = "review.approved"),
        @JsonSubTypes.Type(value = ReviewRejectedEvent.class, name = "review.rejected"),

        @JsonSubTypes.Type(value = VoteAddedEvent.class, name = "vote.added"),

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