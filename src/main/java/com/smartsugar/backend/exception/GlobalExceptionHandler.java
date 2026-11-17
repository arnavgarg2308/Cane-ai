package com.smartsugar.backend.exception;

import com.smartsugar.backend.service.DuplicateImportException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private Map<String, Object> createError(
            String message,
            HttpStatus status) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return response;
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleFileSizeError(
            MaxUploadSizeExceededException exception) {

        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(createError(
                        "Uploaded file exceeds the allowed size.",
                        HttpStatus.PAYLOAD_TOO_LARGE
                ));
    }

    @ExceptionHandler(DuplicateImportException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateImport(
            DuplicateImportException exception) {

        Map<String, Object> response = createError(
                exception.getMessage(),
                HttpStatus.CONFLICT
        );

        response.put(
                "existingImportId",
                exception.getExistingImportId()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(
            IllegalArgumentException exception) {

        return ResponseEntity
                .badRequest()
                .body(createError(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidState(
            IllegalStateException exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createError(
                        exception.getMessage(),
                        HttpStatus.CONFLICT
                ));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Map<String, Object>> handleExcelReadError(
            IOException exception) {

        log.warn("Unable to read uploaded Excel workbook", exception);

        return ResponseEntity
                .badRequest()
                .body(createError(
                        "Unable to read the Excel workbook. Please check the file.",
                        HttpStatus.BAD_REQUEST
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDatabaseConflict(
            DataIntegrityViolationException exception) {

        log.warn("Database integrity conflict during import", exception);

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createError(
                        "The import conflicts with an existing database record.",
                        HttpStatus.CONFLICT
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedError(
            Exception exception) {

        log.error("Unexpected application error", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createError(
                        "An unexpected error occurred. Please try again.",
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
    }
}