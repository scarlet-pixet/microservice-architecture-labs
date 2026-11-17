package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationDTO;
import org.example.dto.OperationUpdateDTO;
import org.example.service.FinancialOperationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operations")
@Tag(name = "Финансовые операции", description = "Доходы и расходы пользователей")
public class FinancialOperationController {

    private final FinancialOperationService service;

    @GetMapping
    @Operation(summary = "Получить финансовые операции")
    public List<OperationDTO> index(
            @Parameter(description = "Необязательный фильтр по пользователю", example = "1")
            @RequestParam(required = false) Long userId) {
        return service.findAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать доход или расход")
    public OperationDTO create(@Valid @RequestBody OperationCreateDTO data) {
        return service.create(data);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить операцию по идентификатору")
    public OperationDTO show(@Parameter(description = "Идентификатор операции", example = "1") @PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить финансовую операцию")
    public OperationDTO update(
            @Parameter(description = "Идентификатор операции", example = "1") @PathVariable Long id,
            @Valid @RequestBody OperationUpdateDTO data
    ) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить финансовую операцию")
    public void delete(@Parameter(description = "Идентификатор операции", example = "1") @PathVariable Long id) {
        service.delete(id);
    }
}
