package com.isep.acme.adapters.database.repositories.postgresql;

import com.isep.acme.adapters.database.models.postgresql.ReviewPg;
import com.isep.acme.adapters.database.models.postgresql.UserPg;
import com.isep.acme.adapters.database.models.postgresql.VotePg;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.springRepositories.postgresql.ReviewRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.UserRepositoryPostgresql;
import com.isep.acme.adapters.database.springRepositories.postgresql.VoteRepositoryPostgresql;
import com.isep.acme.applicationServices.interfaces.repositories.IVoteRepository;
import com.isep.acme.domain.aggregates.votes.Vote;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("VoteRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "postgresql", matchIfMissing = true)
@RequiredArgsConstructor
public class VotePgRepositoryImpl implements IVoteRepository {

    private final VoteRepositoryPostgresql voteRepository;

    private final ReviewRepositoryPostgresql reviewRepository;

    private final UserRepositoryPostgresql userRepository;

    @Override
    public void create(Vote vote) throws DatabaseException {
        Optional<ReviewPg> reviewPg = this.reviewRepository.findById(vote.getReviewId());
        Optional<UserPg> userPg = this.userRepository.findById(vote.getUserId());

        if (reviewPg.isEmpty()) {
            throw new DatabaseException("Review not exists");
        }

        if (userPg.isEmpty()) {
            throw new DatabaseException("User not exists");
        }

        VotePg votePg = new VotePg(userPg.get(), reviewPg.get(), vote.getVote().name());
        this.voteRepository.save(votePg);
    }

    @Override
    public Vote findByUserAndReview(String userId, String reviewId) {
        VotePg votePg = this.voteRepository.findByUserAndReview(userId, reviewId);
        if (votePg != null) {
            return new Vote(votePg.getUser().getId(), votePg.getReview().getId(), VoteEnum.fromString(votePg.getVote()));
        } else {
            return null;
        }
    }

    @Transactional
    @Override
    public void deleteAllByReview(String reviewId) {
        this.voteRepository.deleteAllByReview(reviewId);
    }

    @Override
    public List<Vote> findByReview(String reviewId) {
        List<VotePg> votesPg = this.voteRepository.findByReview(reviewId);
        List<Vote> votes = new ArrayList<>();

        for (int i = 0; i < votesPg.size(); i++) {
            votes.add(new Vote(votesPg.get(i).getUser().getId(), votesPg.get(i).getReview().getId(),
                    VoteEnum.fromString(votesPg.get(i).getVote())));
        }
        return votes;
    }
}
