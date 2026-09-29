package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.config.JacksonConfig;
import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationDTO;
import org.example.model.OperationType;
import org.example.service.FinancialOperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinancialOperationController.class)
@Import(JacksonConfig.class)
@DisplayName("Модульные тесты контроллера финансовых операций")
class FinancialOperationControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FinancialOperationService service;

    private OperationDTO operation;

    @BeforeEach
    void setUp() {
        operation = new OperationDTO();
        operation.setId(1L);
        operation.setUserId(10L);
        operation.setType(OperationType.EXPENSE);
        operation.setAmount(new BigDecimal("125.50"));
        operation.setCategory("Продукты");
        operation.setDescription("Покупка продуктов");
        operation.setOperationDate(LocalDate.of(2026, 9, 27));
    }

    @Test
    @DisplayName("POST создаёт операцию")
    void createOperation() throws Exception {
        var request = createRequest();
        given(service.create(any(OperationCreateDTO.class))).willReturn(operation);

        mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.category").value("Продукты"));
    }

    @Test
    @DisplayName("GET возвращает список операций")
    void getAllOperations() throws Exception {
        given(service.findAll(null)).willReturn(List.of(operation));

        mockMvc.perform(get("/api/operations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].amount").value(125.50));
    }

    @Test
    @DisplayName("GET по идентификатору возвращает одну операцию")
    void getOperationById() throws Exception {
        given(service.findById(1L)).willReturn(operation);

        mockMvc.perform(get("/api/operations/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Покупка продуктов"));
    }

    @Test
    @DisplayName("PUT обновляет переданные поля")
    void updateOperation() throws Exception {
        operation.setAmount(new BigDecimal("300.00"));
        given(service.update(any(Long.class), any())).willReturn(operation);

        mockMvc.perform(put("/api/operations/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":300.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(300.00));
    }

    @Test
    @DisplayName("DELETE удаляет операцию")
    void deleteOperation() throws Exception {
        mockMvc.perform(delete("/api/operations/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    private OperationCreateDTO createRequest() {
        var request = new OperationCreateDTO();
        request.setUserId(10L);
        request.setType(OperationType.EXPENSE);
        request.setAmount(new BigDecimal("125.50"));
        request.setCategory("Продукты");
        request.setDescription("Покупка продуктов");
        request.setOperationDate(LocalDate.of(2026, 9, 27));
        return request;
    }
}
