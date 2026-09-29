package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationDTO;
import org.example.dto.OperationUpdateDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.OperationMapper;
import org.example.model.FinancialOperation;
import org.example.repository.FinancialOperationRepository;
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

    private final FinancialOperationRepository repository;
    private final OperationMapper mapper;

    @GetMapping
    public List<OperationDTO> index(@RequestParam(required = false) Long userId) {
        var operations = userId == null
                ? repository.findAll()
                : repository.findAllByUserIdOrderByOperationDateDesc(userId);
        return operations.stream().map(mapper::map).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OperationDTO create(@Valid @RequestBody OperationCreateDTO data) {
        return mapper.map(repository.save(mapper.map(data)));
    }

    @GetMapping("/{id}")
    public OperationDTO show(@PathVariable Long id) {
        return mapper.map(findOperation(id));
    }

    @PutMapping("/{id}")
    public OperationDTO update(
            @PathVariable Long id,
            @Valid @RequestBody OperationUpdateDTO data
    ) {
        var operation = findOperation(id);
        mapper.update(data, operation);
        return mapper.map(repository.save(operation));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repository.delete(findOperation(id));
    }

    private FinancialOperation findOperation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operation not found: " + id));
    }
}
