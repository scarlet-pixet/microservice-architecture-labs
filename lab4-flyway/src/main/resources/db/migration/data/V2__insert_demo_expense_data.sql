INSERT INTO users (name, email)
VALUES ('Иван Петров', 'ivan@example.com'),
       ('Анна Смирнова', 'anna@example.com');

INSERT INTO financial_operations
    (user_id, type, amount, category, description, operation_date)
VALUES
    (1, 'INCOME', 3500.00, 'Зарплата', 'Ежемесячный доход', '2026-09-01'),
    (1, 'EXPENSE', 125.50, 'Продукты', 'Покупка продуктов', '2026-09-02'),
    (2, 'EXPENSE', 45.00, 'Транспорт', 'Проездной билет', '2026-09-03');
