package model;

/**
 * Representa uma linha da tabela "usuario".
 * senha armazena o hash BCrypt — nunca a senha em texto puro.
 *
 * Atualmente o sistema usa apenas o usuário de id = 1.
 * A tabela já está preparada para múltiplos usuários no futuro.
 */
public class Usuario {

    private int    id;
    private String usuario;
    private String senha; // hash BCrypt

    public Usuario() {}

    public Usuario(String usuario, String senha) {
        this.usuario = usuario;
        this.senha   = senha;
    }

    public int getId()                       { return id; }
    public void setId(int id)                { this.id = id; }

    public String getUsuario()               { return usuario; }
    public void setUsuario(String usuario)   { this.usuario = usuario; }

    public String getSenha()                 { return senha; }
    public void setSenha(String senha)       { this.senha = senha; }

    @Override
    public String toString() {
        // Nunca imprime a senha — nem o hash!
        return "Usuario{id=" + id + ", usuario='" + usuario + "'}";
    }
}