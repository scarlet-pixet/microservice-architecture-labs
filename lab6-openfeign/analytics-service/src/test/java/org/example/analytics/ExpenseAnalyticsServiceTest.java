package org.example.analytics;

import org.example.analytics.client.ExpenseServiceClient;
import org.example.analytics.dto.OperationResponse;
import org.example.analytics.service.ExpenseAnalyticsService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExpenseAnalyticsServiceTest {

    @Test
    void calculatesIncomeExpenseBalanceAndCategories() {
        ExpenseServiceClient client = mock(ExpenseServiceClient.class);
        when(client.findOperations(13L)).thenReturn(List.of(
                operation(1L, "INCOME", "2500.00", "Зарплата"),
                operation(2L, "EXPENSE", "320.50", "Продукты"),
                operation(3L, "EXPENSE", "79.50", "Продукты")
        ));

        var summary = new ExpenseAnalyticsService(client).getSummary(13L);

        assertThat(summary.totalIncome()).isEqualByComparingTo("2500.00");
        assertThat(summary.totalExpense()).isEqualByComparingTo("400.00");
        assertThat(summary.balance()).isEqualByComparingTo("2100.00");
        assertThat(summary.operationCount()).isEqualTo(3);
        assertThat(summary.expensesByCategory().get("Продукты")).isEqualByComparingTo("400.00");
    }

    private OperationResponse operation(Long id, String type, String amount, String category) {
        return new OperationResponse(
                id, 13L, type, new BigDecimal(amount), category,
                "Тестовая операция", LocalDate.of(2026, 10, 19));
    }
}
