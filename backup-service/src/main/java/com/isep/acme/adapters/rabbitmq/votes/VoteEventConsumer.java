package com.isep.acme.adapters.rabbitmq.votes;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.events.EventMessage;
import com.isep.acme.applicationServices.events.votes.VoteAddedEvent;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IVoteRepository;
import com.isep.acme.domain.aggregates.votes.Vote;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Component
@Slf4j
public class VoteEventConsumer {

    private final IVoteRepository voteRepository;

    private final IUserRepository userRepository;

    private final IReviewRepository reviewRepository;

    private final Map<String, Consumer<EventMessage>> eventHandler;

    public VoteEventConsumer(IVoteRepository voteRepository,
                             IUserRepository userRepository,
                             IReviewRepository reviewRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;

        eventHandler = Map.of(
                "vote.added", this::handleUserAdded
        );
    }

    @RabbitListener(queues = "#{queueVote.name}")
    private void receiveQueueVote(EventMessage message) {
        String eventType = message.getTopic();
        if (eventType == null) {
            log.error("Message is malformed, no topic found: " + message);
        } else {
            eventHandler.getOrDefault(eventType, this::handleUnknownEventType).accept(message);
        }
    }

    private void handleUserAdded(EventMessage message) {
        VoteAddedEvent event = (VoteAddedEvent) message;
        log.info("Handling vote added event: " + event);

        if (this.userRepository.findById(event.getVote().getUserId()) != null &&
                this.reviewRepository.findById(event.getVote().getReviewId()) != null) {
            try {
                Vote vote = new Vote(event.getVote().getUserId(), event.getVote().getReviewId(),
                        VoteEnum.fromString(event.getVote().getVoteType()));
                this.voteRepository.create(vote);
            } catch (DatabaseException ignored) {
                log.error("Error handling vote added event: " + event);
            }
        }
    }

    private void handleUnknownEventType(EventMessage userMessage) {
        log.error("Unknown event type in message: " + userMessage);
    }
}
