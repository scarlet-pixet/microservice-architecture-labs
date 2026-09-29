package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserUpdateDTO {

    @NotNull
    private JsonNullable<@NotBlank String> name = JsonNullable.undefined();

    @NotNull
    private JsonNullable<@Email @NotBlank String> email = JsonNullable.undefined();
}
