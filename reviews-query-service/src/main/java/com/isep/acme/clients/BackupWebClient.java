package com.isep.acme.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class BackupWebClient {

    private final WebClient webClient;

    public ResponseEntity<String> requestProductsInit() {

        return webClient
                .get()
                .uri("/products/init/reviewsService")
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    public ResponseEntity<String> requestReviewsInit() {

        return webClient
                .get()
                .uri("/reviews/init/reviewsService")
                .retrieve()
                .toEntity(String.class)
                .block();
    }

    public ResponseEntity<String> requestUsersInit() {

        return webClient
                .get()
                .uri("/admin/user/init/allServices")
                .retrieve()
                .toEntity(String.class)
                .block();
    }
}
