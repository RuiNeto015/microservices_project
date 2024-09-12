package com.isep.acme.applicationServices;

import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.adapters.dto.CreateReviewDTO;
import com.isep.acme.adapters.dto.FetchReviewDTO;
import com.isep.acme.adapters.dto.UpdateReviewDTO;
import com.isep.acme.applicationServices.events.reviews.ReviewCreatedPayload;
import com.isep.acme.applicationServices.interfaces.amqp.IReviewEventSender;
import com.isep.acme.applicationServices.interfaces.http.IFunFactRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IProductRepository;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.domain.aggregates.product.Product;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ReviewService {

    private final IProductRepository productRepository;

    private final IReviewRepository reviewRepository;

    private final IFunFactRepository funFactRepository;

    private final IReviewEventSender reviewEventSender;


    @Transactional
    public FetchReviewDTO create(String userId, CreateReviewDTO createReviewDTO) throws Exception {

        Product product = this.productRepository.findBySku(createReviewDTO.getSku());
        if (product == null) {
            throw new IllegalArgumentException("The product doesn't exist on the DB service");
        }

        if (!product.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
            throw new IllegalArgumentException("The product needs to be approved!");
        }

        String funFact = funFactRepository.fetchFunFact(LocalDate.now());

        Review review = new Review(createReviewDTO.getText(), "", funFact,
                createReviewDTO.getRating(), userId, createReviewDTO.getSku());
        this.reviewRepository.create(review);

        //amqp
        ReviewCreatedPayload reviewCreatedPayload = new ReviewCreatedPayload(review.getId(), review.getText(),
                review.getReport(), review.getPublishingDate(), review.getFunFact(), review.getRating(),
                review.getUserId(), review.getSku(), 0);
        this.reviewEventSender.notifyCreatedEvent(reviewCreatedPayload);

        return this.toReviewDTO(review);
    }

    @Transactional
    public FetchReviewDTO delete(String id) throws DatabaseException {
        Review review = this.reviewRepository.findById(id);
        this.reviewRepository.delete(id);

        //amqp
        this.reviewEventSender.notifyDeletedEvent(id);

        return this.toReviewDTO(review);
    }

    @Transactional
    public FetchReviewDTO update(UpdateReviewDTO updateReviewDTO) throws DatabaseException {
        Review review = this.reviewRepository.findById(updateReviewDTO.getId());
        review.setRating(updateReviewDTO.getRating());
        review.setText(updateReviewDTO.getText());
        this.reviewRepository.update(review);
        return this.toReviewDTO(review);
    }

    @Transactional
    public String approve(String id) throws DatabaseException {
        Review review = this.reviewRepository.findById(id);

        if (review == null) {
            throw new DatabaseException("The review not exists!");
        }

        if (review.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
            throw new DatabaseException("The review is already approved!");
        }

        review.approve();
        this.reviewRepository.update(review);

        //amqp
        this.reviewEventSender.notifyApprovedEvent(id);

        String response;
        if (review.getNumApprovals() == 1) {
            response = "Review approval with success! One approval left to became global acceptable";
        } else {
            response = "Review approved with success! The review is now public acceptable!";
        }
        return response;
    }

    @Transactional
    public void reject(String id, String report) throws DatabaseException {
        Review review = this.reviewRepository.findById(id);

        if (review != null && review.getApprovalStatus().equals(ApprovalStatusEnum.Approved)) {
            throw new DatabaseException("The review is already approved!");
        }

        if (review != null && review.getApprovalStatus().equals(ApprovalStatusEnum.Rejected)) {
            throw new DatabaseException("The review is already rejected!");
        }

        review.reject(report);
        this.reviewRepository.update(review);

        //amqp
        this.reviewEventSender.notifyRejectedEvent(id, report);
    }

    private FetchReviewDTO toReviewDTO(Review r) {
        return new FetchReviewDTO(r.getId(), r.getApprovalStatus().name(), r.getText(), r.getReport(),
                r.getPublishingDate(), r.getFunFact(), r.getRating(), r.getUserId(), r.getSku(), r.getVotes());
    }
}
