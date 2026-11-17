
package com.smartsugar.backend.service;

import com.smartsugar.backend.dto.AnalyticsApiResponse;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AnalyticsService {

    public AnalyticsApiResponse getVillageAnalytics() {

        return notReady(
                "/api/analytics/village-wise",
                "Village code, village name and cane sample data."
        );
    }

    public AnalyticsApiResponse getCentreAnalytics() {

        return notReady(
                "/api/analytics/centre-wise",
                "Centre information and cane sample data."
        );
    }

    public AnalyticsApiResponse getVarietyAnalytics() {

        return notReady(
                "/api/analytics/variety-wise",
                "Cane variety information and cane sample data."
        );
    }

    public AnalyticsApiResponse getDailyAnalytics() {

        return notReady(
                "/api/analytics/daily",
                "A confirmed report date field."
        );
    }

    public AnalyticsApiResponse getSeasonalAnalytics() {

        return notReady(
                "/api/analytics/seasonal",
                "Confirmed season definition and report date."
        );
    }

    public AnalyticsApiResponse getAreaYieldAnalytics() {

        return notReady(
                "/api/analytics/area-wise-yield",
                "Confirmed area, production fields and measurement units."
        );
    }

    private AnalyticsApiResponse notReady(
            String endpoint,
            String pendingRequirement) {

        return new AnalyticsApiResponse(
                LocalDateTime.now(),
                501,
                "Analytics data integration is pending.",
                endpoint,
                pendingRequirement
        );
    }
}
