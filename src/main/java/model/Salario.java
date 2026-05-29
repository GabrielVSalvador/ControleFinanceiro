package model;

import java.math.BigDecimal;

public class Salario {
    private int id;
    private BigDecimal valor;
    private int diaPagamento;
    private boolean ativo;

    public Salario(BigDecimal valor, int diaPagamento, boolean ativo) {
        this.valor = valor;
        this.diaPagamento = diaPagamento;
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

    public int getDiaPagamento() {
        return diaPagamento;
    }

    public void setDiaPagamento(int diaPagamento) {
        this.diaPagamento = diaPagamento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Salario{id=" + id + ", valor=" + valor + ", diaPagamento=" + diaPagamento + ", ativo=" + ativo + "}";
    }
}
