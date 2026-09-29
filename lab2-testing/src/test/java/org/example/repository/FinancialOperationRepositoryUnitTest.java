package org.example.repository;

import org.example.config.JpaAuditingConfig;
import org.example.model.FinancialOperation;
import org.example.model.OperationType;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("Модульные тесты репозитория финансовых операций")
class FinancialOperationRepositoryUnitTest {

    @Autowired
    private FinancialOperationRepository operationRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setName("Анна Смирнова");
        user.setEmail("anna@example.com");
        user = userRepository.save(user);
    }

    @Test
    @DisplayName("Сохранение операции назначает идентификатор")
    void saveOperation() {
        var saved = operationRepository.save(operation("120.00", "Транспорт"));

        assertThat(saved.getId()).isPositive();
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Операция находится по идентификатору")
    void getOperationById() {
        var saved = operationRepository.save(operation("850.00", "Продукты"));

        var found = operationRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.orElseThrow().getCategory()).isEqualTo("Продукты");
    }

    @Test
    @DisplayName("Получение списка операций пользователя")
    void getOperationsByUser() {
        operationRepository.save(operation("100.00", "Транспорт"));
        operationRepository.save(operation("500.00", "Продукты"));

        var operations = operationRepository.findAllByUserIdOrderByOperationDateDesc(user.getId());

        assertThat(operations).hasSize(2);
        assertThat(operations).allMatch(item -> item.getUser().getId().equals(user.getId()));
    }

    @Test
    @DisplayName("Обновление сохраняет новую сумму")
    void updateOperation() {
        var saved = operationRepository.save(operation("100.00", "Связь"));
        saved.setAmount(new BigDecimal("150.00"));

        var updated = operationRepository.save(saved);

        assertThat(updated.getAmount()).isEqualByComparingTo("150.00");
    }

    @Test
    @DisplayName("Удаление убирает операцию из базы")
    void deleteOperation() {
        var saved = operationRepository.save(operation("300.00", "Развлечения"));

        operationRepository.deleteById(saved.getId());

        assertThat(operationRepository.findById(saved.getId())).isEmpty();
    }

    private FinancialOperation operation(String amount, String category) {
        var operation = new FinancialOperation();
        operation.setUser(user);
        operation.setType(OperationType.EXPENSE);
        operation.setAmount(new BigDecimal(amount));
        operation.setCategory(category);
        operation.setDescription("Тестовая операция");
        operation.setOperationDate(LocalDate.of(2026, 9, 27));
        return operation;
    }
}
