package com.isep.acme.bootstrapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.isep.acme.adapters.database.repositories.DatabaseException;
import com.isep.acme.applicationServices.interfaces.repositories.IReviewRepository;
import com.isep.acme.clients.BackupWebClient;
import com.isep.acme.clients.dtos.ResponseReview;
import com.isep.acme.domain.aggregates.review.Review;
import com.isep.acme.domain.enums.ApprovalStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewBootstrapper {

    private final IReviewRepository reviewRepo;

    @Autowired
    private BackupWebClient backupWebClient;

    public void run() throws IllegalArgumentException, DatabaseException {

        Gson gson = new Gson();

        //Save reviews

        ResponseEntity<String> backupReviewRequestResponse;
        try {
            backupReviewRequestResponse = this.backupWebClient.requestReviewsInit();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error communicating with the backup service!");
        }

        if (backupReviewRequestResponse.getStatusCode() != HttpStatus.OK) {
            throw new IllegalArgumentException("Error communicating with the backup service! Response Status isn't OK");
        }

        TypeToken<List<ResponseReview>> token = new TypeToken<List<ResponseReview>>() {
        };
        String reviewsResponseBody = backupReviewRequestResponse.getBody();
        List<ResponseReview> reviewsList = gson.fromJson(reviewsResponseBody, token.getType());

        if (reviewsList == null || reviewsList.isEmpty()) {
            System.out.println("No reviews on the backup service. Everything fine!");
        } else {
            for (ResponseReview r : reviewsList) {
                Review review = new Review(r.getId(), ApprovalStatusEnum.valueOf(r.getApprovalStatus()),
                        r.getNumApprovals(), r.getUserId());
                if (!this.reviewRepo.exists(r.getId())) {
                    this.reviewRepo.create(review);
                }
            }
        }
    }
}
