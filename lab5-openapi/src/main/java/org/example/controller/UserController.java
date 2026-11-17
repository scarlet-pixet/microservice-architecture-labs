package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserCreateDTO;
import org.example.dto.UserDTO;
import org.example.dto.UserUpdateDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "Пользователи", description = "CRUD-операции с пользователями трекера расходов")
public class UserController {

    private final UserRepository repository;
    private final UserMapper mapper;

    @GetMapping
    @Operation(summary = "Получить всех пользователей")
    public List<UserDTO> index() {
        return repository.findAll().stream().map(mapper::map).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя")
    public UserDTO create(@Valid @RequestBody UserCreateDTO data) {
        return mapper.map(repository.save(mapper.map(data)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по идентификатору")
    public UserDTO show(@Parameter(description = "Идентификатор пользователя", example = "1") @PathVariable Long id) {
        return mapper.map(findUser(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить пользователя")
    public UserDTO update(@Parameter(description = "Идентификатор пользователя", example = "1") @PathVariable Long id,
                          @Valid @RequestBody UserUpdateDTO data) {
        var user = findUser(id);
        mapper.update(data, user);
        return mapper.map(repository.save(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя")
    public void delete(@Parameter(description = "Идентификатор пользователя", example = "1") @PathVariable Long id) {
        repository.delete(findUser(id));
    }

    private org.example.model.User findUser(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}
