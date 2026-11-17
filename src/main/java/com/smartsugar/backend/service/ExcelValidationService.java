
package com.smartsugar.backend.service;

import com.smartsugar.backend.dto.ValidationIssue;
import com.smartsugar.backend.dto.ValidationReport;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExcelValidationService {

    public ValidationReport validateRows(
            List<Map<String, String>> records,
            List<Integer> rowNumbers) {

        List<ValidationIssue> issues = new ArrayList<>();

        Set<Integer> invalidRowNumbers = new HashSet<>();

        for (int i = 0; i < records.size(); i++) {

            Map<String, String> record = records.get(i);

            int rowNumber = rowNumbers.get(i);

            // Required text fields
            checkRequired(record, rowNumber,
                    "village code", "Village Code",
                    issues, invalidRowNumbers);

            checkRequired(record, rowNumber,
                    "zone", "Zone",
                    issues, invalidRowNumbers);

            checkRequired(record, rowNumber,
                    "village center", "Village/Center",
                    issues, invalidRowNumbers);

            checkRequired(record, rowNumber,
                    "category of cane", "Category of cane",
                    issues, invalidRowNumbers);

            checkRequired(record, rowNumber,
                    "r p", "R/P",
                    issues, invalidRowNumbers);

            checkRequired(record, rowNumber,
                    "variety", "Variety",
                    issues, invalidRowNumbers);

            // Numeric fields
            checkNumeric(record, rowNumber,
                    "s no", "S. No.",
                    issues, invalidRowNumbers);

            checkNumeric(record, rowNumber,
                    "brix", "Brix",
                    issues, invalidRowNumbers);

            checkNumeric(record, rowNumber,
                    "pol", "Pol",
                    issues, invalidRowNumbers);

            checkNumeric(record, rowNumber,
                    "purity", "Purity",
                    issues, invalidRowNumbers);

            checkNumeric(record, rowNumber,
                    "pol cane", "Pol % Cane",
                    issues, invalidRowNumbers);
        }

        int totalRows = records.size();

        int invalidRows = invalidRowNumbers.size();

        int validRows = totalRows - invalidRows;

        return new ValidationReport(
                totalRows,
                validRows,
                invalidRows,
                issues
        );
    }

    private void checkRequired(
            Map<String, String> record,
            int rowNumber,
            String normalizedColumn,
            String displayName,
            List<ValidationIssue> issues,
            Set<Integer> invalidRows) {

        String value = getValue(record, normalizedColumn);

        if (value == null || value.trim().isEmpty()) {

            addIssue(
                    rowNumber,
                    displayName,
                    displayName + " is missing",
                    issues,
                    invalidRows
            );
        }
    }

    private void checkNumeric(
            Map<String, String> record,
            int rowNumber,
            String normalizedColumn,
            String displayName,
            List<ValidationIssue> issues,
            Set<Integer> invalidRows) {

        String value = getValue(record, normalizedColumn);

        if (value == null || value.trim().isEmpty()) {

            addIssue(
                    rowNumber,
                    displayName,
                    displayName + " is missing",
                    issues,
                    invalidRows
            );

            return;
        }

        try {
            double number = Double.parseDouble(value.trim());

            if (!Double.isFinite(number)) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            addIssue(
                    rowNumber,
                    displayName,
                    displayName + " must be numeric",
                    issues,
                    invalidRows
            );
        }
    }

    private String getValue(
            Map<String, String> record,
            String normalizedColumn) {

        for (Map.Entry<String, String> entry : record.entrySet()) {

            String key = entry.getKey()
                    .toLowerCase()
                    .replace("%", "")
                    .replaceAll("[^a-z0-9]+", " ")
                    .trim();

            if (key.equals(normalizedColumn)) {
                return entry.getValue();
            }
        }

        return null;
    }

    private void addIssue(
            int rowNumber,
            String columnName,
            String message,
            List<ValidationIssue> issues,
            Set<Integer> invalidRows) {

        issues.add(new ValidationIssue(
                rowNumber,
                columnName,
                message,
                "ERROR"
        ));

        invalidRows.add(rowNumber);
    }
}
