package org.example.service;

import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationDTO;
import org.example.dto.OperationUpdateDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.OperationMapper;
import org.example.model.FinancialOperation;
import org.example.model.OperationType;
import org.example.repository.FinancialOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты сервиса финансовых операций")
class FinancialOperationServiceUnitTest {

    @Mock
    private FinancialOperationRepository repository;

    @Mock
    private OperationMapper mapper;

    @InjectMocks
    private FinancialOperationServiceImpl service;

    private FinancialOperation operation;
    private OperationDTO response;

    @BeforeEach
    void setUp() {
        operation = new FinancialOperation();
        operation.setId(1L);
        operation.setType(OperationType.EXPENSE);
        operation.setAmount(new BigDecimal("250.00"));
        operation.setCategory("Продукты");
        operation.setOperationDate(LocalDate.of(2026, 9, 27));

        response = new OperationDTO();
        response.setId(1L);
        response.setType(OperationType.EXPENSE);
        response.setAmount(new BigDecimal("250.00"));
        response.setCategory("Продукты");
    }

    @Test
    @DisplayName("Создание операции использует маппер и репозиторий")
    void createOperation() {
        var request = new OperationCreateDTO();
        given(mapper.map(request)).willReturn(operation);
        given(repository.save(operation)).willReturn(operation);
        given(mapper.map(operation)).willReturn(response);

        var result = service.create(request);

        assertThat(result).isSameAs(response);
        verify(repository).save(operation);
    }

    @Test
    @DisplayName("Получение списка преобразует все сущности в DTO")
    void getAllOperations() {
        given(repository.findAll()).willReturn(List.of(operation));
        given(mapper.map(operation)).willReturn(response);

        var result = service.findAll(null);

        assertThat(result).containsExactly(response);
    }

    @Test
    @DisplayName("Получение существующей операции возвращает DTO")
    void getOperationById() {
        given(repository.findById(1L)).willReturn(Optional.of(operation));
        given(mapper.map(operation)).willReturn(response);

        assertThat(service.findById(1L)).isSameAs(response);
    }

    @Test
    @DisplayName("Для неизвестного идентификатора выбрасывается исключение")
    void getUnknownOperation() {
        given(repository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Обновление изменяет найденную сущность")
    void updateOperation() {
        var request = new OperationUpdateDTO();
        request.setAmount(JsonNullable.of(new BigDecimal("300.00")));
        given(repository.findById(1L)).willReturn(Optional.of(operation));
        given(repository.save(operation)).willReturn(operation);
        given(mapper.map(operation)).willReturn(response);

        var result = service.update(1L, request);

        assertThat(result).isSameAs(response);
        verify(mapper).update(request, operation);
        verify(repository).save(operation);
    }

    @Test
    @DisplayName("Удаление передаёт найденную сущность репозиторию")
    void deleteOperation() {
        given(repository.findById(1L)).willReturn(Optional.of(operation));

        service.delete(1L);

        verify(repository).delete(operation);
    }
}
