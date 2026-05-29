package model;

import java.math.BigDecimal;

public class Saldo {
    private int id;
    private BigDecimal saldoDinheiro;
    private BigDecimal saldoPix;

    public Saldo(){}

    public Saldo(BigDecimal saldoDinheiro, BigDecimal saldoPix) {
        this.saldoDinheiro = saldoDinheiro;
        this.saldoPix = saldoPix;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public BigDecimal getSaldoDinheiro() {
        return saldoDinheiro;
    }
    public void setSaldoDinheiro(BigDecimal saldoDinheiro) {
        this.saldoDinheiro = saldoDinheiro;
    }
    public BigDecimal getSaldoPix() {
        return saldoPix;
    }
    public void setSaldoPix(BigDecimal saldoPix) {
        this.saldoPix = saldoPix;
    }

    @Override
    public String toString() {
        return "Saldo{id=" + id + ", saldoDinheiro=" + saldoDinheiro + ", saldoPix=" + saldoPix + "}";
    }
}