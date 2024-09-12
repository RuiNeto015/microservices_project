package com.isep.acme.domain.aggregates.review;

import lombok.Getter;

@Getter
public class Report {

    private String report;

    public Report(String report) {
        this.setReport(report);
    }

    private void setReport(String report) {
        if (report.length() > 2048) {
            throw new IllegalArgumentException("Report must not be greater than 2048 characters.");
        }
        this.report = report;
    }
}
