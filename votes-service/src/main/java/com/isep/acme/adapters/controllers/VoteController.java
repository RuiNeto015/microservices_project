package com.isep.acme.adapters.controllers;

import com.isep.acme.applicationServices.VoteService;
import com.isep.acme.domain.aggregates.votes.Vote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Vote", description = "Endpoints for managing Vote")
@RestController
@RequiredArgsConstructor
class VoteController {

    private final VoteService service;

    @Operation(summary = "add vote")
    @PutMapping("/reviews/{reviewID}/vote")
    public ResponseEntity<String> addVote(@PathVariable(value = "reviewID") final String reviewID,
                                          @RequestBody String vote,
                                          Authentication authentication) throws Exception {
        String response = this.service.vote(authentication.getName().replaceAll(",null", ""), reviewID, vote);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "get vote")
    @GetMapping("/reviews/{reviewID}/vote")
    public ResponseEntity<List<Vote>> getVotes(@PathVariable(value = "reviewID") final String reviewID) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.service.getVotesByReview(reviewID));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
