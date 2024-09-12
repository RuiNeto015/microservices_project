package com.isep.acme.adapters.http;

import com.isep.acme.applicationServices.interfaces.http.IFunFactRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Component
public class FunFactRepository implements IFunFactRepository {

    private final RestTemplate restTemplate;

    public FunFactRepository(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public String fetchFunFact(LocalDate date) {
        String endpoint = "http://numbersapi.com/{month}/{day}/date";
        return this.restTemplate.getForObject(endpoint, String.class, date.getMonthValue(),date.getDayOfMonth());
    }
}
