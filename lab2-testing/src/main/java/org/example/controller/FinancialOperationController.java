package org.example.controller;

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
public class FinancialOperationController {

    private final FinancialOperationService service;

    @GetMapping
    public List<OperationDTO> index(@RequestParam(required = false) Long userId) {
        return service.findAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OperationDTO create(@Valid @RequestBody OperationCreateDTO data) {
        return service.create(data);
    }

    @GetMapping("/{id}")
    public OperationDTO show(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public OperationDTO update(
            @PathVariable Long id,
            @Valid @RequestBody OperationUpdateDTO data
    ) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
