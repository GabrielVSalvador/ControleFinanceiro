import db.ConexaoDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

/**
 * Ponto de entrada da aplicação.
 *
 * Por enquanto só testa a conexão com o banco e lista
 * as categorias cadastradas no schema.sql.
 * Nas próximas etapas vira o menu principal do sistema.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("=== Controle Financeiro ===");
        System.out.println("Conectando ao banco...\n");

        // O bloco try-finally garante que a conexão é sempre fechada,
        // mesmo que ocorra um erro durante a execução.
        try {
            testarConexao();
        } finally {
            // Sempre executa — fecha a conexão ao terminar
            ConexaoDB.fechar();
        }
    }

    /**
     * Testa a conexão abrindo-a e fazendo uma consulta simples.
     * Se funcionar, lista as categorias do banco.
     */
    private static void testarConexao() {
        try {
            // Pede a conexão ao ConexaoDB
            Connection con = ConexaoDB.getConexao();

            System.out.println("Testando consulta — categorias cadastradas:");
            System.out.println("-------------------------------------------");

            // Statement executa SQL simples (sem parâmetros variáveis)
            // Nas próximas etapas usaremos PreparedStatement no lugar dele
            Statement stmt = con.createStatement();
            ResultSet rs   = stmt.executeQuery("SELECT id, nome FROM categorias ORDER BY nome");

            // Percorre os resultados linha por linha
            while (rs.next()) {
                int    id   = rs.getInt("id");
                String nome = rs.getString("nome");
                System.out.printf("  [%d] %s%n", id, nome);
            }

            // Boa prática: fechar ResultSet e Statement após usar
            rs.close();
            stmt.close();

            System.out.println("\nConexão funcionando corretamente!");

        } catch (SQLException e) {
            System.err.println("ERRO ao conectar ao banco:");
            System.err.println(e.getMessage());
        }
    }
}
