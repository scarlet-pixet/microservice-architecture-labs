package org.example.analytics.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExpenseSummary(
        Long userId,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        int operationCount,
        Map<String, BigDecimal> expensesByCategory
) {
}
