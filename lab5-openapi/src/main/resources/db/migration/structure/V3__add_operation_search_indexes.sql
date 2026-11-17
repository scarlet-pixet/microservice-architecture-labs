CREATE INDEX idx_financial_operations_user_date
    ON financial_operations (user_id, operation_date);

CREATE INDEX idx_financial_operations_type
    ON financial_operations (type);
