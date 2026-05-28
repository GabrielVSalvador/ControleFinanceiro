package model;

public class Categoria {
    private int id;
    private String nome;

        public Categoria() {}

    /** Construtor com dados — útil para criar e já inserir */
    public Categoria(String nome) {
        this.nome = nome;
    }

    // ---------------------------------------------------
    // Getters e Setters
    // ---------------------------------------------------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // ---------------------------------------------------
    // toString — útil para imprimir o objeto no console
    // ---------------------------------------------------

    @Override
    public String toString() {
        return "Categoria{id=" + id + ", nome='" + nome + "'}";
    }
}
