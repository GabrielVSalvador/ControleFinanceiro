package model;

import java.math.BigDecimal;

public class Salario {
    private int id;
    private BigDecimal valor;
    private int dia_pagamento;
    private boolean ativo;

    public Salario(BigDecimal valor, int dia_pagamento, boolean ativo) {
        this.valor = valor;
        this.dia_pagamento = dia_pagamento;
        this.ativo = ativo;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getDia_pagamento() {
        return dia_pagamento;
    }

    public void setDia_pagamento(int dia_pagamento) {
        this.dia_pagamento = dia_pagamento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
