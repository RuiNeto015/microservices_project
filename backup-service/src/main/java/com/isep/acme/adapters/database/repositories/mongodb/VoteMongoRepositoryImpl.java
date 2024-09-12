package com.isep.acme.adapters.database.repositories.mongodb;

import com.isep.acme.adapters.database.models.mongodb.VoteMongo;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.database.springRepositories.mongodb.VoteRepositoryMongodb;
import com.isep.acme.applicationServices.interfaces.repositories.IVoteRepository;
import com.isep.acme.domain.aggregates.votes.Vote;
import com.isep.acme.domain.enums.VoteEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("VoteRepositoryCapsule")
@ConditionalOnProperty(value = "spring.repositories.targetPackage", havingValue = "mongodb")
public class VoteMongoRepositoryImpl implements IVoteRepository {

    private final VoteRepositoryMongodb voteRepository;

    @Autowired
    public VoteMongoRepositoryImpl(VoteRepositoryMongodb voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public void create(Vote vote) throws DatabaseException {
        VoteMongo voteMongo = new VoteMongo();
        voteMongo.setVote(vote.getVote().toString());
        voteMongo.setReviewId(vote.getReviewId());
        voteMongo.setUserId(vote.getUserId());
        this.voteRepository.save(voteMongo);
    }

    @Override
    public List<Vote> findAll() {
        List<VoteMongo> databaseVotes = (List<VoteMongo>) this.voteRepository.findAll();

        List<Vote> domainVotes = new ArrayList<>();

        for (VoteMongo v : databaseVotes) {
            Vote voteToAdd = new Vote(v.getUserId(), v.getReviewId(), VoteEnum.valueOf(v.getVote()));
            domainVotes.add(voteToAdd);
        }
        return domainVotes;
    }

    @Override
    public void deleteAllByReview(String reviewId) {
        this.voteRepository.deleteAllByReviewId(reviewId);
    }
}
