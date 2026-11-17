package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.model.OperationType;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class OperationUpdateDTO {

    @NotNull
    private JsonNullable<@NotNull Long> userId = JsonNullable.undefined();

    @NotNull
    private JsonNullable<@NotNull OperationType> type = JsonNullable.undefined();

    @NotNull
    private JsonNullable<@NotNull @DecimalMin("0.01") BigDecimal> amount = JsonNullable.undefined();

    @NotNull
    private JsonNullable<@NotBlank String> category = JsonNullable.undefined();

    @NotNull
    private JsonNullable<String> description = JsonNullable.undefined();

    @NotNull
    private JsonNullable<@NotNull LocalDate> operationDate = JsonNullable.undefined();
}
