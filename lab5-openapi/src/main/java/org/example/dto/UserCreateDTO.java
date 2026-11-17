package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateDTO {

    @Schema(description = "Имя пользователя", example = "Иван Петров")
    @NotBlank
    private String name;

    @Schema(description = "Уникальный адрес электронной почты", example = "ivan@example.com")
    @Email
    @NotBlank
    private String email;
}
