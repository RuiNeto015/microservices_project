package com.isep.acme.adapters.rabbitmq.votes;

import com.isep.acme.applicationServices.events.users.UserRegisteredEvent;
import com.isep.acme.applicationServices.events.users.UserRegisteredPayload;
import com.isep.acme.applicationServices.events.votes.VoteAddedEvent;
import com.isep.acme.applicationServices.events.votes.VoteAddedPayload;
import com.isep.acme.applicationServices.interfaces.amqp.IVoteEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteEventSender implements IVoteEventSender {

    private final FanoutExchange voteFanoutExchange;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void notifyVoteAdded(VoteAddedPayload voteAddedInfo) {
        VoteAddedEvent event = new VoteAddedEvent(voteAddedInfo);
        rabbitTemplate.convertAndSend(voteFanoutExchange.getName(), "vote.added", event);
        log.info(String.format("[VOTE.ADDED] sent -> %s", event));
    }

}
