
package com.smartsugar.backend.dto;

import java.math.BigDecimal;

public record VillageAnalyticsDTO(
        String villageCode,
        String villageName,
        long totalSamples,
        BigDecimal averageBrix,
        BigDecimal averagePol,
        BigDecimal averagePurity
) {
}
