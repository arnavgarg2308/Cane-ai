package com.smartsugar.backend.controller;

import com.smartsugar.backend.dto.AnalyticsReportRequest;
import com.smartsugar.backend.dto.AnalyticsReportType;

import com.smartsugar.backend.exception.AnalyticsDataUnavailableException;
import com.smartsugar.backend.service.AnalyticsReportService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class AnalyticsReportController {

    private static final MediaType XLSX_TYPE = MediaType.parseMediaType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    );

    private final AnalyticsReportService reportService;

    public AnalyticsReportController(
            AnalyticsReportService reportService) {

        this.reportService = reportService;
    }

    @GetMapping("/excel")
    public ResponseEntity<?> downloadReport(
            @RequestParam AnalyticsReportType type,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate)
            throws IOException {

        AnalyticsReportRequest request =
                new AnalyticsReportRequest(
                        type,
                        fromDate,
                        toDate
                );

        try {

            byte[] report = reportService.generate(request);

            String filename = "smartsugar-"
                    + type.name().toLowerCase()
                    + "-report.xlsx";

            return ResponseEntity.ok()
                    .contentType(XLSX_TYPE)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\""
                    )
                    .contentLength(report.length)
                    .body(report);

        } catch (AnalyticsDataUnavailableException exception) {

            Map<String, Object> response = new LinkedHashMap<>();

            response.put("status", 501);
            response.put("error", "Not Implemented");
            response.put("message", exception.getMessage());
            response.put("reportType", type.name());

            return ResponseEntity
                    .status(HttpStatus.NOT_IMPLEMENTED)
                    .body(response);
        }
    }
}