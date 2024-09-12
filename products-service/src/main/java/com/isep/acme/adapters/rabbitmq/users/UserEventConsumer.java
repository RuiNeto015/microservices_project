package com.isep.acme.adapters.rabbitmq.users;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.events.EventMessage;
import com.isep.acme.applicationServices.events.users.UserRegisteredEvent;
import com.isep.acme.applicationServices.events.users.UserRegisteredPayload;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.config.ApplicationContextProvider;
import com.isep.acme.domain.aggregates.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Slf4j
public class UserEventConsumer {

    private final IUserRepository userRepository;

    private final Map<String, Consumer<EventMessage>> userEventHandler;

    public UserEventConsumer(IUserRepository userRepository,
                             @Value("${rabbitmq.users.events.registered}") String registeredTopicName) {
        this.userRepository = userRepository;

        userEventHandler = Map.of(
                "user.registered", this::handleUserRegistered
        );
    }

    @RabbitListener(queues = "#{queueUser.name}")
    private void receiveQueueUser(EventMessage message) {
        String eventType = message.getTopic();
        if (eventType == null || message.getSender() == null) {
            log.error("Message is malformed, topic or sender missing: " + message);
        } else {
            if (message.getSender().equals(ApplicationContextProvider.getContext().getBean(UUID.class).toString())) {
                log.info("Message is being ignored. Sender and consumer is matching: " + message);
            } else {
                userEventHandler.getOrDefault(eventType, this::handleUnknownEventType).accept(message);
            }
        }
    }

    private void handleUserRegistered(EventMessage userMessage) {
        UserRegisteredEvent registeredEvent = (UserRegisteredEvent) userMessage;
        log.info("Handling user registered event: " + registeredEvent);

        if (this.userRepository.findByEmail(registeredEvent.getUser().getEmail()) == null) {
            try {
                UserRegisteredPayload userPayload = registeredEvent.getUser();
                User user = new User(userPayload.getId(), userPayload.getEmail(), userPayload.getPassword(),
                        userPayload.getFullName(), userPayload.getNif(), userPayload.getNif(), userPayload.getRole());
                this.userRepository.create(user);
            } catch (DatabaseException ignored) {
                log.error("Error handling user registered event: " + registeredEvent);
            }
        }
    }

    private void handleUnknownEventType(EventMessage userMessage) {
        log.error("Unknown event type in message: " + userMessage);
    }
}
