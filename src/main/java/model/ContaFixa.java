package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaFixa extends Conta { 

    public ContaFixa() {}
    public ContaFixa(int categoriaId, String nome, BigDecimal valor, LocalDate vencimento) {
        super(categoriaId, nome, valor, vencimento);
    }

    @Override
    public String toString() {
        return "ContaFixa{" + super.toString() + "}";
    }
}
