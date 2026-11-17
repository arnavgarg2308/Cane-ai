package com.smartsugar.backend.dto;

import java.time.LocalDate;

public record AnalyticsReportRequest(
        AnalyticsReportType reportType,
        LocalDate fromDate,
        LocalDate toDate
) {

    public AnalyticsReportRequest {

        if (reportType == null) {
            throw new IllegalArgumentException(
                    "Report type is required."
            );
        }

        if (fromDate != null
                && toDate != null
                && fromDate.isAfter(toDate)) {

            throw new IllegalArgumentException(
                    "fromDate cannot be after toDate."
            );
        }
    }
}