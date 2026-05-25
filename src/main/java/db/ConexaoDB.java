package db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Gerencia a conexão com o banco de dados MySQL via JDBC.
 *
 * Padrão utilizado: Singleton
 *   - Garante que apenas UMA conexão seja aberta durante toda
 *     a execução do programa.
 *   - Se a conexão cair, abre automaticamente uma nova.
 *
 * As configurações (url, usuário, senha) são lidas do arquivo
 * src/main/resources/db.properties — nunca ficam no código-fonte.
 */
public class ConexaoDB {

    // A única instância da conexão — começa nula, é criada na 1ª chamada
    private static Connection conexao;

    /**
     * Retorna a conexão ativa com o banco.
     * Se ainda não existir ou estiver fechada, abre uma nova.
     *
     * @return Connection — objeto de conexão JDBC
     * @throws SQLException se não conseguir conectar ao banco
     */
    public static Connection getConexao() throws SQLException {

        // Só abre nova conexão se não existir ou estiver fechada
        if (conexao == null || conexao.isClosed()) {
            conexao = abrirConexao();
        }
        return conexao;
    }

    /**
     * Fecha a conexão com o banco de forma segura.
     * Deve ser chamado ao encerrar o programa (no Main).
     */
    public static void fechar() {
        if (conexao != null) {
            try {
                if (!conexao.isClosed()) {
                    conexao.close();
                    System.out.println("Conexão encerrada.");
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // -------------------------------------------------------
    // Métodos privados (detalhes internos da classe)
    // -------------------------------------------------------

    /**
     * Lê as configurações do db.properties e abre a conexão.
     * É private porque só esta classe precisa chamar.
     */
    private static Connection abrirConexao() throws SQLException {
        Properties config = lerConfiguracoes();

        String url     = config.getProperty("db.url");
        String usuario = config.getProperty("db.usuario");
        String senha   = config.getProperty("db.senha");

        try {
            // Carrega o driver do MySQL na memória
            // (necessário para o Java encontrar o mysql-connector.jar)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Banco conectado com sucesso.");
            return con;

        } catch (ClassNotFoundException e) {
            throw new SQLException(
                "Driver MySQL não encontrado!\n" +
                "Verifique se o arquivo mysql-connector-j.jar está na pasta lib/",
                e
            );
        }
    }

    /**
     * Lê o arquivo db.properties do classpath.
     *
     * "Classpath" é o conjunto de pastas que o Java conhece.
     * O arquivo db.properties fica em src/main/resources/,
     * que deve estar no classpath para ser encontrado aqui.
     */
    private static Properties lerConfiguracoes() throws SQLException {
        Properties props = new Properties();

        // getResourceAsStream busca o arquivo no classpath
        try (InputStream entrada = ConexaoDB.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (entrada == null) {
                throw new SQLException(
                    "Arquivo db.properties não encontrado!\n" +
                    "Crie o arquivo em src/main/resources/db.properties\n" +
                    "Use o db.properties.example como modelo."
                );
            }

            props.load(entrada);
            return props;

        } catch (IOException e) {
            throw new SQLException("Erro ao ler db.properties: " + e.getMessage(), e);
        }
    }
}
