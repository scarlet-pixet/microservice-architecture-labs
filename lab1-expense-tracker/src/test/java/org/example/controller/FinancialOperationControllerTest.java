package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationUpdateDTO;
import org.example.model.FinancialOperation;
import org.example.model.OperationType;
import org.example.model.User;
import org.example.repository.FinancialOperationRepository;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FinancialOperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FinancialOperationRepository operationRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private FinancialOperation testOperation;

    @BeforeEach
    void setUp() {
        operationRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setName("Иван Петров");
        testUser.setEmail("ivan@example.com");
        testUser = userRepository.save(testUser);

        testOperation = new FinancialOperation();
        testOperation.setUser(testUser);
        testOperation.setType(OperationType.EXPENSE);
        testOperation.setAmount(new BigDecimal("125.50"));
        testOperation.setCategory("Продукты");
        testOperation.setDescription("Покупка продуктов");
        testOperation.setOperationDate(LocalDate.of(2026, 9, 27));
    }

    @Test
    void indexReturnsOperations() throws Exception {
        operationRepository.save(testOperation);

        mockMvc.perform(get("/api/operations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("Продукты"))
                .andExpect(jsonPath("$[0].userId").value(testUser.getId()));
    }

    @Test
    void createPersistsOperation() throws Exception {
        var dto = createDto();

        mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").value("EXPENSE"))
                .andExpect(jsonPath("$.amount").value(125.50));

        var saved = operationRepository.findAll().getFirst();
        assertThat(saved.getCategory()).isEqualTo("Продукты");
        assertThat(saved.getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    void showReturnsOneOperation() throws Exception {
        var saved = operationRepository.save(testOperation);

        mockMvc.perform(get("/api/operations/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Покупка продуктов"));
    }

    @Test
    void updateChangesOnlyProvidedFields() throws Exception {
        var saved = operationRepository.save(testOperation);
        var dto = new OperationUpdateDTO();
        dto.setAmount(JsonNullable.of(new BigDecimal("150.00")));
        dto.setDescription(JsonNullable.of("Продукты и бытовая химия"));

        mockMvc.perform(put("/api/operations/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(150.00))
                .andExpect(jsonPath("$.category").value("Продукты"));

        var updated = operationRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getDescription()).isEqualTo("Продукты и бытовая химия");
        assertThat(updated.getCategory()).isEqualTo("Продукты");
    }

    @Test
    void deleteRemovesOperation() throws Exception {
        var saved = operationRepository.save(testOperation);

        mockMvc.perform(delete("/api/operations/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        assertThat(operationRepository.existsById(saved.getId())).isFalse();
    }

    @Test
    void createRejectsInvalidAmount() throws Exception {
        var dto = createDto();
        dto.setAmount(BigDecimal.ZERO);

        mockMvc.perform(post("/api/operations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void showReturnsNotFoundForUnknownId() throws Exception {
        mockMvc.perform(get("/api/operations/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    private OperationCreateDTO createDto() {
        var dto = new OperationCreateDTO();
        dto.setUserId(testUser.getId());
        dto.setType(OperationType.EXPENSE);
        dto.setAmount(new BigDecimal("125.50"));
        dto.setCategory("Продукты");
        dto.setDescription("Покупка продуктов");
        dto.setOperationDate(LocalDate.of(2026, 9, 27));
        return dto;
    }
}
