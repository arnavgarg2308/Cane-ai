
package com.smartsugar.backend.dto;

import java.math.BigDecimal;

public record VarietyAnalyticsDTO(
        String variety,
        long totalSamples,
        BigDecimal averageBrix,
        BigDecimal averagePol,
        BigDecimal averagePurity
) {
}
