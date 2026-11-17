package com.smartsugar.backend.controller;

import com.smartsugar.backend.dto.ValidationReport;
import com.smartsugar.backend.entity.ImportBatch;
import com.smartsugar.backend.service.DuplicateImportException;
import com.smartsugar.backend.service.ExcelFileValidationService;
import com.smartsugar.backend.service.ExcelService;
import com.smartsugar.backend.service.ImportBatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/excel")
public class ExcelUploadController {

    private static final Logger log =
            LoggerFactory.getLogger(ExcelUploadController.class);

    private final ExcelService excelService;
    private final ImportBatchService importBatchService;
    private final ExcelFileValidationService fileValidationService;

    public ExcelUploadController(
            ExcelService excelService,
            ImportBatchService importBatchService,
            ExcelFileValidationService fileValidationService) {

        this.excelService = excelService;
        this.importBatchService = importBatchService;
        this.fileValidationService = fileValidationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadExcel(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        // 1. Validate uploaded file.
        String fileName =
                fileValidationService.validateAndGetFileName(file);

        // 2. Calculate SHA-256 fingerprint.
        String fileHash =
                fileValidationService.calculateSha256(file);

        // 3. Reject duplicate files.
        var existingImport =
                importBatchService.findByFileHash(fileHash);

        if (existingImport.isPresent()) {
            throw new DuplicateImportException(
                    "This exact Excel file has already been imported.",
                    existingImport.get().getId()
            );
        }

        // 4. Create persistent import history first.
        ImportBatch batch =
                importBatchService.createImport(fileName, fileHash);

        Long importId = batch.getId();

        try {

            // 5. Mark processing.
            importBatchService.markProcessing(importId);

            // 6. Read and validate workbook.
            Map<String, Object> result =
                    excelService.readExcel(file);

            int totalRows =
                    (Integer) result.get("totalDataRows");

            int validRows = 0;
            int invalidRows = 0;

            // 7. Aggregate validation results from all sheets.
            Object sheetsObject = result.get("sheets");

            if (sheetsObject instanceof List<?> sheets) {

                for (Object sheetObject : sheets) {

                    if (!(sheetObject instanceof Map<?, ?> sheet)) {
                        continue;
                    }

                    Object reportObject =
                            sheet.get("validationReport");

                    if (reportObject instanceof ValidationReport report) {

                        validRows += report.getValidRows();
                        invalidRows += report.getInvalidRows();
                    }
                }
            }

            // 8. Persist validation summary and status.
            ImportBatch validatedBatch =
                    importBatchService.markValidated(
                            importId,
                            totalRows,
                            validRows,
                            invalidRows
                    );

            // 9. Add import metadata to response.
            result.put("importBatchId", validatedBatch.getId());
            result.put("importStatus", validatedBatch.getStatus());
            result.put("validRows", validatedBatch.getValidRows());
            result.put("invalidRows", validatedBatch.getInvalidRows());

            return ResponseEntity.ok(result);

        } catch (IOException | RuntimeException exception) {

            // 10. Preserve failed import history.
            try {
                importBatchService.markFailed(
                        importId,
                        exception.getMessage()
                );
            } catch (Exception statusException) {
                log.error(
                        "Could not save FAILED status for import ID {}",
                        importId,
                        statusException
                );
            }

            log.error(
                    "Excel import failed. Import ID: {}",
                    importId,
                    exception
            );

            throw exception;
        }
    }
}