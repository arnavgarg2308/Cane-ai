
package com.smartsugar.backend.dto;

import java.math.BigDecimal;

public record CentreAnalyticsDTO(
        String centreName,
        long totalSamples,
        BigDecimal averageBrix,
        BigDecimal averagePol,
        BigDecimal averagePurity
) {
}
