-- Criação da tabela 'Conta'
CREATE TABLE conta (
                       id SERIAL PRIMARY KEY,
                       descricao VARCHAR(255) NOT NULL,
                       valor NUMERIC(10, 2) NOT NULL,
                       data_vencimento DATE NOT NULL,
                       data_pagamento DATE
);

-- Inserção de dados mockados na tabela 'Conta'
INSERT INTO conta (descricao, valor, data_vencimento, data_pagamento)
VALUES
    ('Conta de Luz', 150.50, '2024-01-15', '2024-01-10'),
    ('Conta de Água', 75.25, '2024-01-20', '2024-01-18'),
    ('Conta de Internet', 200.00, '2024-02-01', NULL),
    ('Supermercado', 300.00, '2024-02-05', '2024-02-04'),
    ('Seguro do Carro', 450.00, '2024-03-01', NULL);

-- Inserção de mais dados na tabela 'Conta'
INSERT INTO conta (descricao, valor, data_vencimento, data_pagamento)
VALUES
    ('Pagamento do Cartão de Crédito', 1000.00, '2024-01-31', '2024-01-30'),
    ('Combustível', 120.00, '2024-02-15', '2024-02-14'),
    ('Academia', 80.00, '2024-03-01', NULL),
    ('Assinatura de Streaming', 50.00, '2024-03-10', NULL),
    ('Mensalidade Escolar', 1500.00, '2024-03-20', '2024-03-19');
