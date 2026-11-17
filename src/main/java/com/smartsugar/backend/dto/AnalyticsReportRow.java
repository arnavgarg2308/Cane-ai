package com.smartsugar.backend.dto;

import java.math.BigDecimal;

public record AnalyticsReportRow(
        String groupCode,
        String groupName,
        Long totalSamples,
        BigDecimal averageBrix,
        BigDecimal averagePol,
        BigDecimal averagePurity
) {

    public AnalyticsReportRow {

        if (totalSamples != null && totalSamples < 0) {
            throw new IllegalArgumentException(
                    "Total samples cannot be negative."
            );
        }
    }
}