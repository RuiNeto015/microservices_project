package com.isep.acme.adapters.controllers;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateReviewDTO;
import com.isep.acme.adapters.dto.FetchReviewDTO;
import com.isep.acme.applicationServices.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
@RequiredArgsConstructor
class ReviewController {

    private final ReviewService service;

    @Operation(summary = "creates review")
    @PostMapping("/products/reviews")
    public ResponseEntity<FetchReviewDTO> create(@RequestBody CreateReviewDTO createReviewDTO,
                                                 Authentication authentication) throws Exception {
        FetchReviewDTO review = this.service.create(authentication.getName().replaceAll(",null", ""), createReviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @Operation(summary = "deletes review")
    @DeleteMapping("/reviews/{reviewID}")
    public ResponseEntity<FetchReviewDTO> delete(@PathVariable(value = "reviewID") final String reviewID) throws DatabaseException {
        FetchReviewDTO review = this.service.delete(reviewID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(review);
    }

    @Operation(summary = "Approve review")
    @PutMapping("/reviews/approve/{reviewID}")
    public ResponseEntity<String> approve(@PathVariable(value = "reviewID") final String reviewID) throws DatabaseException {
        String response = this.service.approve(reviewID);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(summary = "Reject review")
    @PutMapping("/reviews/reject/{reviewID}")
    public ResponseEntity<String> reject(@PathVariable(value = "reviewID") final String reviewID,
                                         @RequestBody String report) throws DatabaseException {
        this.service.reject(reviewID, report);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Review rejected with success");
    }
}
