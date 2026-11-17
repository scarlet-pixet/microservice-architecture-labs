package org.example.analytics;

import org.example.analytics.controller.AnalyticsController;
import org.example.analytics.dto.ExpenseSummary;
import org.example.analytics.service.ExpenseAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ExpenseAnalyticsService analyticsService;

    @Test
    void returnsSummaryForRequestedUser() throws Exception {
        when(analyticsService.getSummary(13L)).thenReturn(new ExpenseSummary(
                13L,
                new BigDecimal("1800.00"),
                new BigDecimal("275.40"),
                new BigDecimal("1524.60"),
                2,
                Map.of("Продукты", new BigDecimal("275.40"))
        ));

        mockMvc.perform(get("/api/analytics/users/13/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(13))
                .andExpect(jsonPath("$.balance").value(1524.60))
                .andExpect(jsonPath("$.expensesByCategory.Продукты").value(275.40));
    }
}
