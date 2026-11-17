
package com.smartsugar.backend.controller;

import com.smartsugar.backend.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public HealthResponse checkHealth() {

        return new HealthResponse(
            "SmartSugar AI",
            "RUNNING",
            "Backend is healthy"
        );
    }
}
