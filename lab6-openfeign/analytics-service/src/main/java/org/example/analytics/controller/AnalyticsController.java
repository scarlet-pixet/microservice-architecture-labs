package org.example.analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.analytics.dto.ExpenseSummary;
import org.example.analytics.service.ExpenseAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Аналитика расходов", description = "Агрегированные данные, полученные через OpenFeign")
public class AnalyticsController {

    private final ExpenseAnalyticsService analyticsService;

    public AnalyticsController(ExpenseAnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/users/{userId}/summary")
    @Operation(summary = "Получить сводку доходов и расходов пользователя")
    public ExpenseSummary getSummary(@PathVariable Long userId) {
        return analyticsService.getSummary(userId);
    }
}
