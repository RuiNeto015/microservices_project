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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    public Page<FetchReviewDTO> findByUser(String userId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);
        List<Review> reviews = this.reviewRepository.findByUser(userId);

        List<FetchReviewDTO> reviewDTOS = this.toReviewDTOList(reviews);

        return new PageImpl<>(reviewDTOS, pageRequest, reviews.size());
    }

    @Transactional
    public Page<FetchReviewDTO> findPending(int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        List<Review> reviews = this.reviewRepository.findPending();

        List<FetchReviewDTO> reviewDTOS = this.toReviewDTOList(reviews);

        return new PageImpl<>(reviewDTOS, pageRequest, reviews.size());
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
    public Page<FetchReviewDTO> getRecommendations(String userId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 10);

        var reviews = reviewsRecommendations.getReviewsRecommendation(userId, 10);

        List<FetchReviewDTO> reviewDTOS = this.toReviewDTOList(reviews);

        return new PageImpl<>(reviewDTOS, pageRequest, reviews.size());
    }

    private FetchReviewDTO toReviewDTO(Review r) {
        return new FetchReviewDTO(r.getId(), r.getApprovalStatus().name(), r.getText(), r.getReport(),
                r.getPublishingDate(), r.getFunFact(), r.getRating(), r.getUserId(), r.getSku());
    }
}
