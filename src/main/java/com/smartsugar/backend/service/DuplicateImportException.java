
package com.smartsugar.backend.service;

public class DuplicateImportException extends RuntimeException {

    private final Long existingImportId;

    public DuplicateImportException(
            String message,
            Long existingImportId) {

        super(message);

        this.existingImportId = existingImportId;
    }

    public Long getExistingImportId() {
        return existingImportId;
    }
}
