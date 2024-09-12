package com.isep.acme.adapters.rabbitmq.reviews;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.VoteService;
import com.isep.acme.applicationServices.events.EventMessage;
import com.isep.acme.applicationServices.events.reviews.*;
import com.isep.acme.applicationServices.events.votes.VoteAddedPayload;
import com.isep.acme.applicationServices.interfaces.amqp.IVoteEventSender;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IVoteRepository;
import com.isep.acme.config.ApplicationContextProvider;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.aggregates.votes.Vote;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Component
@Slf4j
public class ReviewsEventConsumer {

    private final IReviewRepository reviewRepository;

    private final IVoteRepository voteRepository;

    private final IVoteEventSender voteEventSender;

    private final Map<String, Consumer<EventMessage>> reviewEventHandler;

    public ReviewsEventConsumer(IReviewRepository reviewRepository,
                                IVoteEventSender voteEventSender,
                                IVoteRepository voteRepository,
                                @Value("${rabbitmq.reviews.events.created}") String createdTopicName,
                                @Value("${rabbitmq.reviews.events.deleted}") String deletedTopicName) {
        this.reviewRepository = reviewRepository;
        this.voteEventSender = voteEventSender;
        this.voteRepository = voteRepository;

        reviewEventHandler = Map.of(
                "review.created", this::handleCreated,
                "review.deleted", this::handleDeleted,
                "review.approved", this::handleApproved,
                "review.rejected", this::handleRejected
        );
    }

    @RabbitListener(queues = "#{queueReview.name}")
    private void receiveQueueReview(EventMessage message) {
        String eventType = message.getTopic();
        if (eventType == null || message.getSender() == null) {
            log.error("Message is malformed, topic or sender missing: " + message);
        } else {
            if (message.getSender().equals(ApplicationContextProvider.getContext().getBean(UUID.class).toString())) {
                log.info("Message is being ignored. Sender and consumer is matching: " + message);
            } else {
                reviewEventHandler.getOrDefault(eventType, this::handleUnknownEventType).accept(message);
            }
        }
    }

    private void handleCreated(EventMessage reviewMessage) {
        ReviewCreatedEvent event = (ReviewCreatedEvent) reviewMessage;
        log.info("Handling review created event: " + event);

        if (this.reviewRepository.findById(event.getReview().getId()) == null) {
            try {
                ReviewCreatedPayload payload = event.getReview();
                Review review = new Review(payload.getId(), ApprovalStatusEnum.Pending, payload.getNumApprovals(), payload.getUserId());

                this.reviewRepository.create(review);
            } catch (DatabaseException ignored) {
                log.error("Error handling review created event: " + event);
            }
        }
    }

    private void handleDeleted(EventMessage reviewMessage) {
        ReviewDeletedEvent event = (ReviewDeletedEvent) reviewMessage;

        log.info("Handling review deleted event: " + event);

        if (this.reviewRepository.findById(event.getId()) != null) {
            try {
                this.voteRepository.deleteAllByReview(event.getId());
                this.reviewRepository.delete(event.getId());
            } catch (DatabaseException ignored) {
                log.error("Error handling review deleted event: " + event);
            }
        }
    }

    private void handleApproved(EventMessage reviewMessage) {
        ReviewApprovedEvent event = (ReviewApprovedEvent) reviewMessage;

        log.info("Handling review approved event: " + event);

        Review review = this.reviewRepository.findById(event.getId());
        if (review != null && !review.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
            try {
                review.approve();

                //if it was approved
                if (review.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
                    Vote vote = new Vote(review.getUserId(), review.getId(), VoteEnum.UpVote);
                    this.voteRepository.create(vote);

                    //ampq
                    this.voteEventSender.notifyVoteAdded(new VoteAddedPayload(vote.getUserId(), vote.getReviewId(), vote.getVote().toString()));
                }

                this.reviewRepository.update(review);
            } catch (DatabaseException ignored) {
                log.error("Error handling review approved event: " + event);
            }
        }
    }

    private void handleRejected(EventMessage reviewMessage) {
        ReviewRejectedEvent event = (ReviewRejectedEvent) reviewMessage;

        log.info("Handling review rejected event: " + event);

        Review review = this.reviewRepository.findById(event.getId());
        if (review != null && review.getApprovalStatus().equals(ApprovalStatusEnum.Pending)) {
            try {
                review.reject(event.getReport());
                this.reviewRepository.update(review);
            } catch (DatabaseException ignored) {
                log.error("Error handling review approved event: " + event);
            }
        }
    }

    private void handleUnknownEventType(EventMessage reviewMessage) {
        log.error("Unknown event type in message: " + reviewMessage);
    }
}
