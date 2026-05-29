package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaVariavel extends Conta {
    private int numParcelas;
    private int parcelaAtual;

    public ContaVariavel() {}

    public ContaVariavel(int categoriaId, String nome, BigDecimal valor, LocalDate vencimento, int numParcelas) {
        super(categoriaId, nome, valor, vencimento);
        this.numParcelas = numParcelas;
        this.parcelaAtual = 1;
    }

    public int getNumParcelas() {
        return numParcelas;
    }

    public void setNumParcelas(int numParcelas) {
        this.numParcelas = numParcelas;
    }

    public int getParcelaAtual() {
        return parcelaAtual;
    }

    public void setParcelaAtual(int parcelaAtual) {
        this.parcelaAtual = parcelaAtual;
    }

    @Override
    public String toString() {
        return "ContaVariavel{" + super.toString() + ", parcela=" + numParcelas + " / " + parcelaAtual + "}";
    }
}
