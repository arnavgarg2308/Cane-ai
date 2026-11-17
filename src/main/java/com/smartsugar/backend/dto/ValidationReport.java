
package com.smartsugar.backend.dto;

import java.util.List;

public class ValidationReport {

    private int totalRows;
    private int validRows;
    private int invalidRows;
    private List<ValidationIssue> issues;

    public ValidationReport(
            int totalRows,
            int validRows,
            int invalidRows,
            List<ValidationIssue> issues) {

        this.totalRows = totalRows;
        this.validRows = validRows;
        this.invalidRows = invalidRows;
        this.issues = issues;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getValidRows() {
        return validRows;
    }

    public int getInvalidRows() {
        return invalidRows;
    }

    public List<ValidationIssue> getIssues() {
        return issues;
    }
}
