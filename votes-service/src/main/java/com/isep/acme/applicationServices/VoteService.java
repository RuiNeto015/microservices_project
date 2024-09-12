package com.isep.acme.applicationServices;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.events.votes.VoteAddedPayload;
import com.isep.acme.applicationServices.interfaces.amqp.IVoteEventSender;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IUserRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IVoteRepository;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.aggregates.user.User;
import com.isep.acme.domain.aggregates.votes.Vote;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VoteService {

    private final IReviewRepository reviewRepository;

    private final IVoteEventSender voteEventSender;

    private final IVoteRepository voteRepository;

    private final IUserRepository userRepository;

    @Transactional
    public String vote(String userId, String reviewId, String voteType) throws Exception {
        Review review = this.reviewRepository.findById(reviewId);
        User user = this.userRepository.findById(userId);

        if (review == null) {
            throw new DatabaseException("The review not exists");
        }

        if (user == null) {
            throw new DatabaseException("The user not exists");
        }

        if (review.getApprovalStatus().equals(ApprovalStatusEnum.Pending)) {
            throw new IllegalArgumentException("The review is not approved yet");
        }

        VoteEnum voteEnum = VoteEnum.fromString(voteType);
        if (voteEnum == null) {
            throw new IllegalArgumentException("The enum type is invalid");
        }

        Vote oldVote = this.voteRepository.findByUserAndReview(userId, reviewId);

        String response = "";
        if (oldVote != null) {
            if (oldVote.getVote() == voteEnum) {
                throw new IllegalArgumentException("You already vote that!");
            } else {
                response = "You had alter the vote successfully";
            }
        } else {
            response = "Voted added with success";
        }
        Vote vote = new Vote(userId, reviewId, voteEnum);
        this.voteRepository.create(vote);

        //ampq
        this.voteEventSender.notifyVoteAdded(new VoteAddedPayload(vote.getUserId(), vote.getReviewId(), vote.getVote().toString()));

        return response;
    }

    @Transactional
    public List<Vote> getVotesByReview(String reviewId) throws Exception {
        return this.voteRepository.findByReview(reviewId);
    }
}
