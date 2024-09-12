package com.isep.acme.applicationServices;

import com.isep.acme.adapters.dto.ReviewForReviewsServiceDTO;
import com.isep.acme.adapters.dto.ReviewForVotesServiceDTO;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.review.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewService {

    private final IReviewRepository reviewRepository;

    @Transactional
    public List<ReviewForReviewsServiceDTO> findAllForReviewsService() {
        List<Review> reviews = this.reviewRepository.findAll();
        return this.reviewsListToReviewsServiceDTO(reviews);
    }

    @Transactional
    public List<ReviewForVotesServiceDTO> findAllForVotesService() {
        List<Review> reviews = this.reviewRepository.findAll();
        return this.reviewsListToVotesServiceDTO(reviews);
    }

    private List<ReviewForReviewsServiceDTO> reviewsListToReviewsServiceDTO(List<Review> reviews) {
        List<ReviewForReviewsServiceDTO> reviewsDTOS = new ArrayList<>();

        for (Review r : reviews) {
            ReviewForReviewsServiceDTO reviewDTO = new ReviewForReviewsServiceDTO(r.getId(),
                    r.getApprovalStatus().name(), r.getText(), r.getReport(), r.getPublishingDate().toString(),
                    r.getFunFact(), r.getRating(), r.getUserId(), r.getSku(), r.getNumApprovals());
            reviewsDTOS.add(reviewDTO);
        }
        return reviewsDTOS;
    }

    private List<ReviewForVotesServiceDTO> reviewsListToVotesServiceDTO(List<Review> reviews) {
        List<ReviewForVotesServiceDTO> reviewsDTOS = new ArrayList<>();

        for (Review r : reviews) {
            ReviewForVotesServiceDTO reviewDTO = new ReviewForVotesServiceDTO(r.getId(), r.getApprovalStatus().name(),
                    r.getUserId(), r.getNumApprovals());
            reviewsDTOS.add(reviewDTO);
        }
        return reviewsDTOS;
    }
}
