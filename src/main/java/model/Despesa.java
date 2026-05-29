package model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Representa uma linha da tabela "despesas".
 * A data é preenchida automaticamente pelo banco
 * no momento do cadastro.
 */
public class Despesa {

    private int        id;
    private String     nome;
    private BigDecimal valor;
    private LocalDate  data;

    public Despesa() {}

    public Despesa(String nome, BigDecimal valor) {
        this.nome  = nome;
        this.valor = valor;
        // data não entra no construtor — o banco preenche automaticamente
    }

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getNome()                    { return nome; }
    public void setNome(String nome)           { this.nome = nome; }

    public BigDecimal getValor()               { return valor; }
    public void setValor(BigDecimal valor)     { this.valor = valor; }

    public LocalDate getData()                 { return data; }
    public void setData(LocalDate data)        { this.data = data; }

    @Override
    public String toString() {
        return "Despesa{id=" + id + ", nome='" + nome +
               "', valor=" + valor + ", data=" + data + "}";
    }
}