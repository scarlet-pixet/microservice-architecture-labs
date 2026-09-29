package org.example.repository;

import org.example.model.FinancialOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialOperationRepository extends JpaRepository<FinancialOperation, Long> {
    List<FinancialOperation> findAllByUserIdOrderByOperationDateDesc(Long userId);
}
