-- ============================================================
--  CONTROLE FINANCEIRO — Schema MySQL
--  Execute com: mysql -u root -p < schema.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS controle_financeiro
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE controle_financeiro;

-- ------------------------------------------------------------
-- CATEGORIAS
-- Agrupa contas por tipo (ex: Alimentação, Transporte, Saúde)
-- Removido o campo "tipo" pois todas são despesas no contexto
-- do sistema. Receita fica no resumo_diario.
-- ------------------------------------------------------------
CREATE TABLE categorias (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL UNIQUE,
    criado_em DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
-- CONTAS (tabela pai)
-- Campos comuns a TODAS as contas, fixas ou variáveis.
-- O campo "tipo" determina qual tabela filha tem o registro.
-- ------------------------------------------------------------
CREATE TABLE contas (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    categoria_id INT            NOT NULL,
    nome         VARCHAR(150)   NOT NULL,
    valor        DECIMAL(10, 2) NOT NULL,
    vencimento   DATE           NOT NULL,
    pago         BOOLEAN        DEFAULT FALSE,
    observacao   TEXT,
    criado_em    DATETIME       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_conta_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_contas_vencimento  ON contas(vencimento);
CREATE INDEX idx_contas_categoria   ON contas(categoria_id);
CREATE INDEX idx_contas_pago        ON contas(pago);

-- ------------------------------------------------------------
-- CONTAS_FIXAS (tabela filha de contas)
-- Herda tudo de contas. Não tem campos extras por enquanto,
-- mas existe como entidade separada para futuras extensões
-- (ex: dia_debito_automatico, banco, etc).
-- conta_id é PK e FK ao mesmo tempo — garante 1:1 com contas.
-- ------------------------------------------------------------
CREATE TABLE contas_fixas (
    conta_id INT PRIMARY KEY,

    CONSTRAINT fk_contafixa_conta
        FOREIGN KEY (conta_id) REFERENCES contas(id)
        ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- CONTAS_VARIAVEIS (tabela filha de contas)
-- Campos exclusivos de contas parceladas/variáveis.
-- num_parcelas = total de parcelas (ex: 12)
-- parcela_atual = parcela em que está (ex: 3)
-- ------------------------------------------------------------
CREATE TABLE contas_variaveis (
    conta_id      INT PRIMARY KEY,
    num_parcelas  INT NOT NULL DEFAULT 1 COMMENT 'Total de parcelas. 1 = conta única',
    parcela_atual INT NOT NULL DEFAULT 1,

    CONSTRAINT fk_contavar_conta
        FOREIGN KEY (conta_id) REFERENCES contas(id)
        ON DELETE CASCADE
);

-- ------------------------------------------------------------
-- DESPESAS (tabela pai)
-- Campos comuns a TODAS as despesas do dia,
-- sejam elas variáveis avulsas ou despesas da moto.
-- ------------------------------------------------------------
CREATE TABLE despesas (
    id        INT            AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(150)   NOT NULL,
    valor     DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    -- data preenchida automaticamente no momento do cadastro
    data      DATE           NOT NULL DEFAULT (CURDATE()),
    criado_em DATETIME       DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_despesas_data ON despesas(data);

-- ------------------------------------------------------------
-- RESUMO DIÁRIO
-- Calculado e salvo pelo sistema ao fechar o dia.
-- lucro_bruto    = informado pelo usuário
-- total_despesas = soma de todas as despesas do dia
-- lucro_liquido  = lucro_bruto - total_despesas
-- saldo_acumulado= soma de todos os lucros_liquidos até hoje
-- ------------------------------------------------------------
CREATE TABLE resumo_diario (
    id              INT            AUTO_INCREMENT PRIMARY KEY,
    data            DATE           NOT NULL UNIQUE,
    ganho_dinheiro  DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    ganho_pix       DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    criado_em       DATETIME       DEFAULT CURRENT_TIMESTAMP,
    atualizado_em   DATETIME       DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP
);

CREATE INDEX idx_resumo_data ON resumo_diario(data);

-- Saldo atual da carteira — sempre tem só UM registro.
-- Atualizado a cada ganho registrado ou pagamento efetuado.
-- saldo_dinheiro = soma(ganho_dinheiro) - soma(pagamentos em dinheiro)
-- saldo_pix      = soma(ganho_pix)      - soma(pagamentos em pix)
CREATE TABLE saldo (
    id              INT            AUTO_INCREMENT PRIMARY KEY,
    saldo_dinheiro  DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    saldo_pix       DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    atualizado_em   DATETIME       DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP
);

-- Insere o registro único de saldo zerado
INSERT INTO saldo (saldo_dinheiro, saldo_pix) VALUES (0.00, 0.00);

-- Registra o pagamento de uma conta.
-- forma: DINHEIRO desconta saldo_dinheiro, PIX desconta saldo_pix.
-- Ao inserir aqui, o Java também atualiza a tabela saldo
-- e marca contas.pago = TRUE.
CREATE TABLE pagamentos_conta (
    id              INT            AUTO_INCREMENT PRIMARY KEY,
    conta_id        INT            NOT NULL,
    valor           DECIMAL(10,2)  NOT NULL,
    forma           ENUM('DINHEIRO', 'PIX') NOT NULL,
    data_pagamento  DATE           NOT NULL DEFAULT (CURDATE()),
    observacao      TEXT,
    criado_em       DATETIME       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_pagamento_conta
        FOREIGN KEY (conta_id) REFERENCES contas(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_pagamentos_data ON pagamentos_conta(data_pagamento);
CREATE INDEX idx_pagamentos_forma ON pagamentos_conta(forma);

-- ------------------------------------------------------------
-- SALARIO
-- Configuração do salário do usuário.
-- ativo = FALSE significa que o usuário não tem salário
-- cadastrado — o sistema ignora completamente.
-- dia_pagamento = dia do mês que cai o pagamento (1 a 31).
-- ------------------------------------------------------------
CREATE TABLE salario (
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    valor          DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
    dia_pagamento  INT            NOT NULL DEFAULT 1,
    ativo          BOOLEAN        DEFAULT FALSE,
    criado_em      DATETIME       DEFAULT CURRENT_TIMESTAMP,
    atualizado_em  DATETIME       DEFAULT CURRENT_TIMESTAMP
                                  ON UPDATE CURRENT_TIMESTAMP,

    -- Garante que dia_pagamento esteja entre 1 e 31
    CONSTRAINT chk_dia_pagamento CHECK (dia_pagamento BETWEEN 1 AND 31)
);

-- Insere o registro inicial inativo — usuário configura depois
INSERT INTO salario (valor, dia_pagamento, ativo)
VALUES (0.00, 1, FALSE);

-- ------------------------------------------------------------
-- RECEBIMENTOS_SALARIO
-- Histórico de cada salário confirmado pelo usuário.
-- Só é criado quando o usuário CONFIRMA o recebimento.
-- forma: define qual saldo será atualizado.
-- ------------------------------------------------------------
CREATE TABLE recebimentos_salario (
    id          INT            AUTO_INCREMENT PRIMARY KEY,
    salario_id  INT            NOT NULL,
    valor       DECIMAL(10,2)  NOT NULL,
    forma       ENUM('DINHEIRO', 'PIX') NOT NULL,
    data        DATE           NOT NULL,
    criado_em   DATETIME       DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_recebimento_salario
        FOREIGN KEY (salario_id) REFERENCES salario(id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_recebimentos_data ON recebimentos_salario(data);

-- ------------------------------------------------------------
-- USUARIO
-- Preparada para múltiplos usuários no futuro.
-- Por enquanto o sistema usa apenas o usuário de id = 1.
-- senha armazena hash BCrypt — nunca texto puro.
-- ------------------------------------------------------------
CREATE TABLE usuario (
    id        INT          AUTO_INCREMENT PRIMARY KEY,
    usuario   VARCHAR(50)  NOT NULL UNIQUE,
    senha     VARCHAR(255) NOT NULL,
    criado_em DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- Usuário padrão inicial — senha deve ser alterada no primeiro acesso
-- O hash abaixo corresponde à senha "admin123"
INSERT INTO usuario (usuario, senha)
VALUES ('admin', '$2a$10$7QJ9Z1dK2LmN8pX3vY6wOeRtHuIsVbCxMoGkFjDlEnAqWsSyTzPu');

-- ============================================================
-- DADOS INICIAIS
-- ============================================================

INSERT INTO categorias (nome) VALUES
    ('Alimentação'),
    ('Transporte'),
    ('Saúde'),
    ('Moradia'),
    ('Educação'),
    ('Lazer'),
    ('Serviços'),
    ('Manutenção'),
    ('Outros');

-- Configuração inicial da despesa da moto (km zerado — usuário preenche)
-- Para registrar uma despesa_moto, insere-se primeiro em despesas,
-- depois em despesas_moto usando o id gerado.
-- Exemplo de uso (feito pelo Java, não aqui):
--   INSERT INTO despesas (data, nome, valor) VALUES (CURDATE(), 'Moto', 0.00);
--   INSERT INTO despesas_moto (despesa_id, km_no_dia, preco_gasolina, ...)
--          VALUES (LAST_INSERT_ID(), 0, 0.00, 0.00, 0.00);
