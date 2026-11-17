package com.smartsugar.backend.service;

import com.smartsugar.backend.entity.ImportBatch;
import com.smartsugar.backend.entity.ImportStatus;
import com.smartsugar.backend.repository.ImportBatchRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ImportBatchService {

    private final ImportBatchRepository repository;

    public ImportBatchService(
            ImportBatchRepository repository) {

        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<ImportBatch> findByFileHash(String fileHash) {

        return repository.findByFileHash(fileHash);
    }

    /*
     * Creates an import record immediately after
     * file validation and duplicate checking.
     */
    @Transactional
    public ImportBatch createImport(
            String fileName,
            String fileHash) {

        ImportBatch batch = new ImportBatch();

        batch.setFileName(fileName);
        batch.setFileHash(fileHash);
        batch.setUploadedAt(LocalDateTime.now());
        batch.setStatus(ImportStatus.RECEIVED);

        batch.setTotalRows(0);
        batch.setValidRows(0);
        batch.setInvalidRows(0);

        return repository.saveAndFlush(batch);
    }
    /*
 * Temporary backward compatibility for the existing controller.
 * This will be removed after the lifecycle controller is integrated.
 */
@Transactional
@Deprecated
public ImportBatch saveImport(
        String fileName,
        String fileHash,
        int totalRows) {

    ImportBatch batch = new ImportBatch();

    batch.setFileName(fileName);
    batch.setFileHash(fileHash);
    batch.setUploadedAt(LocalDateTime.now());
    batch.setTotalRows(totalRows);
    batch.setStatus("SUCCESS");

    return repository.saveAndFlush(batch);
}

    /*
     * Marks the import as PROCESSING.
     */
    @Transactional
    public ImportBatch markProcessing(Long importId) {

        ImportBatch batch = getRequiredImport(importId);

        batch.setStatus(ImportStatus.PROCESSING);
        batch.setProcessingStartedAt(LocalDateTime.now());

        batch.setErrorMessage(null);

        return repository.saveAndFlush(batch);
    }

    /*
     * Marks the import as VALIDATED after Excel parsing
     * and row validation are finished.
     */
    @Transactional
    public ImportBatch markValidated(
            Long importId,
            int totalRows,
            int validRows,
            int invalidRows) {

        if (totalRows < 0 || validRows < 0 || invalidRows < 0) {
            throw new IllegalArgumentException(
                    "Row counts cannot be negative."
            );
        }

        if (validRows + invalidRows != totalRows) {
            throw new IllegalArgumentException(
                    "Valid rows and invalid rows must equal total rows."
            );
        }

        ImportBatch batch = getRequiredImport(importId);

        batch.setTotalRows(totalRows);
        batch.setValidRows(validRows);
        batch.setInvalidRows(invalidRows);

        batch.setValidatedAt(LocalDateTime.now());
        batch.setStatus(ImportStatus.VALIDATED);
        batch.setErrorMessage(null);

        return repository.saveAndFlush(batch);
    }

    /*
     * This method will be used later when actual business
     * records are successfully saved in the database.
     */
    @Transactional
    public ImportBatch markCompleted(Long importId) {

        ImportBatch batch = getRequiredImport(importId);

        if (!ImportStatus.VALIDATED.equals(batch.getStatus())) {
            throw new IllegalStateException(
                    "Only validated imports can be completed."
            );
        }

        batch.setStatus(ImportStatus.COMPLETED);
        batch.setCompletedAt(LocalDateTime.now());

        return repository.saveAndFlush(batch);
    }

    /*
     * Saves failure information without deleting
     * the import history record.
     */
    @Transactional
    public ImportBatch markFailed(
            Long importId,
            String errorMessage) {

        ImportBatch batch = getRequiredImport(importId);

        batch.setStatus(ImportStatus.FAILED);
        batch.setFailedAt(LocalDateTime.now());

        batch.setErrorMessage(
                sanitizeErrorMessage(errorMessage)
        );

        return repository.saveAndFlush(batch);
    }

    @Transactional(readOnly = true)
    public List<ImportBatch> getAllImports() {

        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public long getTotalImports() {

        return repository.count();
    }

    @Transactional(readOnly = true)
    public ImportBatch getImportById(Long id) {

        return repository.findById(id).orElse(null);
    }

    private ImportBatch getRequiredImport(Long importId) {

        return repository.findById(importId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Import record not found: " + importId
                ));
    }

    private String sanitizeErrorMessage(String message) {

        if (message == null || message.isBlank()) {
            return "An unexpected error occurred during import.";
        }

        String sanitized = message.trim();

        if (sanitized.length() > 2000) {
            sanitized = sanitized.substring(0, 2000);
        }

        return sanitized;
    }
}