package com.isep.acme.applicationServices;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateReviewDTO;
import com.isep.acme.adapters.dto.FetchReviewDTO;
import com.isep.acme.adapters.dto.UpdateReviewDTO;
import com.isep.acme.applicationServices.events.reviews.ReviewCreatedPayload;
import com.isep.acme.applicationServices.interfaces.amqp.IReviewEventSender;
import com.isep.acme.applicationServices.interfaces.domain.IReviewsRecommendations;
import com.isep.acme.applicationServices.interfaces.http.IFunFactRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import com.isep.acme.domain.enums.VoteEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewService {

    private final IReviewRepository reviewRepository;

    private final IReviewsRecommendations reviewsRecommendations;

    @Transactional
    public FetchReviewDTO update(UpdateReviewDTO updateReviewDTO) throws DatabaseException {
        Review review = this.reviewRepository.findById(updateReviewDTO.getId());
        review.setRating(updateReviewDTO.getRating());
        review.setText(updateReviewDTO.getText());
        this.reviewRepository.update(review);
        return this.toReviewDTO(review);
    }

    @Transactional
    public List<FetchReviewDTO> findByUser(String userId) {
        List<Review> reviews = this.reviewRepository.findByUser(userId);
        return this.toReviewDTOList(reviews);
    }

    @Transactional
    public List<FetchReviewDTO> findPending() {
        List<Review> reviews = this.reviewRepository.findPending();
        return this.toReviewDTOList(reviews);
    }

    private List<FetchReviewDTO> toReviewDTOList(List<Review> reviews) {
        List<FetchReviewDTO> list = new ArrayList<>();

        for (Review r : reviews) {
            list.add(new FetchReviewDTO(r.getId(), r.getApprovalStatus().name(), r.getText(), r.getReport(),
                    r.getPublishingDate(), r.getFunFact(), r.getRating(), r.getUserId(), r.getSku()));
        }

        return list;
    }

    @Transactional
    public List<FetchReviewDTO> getRecommendations(String userId) {
        return toReviewDTOList(reviewsRecommendations.getReviewsRecommendation(userId, 10));
    }

    private FetchReviewDTO toReviewDTO(Review r) {
        return new FetchReviewDTO(r.getId(), r.getApprovalStatus().name(), r.getText(), r.getReport(),
                r.getPublishingDate(), r.getFunFact(), r.getRating(), r.getUserId(), r.getSku());
    }
}
