package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Conta {
    
    protected int        id;
    protected int        categoriaId;
    protected String     categoriaNome;
    protected String     nome;
    protected BigDecimal valor;
    protected LocalDate  vencimento;
    protected boolean    pago;
    protected String     observacao;

    public Conta() {}

    public Conta(int categoriaId, String nome, BigDecimal valor, LocalDate vencimento) {
        this.categoriaId = categoriaId;
        this.nome        = nome;
        this.valor       = valor;
        this.vencimento  = vencimento;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public int getCategoriaId()                 { return categoriaId; }
    public void setCategoriaId(int v)           { this.categoriaId = v; }

    public String getCategoriaNome()            { return categoriaNome; }
    public void setCategoriaNome(String v)      { this.categoriaNome = v; }

    public String getNome()                     { return nome; }
    public void setNome(String nome)            { this.nome = nome; }

    public BigDecimal getValor()                { return valor; }
    public void setValor(BigDecimal valor)      { this.valor = valor; }

    public LocalDate getVencimento()            { return vencimento; }
    public void setVencimento(LocalDate v)      { this.vencimento = v; }

    public boolean isPago()                     { return pago; }
    public void setPago(boolean pago)           { this.pago = pago; }

    public String getObservacao()               { return observacao; }
    public void setObservacao(String v)         { this.observacao = v; }

    @Override
    public String toString() {
        return "Conta{id=" + id + ", nome='" + nome +"', valor=" + valor + ", vencimento=" + vencimento +", pago=" + pago + "}";
    }
}
