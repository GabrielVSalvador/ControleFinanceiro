# 💰 Controle Financeiro

Sistema de controle financeiro pessoal desenvolvido em **Java puro com JDBC e MySQL**.

> Projeto construído passo a passo com fins de aprendizado.

---

## 📋 Funcionalidades

- **Contas** — cadastro de contas fixas e variáveis, com parcelas e vencimento
- **Receita diária** — registro do lucro bruto do dia
- **Despesas do dia** — variáveis (avulsas) e fixas calculadas automaticamente
- **Resumo diário** — lucro bruto, despesas, lucro líquido e saldo acumulado
- **Alertas** — aviso automático de contas que vencem hoje
- **Relatórios** — filtros por período, categoria e nome

---

## 🧮 Cálculos Automáticos

```
Gasto Gasolina   = (preço da gasolina ÷ 30) × lucro bruto do dia
Gasto Manutenção = gasto gasolina ÷ 2
Lucro Líquido    = lucro bruto − gasolina − manutenção − despesas variáveis
Saldo Acumulado  = soma de todos os lucros líquidos até hoje
```

---

## 🗂️ Estrutura do Projeto

```
controle-financeiro/
├── docs/                          # Documentação e diagramas
├── src/main/java/
│   ├── db/
│   │   └── ConexaoDB.java         # Gerencia a conexão com o MySQL
│   ├── model/
│   │   ├── Categoria.java
│   │   ├── Conta.java
│   │   ├── Receita.java
│   │   ├── DespesaVariavel.java
│   │   └── ResumoDiario.java
│   ├── dao/
│   │   ├── ContaDAO.java
│   │   ├── ReceitaDAO.java
│   │   ├── DespesaVariavelDAO.java
│   │   └── ResumoDiarioDAO.java
│   ├── service/
│   │   ├── AlertaService.java
│   │   └── RelatorioService.java
│   ├── util/
│   │   └── Formatador.java
│   └── Main.java
├── src/main/resources/
│   └── db.properties.example
├── schema.sql
├── .gitignore
└── README.md
```

---

## ⚙️ Como Configurar

### Pré-requisitos
- Java 11 ou superior
- MySQL 8.0 ou superior
- Driver JDBC: `mysql-connector-j-8.x.jar`

### 1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/controle-financeiro.git
cd controle-financeiro
```

### 2. Crie o banco de dados
```bash
mysql -u root -p < schema.sql
```

### 3. Configure a conexão
Copie o arquivo de exemplo e preencha com seus dados:
```bash
cp src/main/resources/db.properties.example src/main/resources/db.properties
```

Edite `db.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/controle_financeiro?useSSL=false&serverTimezone=America/Sao_Paulo
db.usuario=root
db.senha=sua_senha_aqui
```

### 4. Compile e execute
```bash
javac -cp mysql-connector-j.jar -d bin src/main/java/**/*.java src/main/java/Main.java
java -cp bin:mysql-connector-j.jar Main
```

---

## 🛠️ Tecnologias

| Tecnologia | Uso |
|---|---|
| Java 11+ | Linguagem principal |
| MySQL 8 | Banco de dados |
| JDBC | Acesso ao banco (sem frameworks) |

---

## 📅 Histórico de Desenvolvimento

| Etapa | Descrição | Status |
|---|---|---|
| 1 | Estrutura do projeto + Git | ✅ |
| 2 | Schema do banco de dados | 🔄 |
| 3 | Conexão JDBC | ⏳ |
| 4 | Models | ⏳ |
| 5 | DAOs | ⏳ |
| 6 | Services (alertas e relatórios) | ⏳ |
| 7 | Interface no console | ⏳ |
