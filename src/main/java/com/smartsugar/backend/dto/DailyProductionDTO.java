
package com.smartsugar.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyProductionDTO(
        LocalDate reportDate,
        long totalSamples,
        BigDecimal averageBrix,
        BigDecimal averagePol,
        BigDecimal averagePurity
) {
}
