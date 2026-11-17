package com.smartsugar.backend.service;

import com.smartsugar.backend.dto.AnalyticsReportRequest;
import com.smartsugar.backend.dto.AnalyticsReportRow;
import com.smartsugar.backend.exception.AnalyticsDataUnavailableException;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AnalyticsReportService {

    private final ExcelReportService excelReportService;

    public AnalyticsReportService(
            ExcelReportService excelReportService) {

        this.excelReportService = excelReportService;
    }

    public byte[] generate(AnalyticsReportRequest request)
            throws IOException {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Report request is required."
            );
        }

        /*
         * Actual aggregation will be connected here
         * after CaneSample fields and report dates
         * are finalized.
         *
         * Do not generate an empty production report
         * that could be mistaken for a valid result.
         */

        throw new AnalyticsDataUnavailableException(
                "Report source data is not integrated yet. "
                + "Cane sample schema and production fields "
                + "must be finalized first."
        );
    }

    /*
     * This method allows testing the real Excel exporter
     * independently of database integration.
     */
    public byte[] generateFromRows(
            AnalyticsReportRequest request,
            List<AnalyticsReportRow> rows) throws IOException {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Report request is required."
            );
        }

        return excelReportService.generateReport(
                request.reportType(),
                rows
        );
    }
}