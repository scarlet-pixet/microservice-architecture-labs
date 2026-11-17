package org.example.service;

import org.example.dto.OperationCreateDTO;
import org.example.dto.OperationDTO;
import org.example.dto.OperationUpdateDTO;

import java.util.List;

public interface FinancialOperationService {
    OperationDTO create(OperationCreateDTO data);
    List<OperationDTO> findAll(Long userId);
    OperationDTO findById(Long id);
    OperationDTO update(Long id, OperationUpdateDTO data);
    void delete(Long id);
}
