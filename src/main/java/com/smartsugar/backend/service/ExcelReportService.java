package com.smartsugar.backend.service;

import com.smartsugar.backend.dto.AnalyticsReportRow;
import com.smartsugar.backend.dto.AnalyticsReportType;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ExcelReportService {

    private static final String[] HEADERS = {
            "Group Code",
            "Group Name",
            "Total Samples",
            "Average Brix",
            "Average Pol",
            "Average Purity"
    };

    public byte[] generateReport(
            AnalyticsReportType type,
            List<AnalyticsReportRow> rows) throws IOException {

        Objects.requireNonNull(type, "Report type is required");
        Objects.requireNonNull(rows, "Report rows cannot be null");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100);
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);

            try {
                Sheet sheet = workbook.createSheet("Analytics");

                CellStyle headerStyle = createHeaderStyle(workbook);
                CellStyle numberStyle = createNumberStyle(workbook);

                Row titleRow = sheet.createRow(0);
                titleRow.createCell(0).setCellValue(
                        type.name().replace('_', ' ') + " ANALYTICS"
                );

                Row headerRow = sheet.createRow(2);

                for (int i = 0; i < HEADERS.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(HEADERS[i]);
                    cell.setCellStyle(headerStyle);
                }

                int rowIndex = 3;

                for (AnalyticsReportRow item : rows) {

                    if (item == null) {
                        throw new IllegalArgumentException(
                                "Report rows cannot contain null entries."
                        );
                    }

                    Row row = sheet.createRow(rowIndex++);

                    setText(row, 0, item.groupCode());
                    setText(row, 1, item.groupName());

                    if (item.totalSamples() != null) {
                        row.createCell(2).setCellValue(
                                item.totalSamples().doubleValue()
                        );
                    }

                    setDecimal(row, 3, item.averageBrix(), numberStyle);
                    setDecimal(row, 4, item.averagePol(), numberStyle);
                    setDecimal(row, 5, item.averagePurity(), numberStyle);
                }

                for (int i = 0; i < HEADERS.length; i++) {
                    sheet.setColumnWidth(i, 22 * 256);
                }

                sheet.createFreezePane(0, 3);

                workbook.write(output);
                return output.toByteArray();

            } finally {
                workbook.dispose();
            }
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);
        style.setWrapText(true);

        return style;
    }

    private CellStyle createNumberStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("0.000"));

        return style;
    }

    private void setText(Row row, int column, String value) {

        row.createCell(column).setCellValue(
                value == null ? "" : value
        );
    }

    private void setDecimal(
            Row row,
            int column,
            BigDecimal value,
            CellStyle style) {

        Cell cell = row.createCell(column);

        if (value != null) {
            cell.setCellValue(value.doubleValue());
            cell.setCellStyle(style);
        }
    }
}