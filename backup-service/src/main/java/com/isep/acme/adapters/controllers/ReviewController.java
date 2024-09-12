package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.dto.ReviewForReviewsServiceDTO;
import com.isep.acme.adapters.dto.ReviewForVotesServiceDTO;
import com.isep.acme.applicationServices.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
class ReviewController {

    private final ReviewService service;

    @Operation(summary = "get all reviews for reviews service")
    @GetMapping("/init/reviewsService")
    public ResponseEntity<List<ReviewForReviewsServiceDTO>> getAllForReviewsService() {
        List<ReviewForReviewsServiceDTO> reviewsToSend = this.service.findAllForReviewsService();
        return ResponseEntity.ok().body(reviewsToSend);
    }

    @Operation(summary = "get all reviews for votes service")
    @GetMapping("/init/votesService")
    public ResponseEntity<List<ReviewForVotesServiceDTO>> getAllForVotesService() {
        List<ReviewForVotesServiceDTO> reviewsToSend = this.service.findAllForVotesService();
        return ResponseEntity.ok().body(reviewsToSend);
    }
}
