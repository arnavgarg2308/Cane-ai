
package com.smartsugar.backend.controller;

import com.smartsugar.backend.dto.AnalyticsApiResponse;
import com.smartsugar.backend.dto.AnalyticsFilterRequest;

import com.smartsugar.backend.service.AnalyticsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(
            AnalyticsService analyticsService) {

        this.analyticsService = analyticsService;
    }

    @GetMapping("/village-wise")
    public ResponseEntity<AnalyticsApiResponse> getVillageAnalytics(
            @ModelAttribute AnalyticsFilterRequest filters) {

        return notImplemented(
                analyticsService.getVillageAnalytics()
        );
    }

    @GetMapping("/centre-wise")
    public ResponseEntity<AnalyticsApiResponse> getCentreAnalytics(
            @ModelAttribute AnalyticsFilterRequest filters) {

        return notImplemented(
                analyticsService.getCentreAnalytics()
        );
    }

    @GetMapping("/variety-wise")
    public ResponseEntity<AnalyticsApiResponse> getVarietyAnalytics(
            @ModelAttribute AnalyticsFilterRequest filters) {

        return notImplemented(
                analyticsService.getVarietyAnalytics()
        );
    }

    @GetMapping("/daily")
    public ResponseEntity<AnalyticsApiResponse> getDailyAnalytics(
            @ModelAttribute AnalyticsFilterRequest filters) {

        return notImplemented(
                analyticsService.getDailyAnalytics()
        );
    }

    @GetMapping("/seasonal")
    public ResponseEntity<AnalyticsApiResponse> getSeasonalAnalytics(
            @ModelAttribute AnalyticsFilterRequest filters) {

        return notImplemented(
                analyticsService.getSeasonalAnalytics()
        );
    }

    @GetMapping("/area-wise-yield")
    public ResponseEntity<AnalyticsApiResponse> getAreaYieldAnalytics(
            @ModelAttribute AnalyticsFilterRequest filters) {

        return notImplemented(
                analyticsService.getAreaYieldAnalytics()
        );
    }

    private ResponseEntity<AnalyticsApiResponse> notImplemented(
            AnalyticsApiResponse response) {

        return ResponseEntity
                .status(HttpStatus.NOT_IMPLEMENTED)
                .body(response);
    }
}
