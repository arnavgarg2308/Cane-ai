
package com.smartsugar.backend.dto;

import java.time.LocalDateTime;

public record AnalyticsApiResponse(
        LocalDateTime timestamp,
        int status,
        String message,
        String endpoint,
        String pendingRequirement
) {
}
