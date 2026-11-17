package com.smartsugar.backend.service;

import com.smartsugar.backend.dto.AnalyticsReportRequest;
import com.smartsugar.backend.dto.AnalyticsReportRow;
import com.smartsugar.backend.dto.AnalyticsReportType;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalyticsReportServiceTest {

    private final AnalyticsReportService reportService =
            new AnalyticsReportService(
                    new ExcelReportService()
            );

    @Test
    void shouldGenerateReadableExcelWorkbook() throws Exception {

        AnalyticsReportRequest request =
                new AnalyticsReportRequest(
                        AnalyticsReportType.VILLAGE,
                        null,
                        null
                );

        List<AnalyticsReportRow> rows = List.of(
                new AnalyticsReportRow(
                        "TEST001",
                        "Test Village",
                        10L,
                        new BigDecimal("18.50"),
                        new BigDecimal("14.20"),
                        new BigDecimal("76.75")
                )
        );

        byte[] result =
                reportService.generateFromRows(request, rows);

        assertNotNull(result);
        assertTrue(result.length > 0);

        try (Workbook workbook = WorkbookFactory.create(
                new ByteArrayInputStream(result))) {

            Sheet sheet = workbook.getSheet("Analytics");

            assertNotNull(sheet);

            assertEquals(
                    "Group Code",
                    sheet.getRow(2).getCell(0).getStringCellValue()
            );

            assertEquals(
                    "TEST001",
                    sheet.getRow(3).getCell(0).getStringCellValue()
            );

            assertEquals(
                    "Test Village",
                    sheet.getRow(3).getCell(1).getStringCellValue()
            );

            assertEquals(
                    10.0,
                    sheet.getRow(3).getCell(2).getNumericCellValue()
            );

            assertEquals(
                    18.50,
                    sheet.getRow(3).getCell(3).getNumericCellValue(),
                    0.001
            );
        }
    }

    @Test
    void shouldRejectInvalidDateRange() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new AnalyticsReportRequest(
                        AnalyticsReportType.VILLAGE,
                        java.time.LocalDate.of(2026, 11, 20),
                        java.time.LocalDate.of(2026, 11, 10)
                )
        );
    }

    @Test
    void shouldNotInventDatabaseReportData() {

        AnalyticsReportRequest request =
                new AnalyticsReportRequest(
                        AnalyticsReportType.VILLAGE,
                        null,
                        null
                );

        assertThrows(
                com.smartsugar.backend.exception
                        .AnalyticsDataUnavailableException.class,
                () -> reportService.generate(request)
        );
    }
}