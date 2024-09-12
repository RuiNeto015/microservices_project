package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateReviewDTO;
import com.isep.acme.adapters.dto.FetchReviewDTO;
import com.isep.acme.applicationServices.ReviewService;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@RequiredArgsConstructor
class ReviewController {

    private final ReviewService service;

    @Operation(summary = "gets review by user")
    @GetMapping("/reviews/{userId}")
    public ResponseEntity<List<FetchReviewDTO>> getByUser(@PathVariable(value = "userId") final String userId) {
        List<FetchReviewDTO> reviews = this.service.findByUser(userId);
        return ResponseEntity.ok().body(reviews);
    }

    @Operation(summary = "gets pending reviews")
    @GetMapping("/reviews/pending")
    public ResponseEntity<List<FetchReviewDTO>> getPending() {
        List<FetchReviewDTO> reviews = this.service.findPending();
        return ResponseEntity.ok().body(reviews);
    }

    @Operation(summary = "gets recommended reviews")
    @GetMapping("/reviews/recommended")
    public ResponseEntity<List<FetchReviewDTO>> getRecommendations(Authentication authentication) {
        List<FetchReviewDTO> reviews = this.service.getRecommendations(authentication.getName().replaceAll(",null", ""));
        return ResponseEntity.ok().body(reviews);
    }

}
