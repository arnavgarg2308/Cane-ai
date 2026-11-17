
package com.smartsugar.backend.controller;

import com.smartsugar.backend.entity.CaneSample;
import com.smartsugar.backend.service.CaneSampleService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/samples")
public class CaneSampleController {

    private final CaneSampleService caneSampleService;

    public CaneSampleController(CaneSampleService caneSampleService) {
        this.caneSampleService = caneSampleService;
    }

    // Get all samples
    @GetMapping
    public List<CaneSample> getAllSamples() {
        return caneSampleService.getAllSamples();
    }

    // Get total sample count
    @GetMapping("/count")
    public long getTotalSamples() {
        return caneSampleService.getTotalSamples();
    }

    // Get sample by ID
    @GetMapping("/{id}")
    public ResponseEntity<CaneSample> getSampleById(
            @PathVariable Long id) {

        return caneSampleService.getSampleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
