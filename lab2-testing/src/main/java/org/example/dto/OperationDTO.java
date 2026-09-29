package org.example.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.model.OperationType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OperationDTO {
    private Long id;
    private Long userId;
    private OperationType type;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDate operationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
