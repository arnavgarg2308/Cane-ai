package com.smartsugar.backend.entity;

public final class ImportStatus {

    private ImportStatus() {
    }

    public static final String RECEIVED = "RECEIVED";

    public static final String PROCESSING = "PROCESSING";

    public static final String VALIDATED = "VALIDATED";

    public static final String COMPLETED = "COMPLETED";

    public static final String FAILED = "FAILED";
}