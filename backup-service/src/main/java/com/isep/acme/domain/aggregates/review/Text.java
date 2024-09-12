package com.isep.acme.domain.aggregates.review;

import lombok.Getter;

@Getter
public class Text {

    private String text;

    public Text(String text) {
        this.setText(text);
    }

    private void setText(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Review Text is a mandatory attribute of Review.");
        }
        if (text.length() > 2048) {
            throw new IllegalArgumentException("Review Text must not be greater than 2048 characters.");
        }

        this.text = text;
    }
}
