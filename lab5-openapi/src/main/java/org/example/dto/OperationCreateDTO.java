package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OperationCreateDTO {

    @Schema(description = "Идентификатор пользователя", example = "1")
    @NotNull
    private Long userId;

    @Schema(description = "Тип операции", example = "EXPENSE")
    @NotNull
    private OperationType type;

    @Schema(description = "Сумма операции", example = "125.50", minimum = "0.01")
    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @Schema(description = "Категория", example = "Продукты")
    @NotBlank
    private String category;

    @Schema(description = "Комментарий", example = "Покупка продуктов")
    private String description;

    @Schema(description = "Дата операции", example = "2026-09-02")
    @NotNull
    private LocalDate operationDate;
}
