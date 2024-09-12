package com.isep.acme.adapters.rabbitmq.users;

import com.isep.acme.applicationServices.events.users.UserRegisteredEvent;
import com.isep.acme.applicationServices.events.users.UserRegisteredPayload;
import com.isep.acme.applicationServices.interfaces.amqp.IUserEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventSender implements IUserEventSender {

    private final FanoutExchange usersFanoutExchange;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.users.events.registered}")
    private String registeredTopicName;

    @Override
    public void notifyRegisteredEvent(UserRegisteredPayload userRegisteredInfo) {
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(userRegisteredInfo);
        rabbitTemplate.convertAndSend(usersFanoutExchange.getName(), registeredTopicName, userRegisteredEvent);
        log.info(String.format("[USER.REGISTERED] sent -> %s", userRegisteredEvent));
    }

}
