package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationDTO;
import org.example.dto.OperationUpdateDTO;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.OperationMapper;
import org.example.model.FinancialOperation;
import org.example.repository.FinancialOperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinancialOperationServiceImpl implements FinancialOperationService {

    private final FinancialOperationRepository repository;
    private final OperationMapper mapper;

    @Override
    @Transactional
    public OperationDTO create(OperationCreateDTO data) {
        var operation = mapper.map(data);
        return mapper.map(repository.save(operation));
    }

    @Override
    public List<OperationDTO> findAll(Long userId) {
        var operations = userId == null
                ? repository.findAll()
                : repository.findAllByUserIdOrderByOperationDateDesc(userId);
        return operations.stream().map(mapper::map).toList();
    }

    @Override
    public OperationDTO findById(Long id) {
        return mapper.map(findOperation(id));
    }

    @Override
    @Transactional
    public OperationDTO update(Long id, OperationUpdateDTO data) {
        var operation = findOperation(id);
        mapper.update(data, operation);
        return mapper.map(repository.save(operation));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.delete(findOperation(id));
    }

    private FinancialOperation findOperation(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Operation not found: " + id));
    }
}
