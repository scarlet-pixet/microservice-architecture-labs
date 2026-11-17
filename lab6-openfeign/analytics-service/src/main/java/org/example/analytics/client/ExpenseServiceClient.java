package org.example.analytics.client;

import org.example.analytics.config.FeignClientConfig;
import org.example.analytics.dto.OperationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "expense-service",
        url = "${expense-service.url:http://localhost:8080}",
        configuration = FeignClientConfig.class
)
public interface ExpenseServiceClient {

    @GetMapping("/api/operations")
    List<OperationResponse> findOperations(@RequestParam("userId") Long userId);
}
