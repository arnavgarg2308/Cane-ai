package com.smartsugar.backend.service;

import com.smartsugar.backend.dto.AnalyticsFilterRequest;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsQueryValidator {

    public void validate(AnalyticsFilterRequest filter) {

        if (filter == null) {
            throw new IllegalArgumentException(
                    "Analytics filters cannot be null."
            );
        }

        if (filter.getFromDate() != null
                && filter.getToDate() != null
                && filter.getFromDate().isAfter(filter.getToDate())) {

            throw new IllegalArgumentException(
                    "fromDate cannot be after toDate."
            );
        }

        if (filter.getPage() == null || filter.getPage() < 0) {
            throw new IllegalArgumentException(
                    "Page must be zero or greater."
            );
        }

        if (filter.getSize() == null
                || filter.getSize() < 1
                || filter.getSize() > 100) {

            throw new IllegalArgumentException(
                    "Size must be between 1 and 100."
            );
        }

        validateText(filter.getVillageCode(), "Village code");
        validateText(filter.getCentreName(), "Centre name");
        validateText(filter.getVariety(), "Variety");
    }

    private void validateText(String value, String field) {

        if (value != null && value.length() > 150) {
            throw new IllegalArgumentException(
                    field + " cannot exceed 150 characters."
            );
        }
    }
}