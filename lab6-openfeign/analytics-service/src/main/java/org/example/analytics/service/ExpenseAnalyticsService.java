package org.example.analytics.service;

import org.example.analytics.client.ExpenseServiceClient;
import org.example.analytics.dto.ExpenseSummary;
import org.example.analytics.dto.OperationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseAnalyticsService {

    private final ExpenseServiceClient expenseServiceClient;

    public ExpenseAnalyticsService(ExpenseServiceClient expenseServiceClient) {
        this.expenseServiceClient = expenseServiceClient;
    }

    public ExpenseSummary getSummary(Long userId) {
        List<OperationResponse> operations = expenseServiceClient.findOperations(userId);

        BigDecimal income = sumByType(operations, "INCOME");
        BigDecimal expense = sumByType(operations, "EXPENSE");
        Map<String, BigDecimal> byCategory = new LinkedHashMap<>();

        operations.stream()
                .filter(operation -> "EXPENSE".equals(operation.type()))
                .forEach(operation -> byCategory.merge(
                        operation.category(), operation.amount(), BigDecimal::add));

        return new ExpenseSummary(
                userId,
                income,
                expense,
                income.subtract(expense),
                operations.size(),
                byCategory
        );
    }

    private BigDecimal sumByType(List<OperationResponse> operations, String type) {
        return operations.stream()
                .filter(operation -> type.equals(operation.type()))
                .map(OperationResponse::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
