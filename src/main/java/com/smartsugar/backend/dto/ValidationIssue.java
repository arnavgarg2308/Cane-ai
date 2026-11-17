
package com.smartsugar.backend.dto;

public class ValidationIssue {

    private int rowNumber;
    private String columnName;
    private String errorMessage;
    private String severity;

    public ValidationIssue(
            int rowNumber,
            String columnName,
            String errorMessage,
            String severity) {

        this.rowNumber = rowNumber;
        this.columnName = columnName;
        this.errorMessage = errorMessage;
        this.severity = severity;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getSeverity() {
        return severity;
    }
}
