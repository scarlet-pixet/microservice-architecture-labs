package org.example.analytics;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.analytics.service.ExpenseAnalyticsService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FeignClientIntegrationTest {

    private static final MockWebServer SERVER = startServer();

    @Autowired
    private ExpenseAnalyticsService analyticsService;

    @DynamicPropertySource
    static void configureExpenseService(DynamicPropertyRegistry registry) {
        registry.add("expense-service.url", () -> SERVER.url("/").toString());
    }

    @AfterAll
    static void stopServer() throws IOException {
        SERVER.shutdown();
    }

    @Test
    void feignClientRequestsOperationsAndBuildsSummary() throws InterruptedException {
        SERVER.enqueue(new MockResponse()
                .setHeader("Content-Type", "application/json")
                .setBody("""
                        [
                          {"id":1,"userId":13,"type":"INCOME","amount":1800.00,"category":"Зарплата","description":"Доход","operationDate":"2026-10-01"},
                          {"id":2,"userId":13,"type":"EXPENSE","amount":275.40,"category":"Продукты","description":"Покупка","operationDate":"2026-10-02"}
                        ]
                        """));

        var summary = analyticsService.getSummary(13L);

        assertThat(summary.balance()).isEqualByComparingTo("1524.60");
        assertThat(summary.operationCount()).isEqualTo(2);
        var request = SERVER.takeRequest(2, TimeUnit.SECONDS);
        assertThat(request).isNotNull();
        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getPath()).isEqualTo("/api/operations?userId=13");
    }

    private static MockWebServer startServer() {
        MockWebServer server = new MockWebServer();
        try {
            server.start();
            return server;
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot start MockWebServer", exception);
        }
    }
}
