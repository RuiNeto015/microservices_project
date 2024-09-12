package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.dto.VoteForVoteServiceDTO;
import com.isep.acme.applicationServices.VoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Vote", description = "Endpoints for managing votes")
@RestController
@RequestMapping(path = "/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @GetMapping("/init/votesService")
    public ResponseEntity<?> findAllForVotesService() {
        List<VoteForVoteServiceDTO> votesToSend = this.voteService.findAllForVoteService();
        return ResponseEntity.status(HttpStatus.OK).body(votesToSend);
    }
}
