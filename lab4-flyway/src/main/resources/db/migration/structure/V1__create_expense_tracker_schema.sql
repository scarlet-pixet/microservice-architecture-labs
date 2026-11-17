CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE financial_operations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    category VARCHAR(255) NOT NULL,
    description TEXT,
    operation_date DATE NOT NULL,
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_financial_operations_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT chk_financial_operations_amount CHECK (amount >= 0.01),
    CONSTRAINT chk_financial_operations_type CHECK (type IN ('INCOME', 'EXPENSE'))
);
