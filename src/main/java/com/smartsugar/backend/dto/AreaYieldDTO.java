
package com.smartsugar.backend.dto;

import java.math.BigDecimal;

public record AreaYieldDTO(
        String areaUnit,
        BigDecimal totalArea,
        BigDecimal totalProduction,
        BigDecimal yieldPerArea
) {
}
