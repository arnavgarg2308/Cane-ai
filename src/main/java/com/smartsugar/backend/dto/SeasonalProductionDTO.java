
package com.smartsugar.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SeasonalProductionDTO(
        String seasonName,
        LocalDate startDate,
        LocalDate endDate,
        long totalSamples,
        BigDecimal averageBrix,
        BigDecimal averagePol,
        BigDecimal averagePurity
) {
}
