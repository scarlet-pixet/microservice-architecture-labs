package org.example.dto;

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

    @NotNull
    private Long userId;

    @NotNull
    private OperationType type;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotBlank
    private String category;

    private String description;

    @NotNull
    private LocalDate operationDate;
}
