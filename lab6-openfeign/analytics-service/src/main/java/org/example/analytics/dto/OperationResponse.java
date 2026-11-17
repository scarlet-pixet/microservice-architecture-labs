package org.example.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperationResponse(
        Long id,
        Long userId,
        String type,
        BigDecimal amount,
        String category,
        String description,
        LocalDate operationDate
) {
}
