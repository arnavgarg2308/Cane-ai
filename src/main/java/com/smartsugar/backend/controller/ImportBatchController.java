
package com.smartsugar.backend.controller;

import com.smartsugar.backend.entity.ImportBatch;
import com.smartsugar.backend.service.ImportBatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/imports")
public class ImportBatchController {

    private final ImportBatchService importBatchService;

    public ImportBatchController(
            ImportBatchService importBatchService) {
        this.importBatchService = importBatchService;
    }

    @GetMapping
    public List<ImportBatch> getAllImports() {
        return importBatchService.getAllImports();
    }

    @GetMapping("/count")
    public long getTotalImports() {
        return importBatchService.getTotalImports();
    }

  @GetMapping("/{id}")
public ResponseEntity<ImportBatch> getImportById(
        @PathVariable Long id) {

    ImportBatch batch = importBatchService.getImportById(id);

    if (batch == null) {
        return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok(batch);
}
}
