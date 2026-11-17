
package com.smartsugar.backend.service;

import com.smartsugar.backend.dto.ValidationReport;

import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ExcelService {

    private final DataFormatter formatter = new DataFormatter();

    private final ExcelValidationService validationService;

    public ExcelService(ExcelValidationService validationService) {
        this.validationService = validationService;
    }

    public Map<String, Object> readExcel(MultipartFile file)
            throws IOException {

        Map<String, Object> result = new LinkedHashMap<>();

        try (Workbook workbook =
                     WorkbookFactory.create(file.getInputStream())) {

            List<Map<String, Object>> sheets = new ArrayList<>();

            int totalDataRows = 0;

            for (Sheet sheet : workbook) {

                int headerRowIndex = findHeaderRow(sheet);

                if (headerRowIndex == -1) {
                    throw new IOException(
                            "Could not identify header row in sheet: "
                                    + sheet.getSheetName()
                    );
                }

                Row headerRow = sheet.getRow(headerRowIndex);

                List<String> headers = new ArrayList<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {

                    Cell cell = headerRow.getCell(
                            j,
                            Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
                    );

                    headers.add(
                            cell == null
                                    ? ""
                                    : formatter.formatCellValue(cell).trim()
                    );
                }

                List<Map<String, String>> preview = new ArrayList<>();

                List<Map<String, String>> allRecords = new ArrayList<>();

                List<Integer> rowNumbers = new ArrayList<>();

                int dataRowCount = 0;

                for (int i = headerRowIndex + 1;
                     i <= sheet.getLastRowNum(); i++) {

                    Row row = sheet.getRow(i);

                    if (row == null || isEmptyRow(row)) {
                        continue;
                    }

                    dataRowCount++;

                    Map<String, String> record = new LinkedHashMap<>();

                    for (int j = 0; j < headers.size(); j++) {

                        Cell cell = row.getCell(
                                j,
                                Row.MissingCellPolicy.RETURN_BLANK_AS_NULL
                        );

                        String value = cell == null
                                ? ""
                                : formatter.formatCellValue(cell).trim();

                        record.put(headers.get(j), value);
                    }

                    allRecords.add(record);

                    // Excel row numbers are 1-based
                    rowNumbers.add(i + 1);

                    if (preview.size() < 5) {
                        preview.add(record);
                    }
                }

                totalDataRows += dataRowCount;

                // Validate all records in this sheet
                ValidationReport validationReport =
                        validationService.validateRows(
                                allRecords,
                                rowNumbers
                        );

                Map<String, Object> sheetData = new LinkedHashMap<>();

                sheetData.put("sheetName", sheet.getSheetName());
                sheetData.put("headerRowNumber", headerRowIndex + 1);
                sheetData.put("headers", headers);
                sheetData.put("dataRowCount", dataRowCount);
                sheetData.put("preview", preview);
                sheetData.put("validationReport", validationReport);

                sheets.add(sheetData);
            }

            result.put("fileName", file.getOriginalFilename());
            result.put("totalSheets", workbook.getNumberOfSheets());
            result.put("totalDataRows", totalDataRows);
            result.put("sheets", sheets);
        }

        return result;
    }

    private int findHeaderRow(Sheet sheet) {

        for (int i = 0; i <= sheet.getLastRowNum(); i++) {

            Row row = sheet.getRow(i);

            if (row == null) {
                continue;
            }

            Set<String> values = new HashSet<>();

            for (Cell cell : row) {

                values.add(
                        normalize(formatter.formatCellValue(cell))
                );
            }

            if (values.contains("village code")
                    && values.contains("zone")
                    && values.contains("brix")
                    && values.contains("pol")
                    && values.contains("purity")
                    && values.contains("pol cane")) {

                return i;
            }
        }

        return -1;
    }

    private String normalize(String value) {

        return value.toLowerCase()
                .replace("%", "")
                .replaceAll("[^a-z0-9]+", " ")
                .trim();
    }

    private boolean isEmptyRow(Row row) {

        for (Cell cell : row) {

            if (!formatter.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }

        return true;
    }
}
