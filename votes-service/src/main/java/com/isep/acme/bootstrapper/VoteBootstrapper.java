package com.isep.acme.bootstrapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.interfaces.repositories.IVoteRepository;
import com.isep.acme.clients.BackupWebClient;
import com.isep.acme.clients.dtos.ResponseVote;
import com.isep.acme.domain.aggregates.votes.Vote;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VoteBootstrapper {

    private final IVoteRepository voteRepo;

    @Autowired
    private BackupWebClient backupWebClient;

    public void run() throws IllegalArgumentException, DatabaseException {

        Gson gson = new Gson();

        //Save votes

        ResponseEntity<String> backupVoteRequestResponse;
        try {
            backupVoteRequestResponse = this.backupWebClient.requestVotesInit();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error communicating with the backup service!");
        }

        if (backupVoteRequestResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Error communicating with the backup service! Response Status isn't OK");
        }

        TypeToken<List<ResponseVote>> token = new TypeToken<List<ResponseVote>>() {
        };
        String votesResponseBody = backupVoteRequestResponse.getBody();
        List<ResponseVote> votesList = gson.fromJson(votesResponseBody, token.getType());

        if (votesList == null || votesList.isEmpty()) {
            System.out.println("No votes on the backup service. Everything fine!");
        } else {
            for (ResponseVote v : votesList) {
                Vote vote = new Vote(v.getUserId(), v.getReviewId(), v.getVote());
                if (this.voteRepo.findByUserAndReview(v.getUserId(), v.getReviewId()) == null) {
                    this.voteRepo.create(vote);
                }
            }
        }
    }
}
