package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.dto.FetchReviewDTO;
import com.isep.acme.applicationServices.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@RequiredArgsConstructor
class ReviewController {

    private final ReviewService service;

    @Operation(summary = "gets review by user")
    @GetMapping("/reviews/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable(value = "userId") final String userId,
                                       @RequestParam(required = false, defaultValue = "0") int page) {
        Page<FetchReviewDTO> reviews = this.service.findByUser(userId, page);
        return ResponseEntity.ok().body(reviews);
    }

    @Operation(summary = "gets pending reviews")
    @GetMapping("/reviews/pending")
    public ResponseEntity<?> getPending(@RequestParam(required = false, defaultValue = "0") int page) {
        Page<FetchReviewDTO> reviews = this.service.findPending(page);
        return ResponseEntity.ok().body(reviews);
    }

    @Operation(summary = "gets recommended reviews")
    @GetMapping("/reviews/recommended")
    public ResponseEntity<?> getRecommendations(Authentication authentication,
                                                @RequestParam(required = false, defaultValue = "0") int page) {
        Page<FetchReviewDTO> reviews = this.service.getRecommendations(authentication.getName().replaceAll(",null", ""), page);
        return ResponseEntity.ok().body(reviews);
    }

}
