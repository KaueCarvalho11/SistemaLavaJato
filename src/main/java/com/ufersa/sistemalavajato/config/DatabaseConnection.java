package com.ufersa.sistemalavajato.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por gerenciar conexões com o banco de dados SQLite.
 * Implementa padrão Singleton para garantir uma única instância.
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private static final String DB_NAME = "sistema_lava_jato.db";
    private static final String DB_PATH = "database/";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH + DB_NAME;
    private static final String MIGRATIONS_TABLE = "migrations";
    private boolean isInitialized = false;

    private DatabaseConnection() {
        try {
            // Cria o diretório do banco se não existir
            createDatabaseDirectory();

            // Carrega o driver SQLite
            Class.forName("org.sqlite.JDBC");

            // Testa a conexão
            testConnection();

            // Inicializa a estrutura do banco
            initializeDatabase();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite não encontrado", e);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao conectar com o banco de dados", e);
        }
    }

    /**
     * Obtém a instância única do DatabaseConnection (Singleton).
     * 
     * @return Instância do DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Obtém uma nova conexão com o banco de dados.
     * 
     * @return Conexão com o banco
     * @throws SQLException Se houver erro na conexão
     */
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(DB_URL);

        // Habilita chaves estrangeiras no SQLite
        connection.createStatement().execute("PRAGMA foreign_keys = ON;");

        return connection;
    }

    /**
     * Cria o diretório do banco de dados se não existir.
     */
    private void createDatabaseDirectory() {
        try {
            Path dbPath = Paths.get(DB_PATH);
            if (!Files.exists(dbPath)) {
                Files.createDirectories(dbPath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar diretório do banco", e);
        }
    }

    /**
     * Testa a conexão com o banco de dados.
     */
    private void testConnection() throws SQLException {
        System.out.println("=== TESTE DE CONEXÃO COM BANCO DE DADOS ===");
        System.out.println("Tentando conectar ao banco: " + DB_URL);

        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            System.out.println("✓ Conexão estabelecida com sucesso!");

            // Testa se a conexão está funcionando
            connection.createStatement().execute("SELECT 1;");
            System.out.println("✓ Teste de query executado com sucesso!");

            // Verifica informações do banco
            System.out.println("✓ Informações do banco:");
            System.out.println("  - Caminho do arquivo: " + getDatabasePath());
            System.out.println("  - URL de conexão: " + DB_URL);
            System.out.println("  - Driver: SQLite JDBC");

        } catch (SQLException e) {
            System.err.println("✗ Erro ao conectar com o banco de dados:");
            System.err.println("  - Mensagem: " + e.getMessage());
            System.err.println("  - Código de erro: " + e.getErrorCode());
            throw e;
        }

        System.out.println("=== TESTE DE CONEXÃO CONCLUÍDO ===");
    }

    /**
     * Fecha uma conexão de forma segura.
     * 
     * @param connection Conexão a ser fechada
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    /**
     * Obtém o caminho completo do banco de dados.
     * 
     * @return Caminho do banco
     */
    public String getDatabasePath() {
        return DB_PATH + DB_NAME;
    }

    /**
     * Obtém a URL de conexão do banco.
     * 
     * @return URL de conexão
     */
    public String getDatabaseUrl() {
        return DB_URL;
    }

    /**
     * Inicializa a estrutura do banco de dados com migrations.
     */
    private void initializeDatabase() throws SQLException {
        if (isInitialized) {
            System.out.println("✓ Banco de dados já foi inicializado anteriormente");
            return;
        }

        System.out.println("=== INICIALIZANDO BANCO DE DADOS ===");

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {

            // Cria tabela de migrations se não existir
            System.out.println("Criando tabela de migrations...");
            createMigrationsTable(statement);

            // Executa todas as migrations
            System.out.println("Executando migrations...");
            runMigrations(statement);

            isInitialized = true;
            System.out.println("✓ Banco de dados inicializado com sucesso!");

        } catch (SQLException e) {
            System.err.println("✗ Erro ao inicializar banco de dados: " + e.getMessage());
            throw e;
        }

        System.out.println("=== INICIALIZAÇÃO CONCLUÍDA ===");
    }

    /**
     * Cria a tabela de controle de migrations.
     */
    private void createMigrationsTable(Statement statement) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + MIGRATIONS_TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "version TEXT NOT NULL UNIQUE, " +
                "description TEXT NOT NULL, " +
                "executed_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        statement.execute(sql);
    }

    /**
     * Executa todas as migrations necessárias.
     */
    private void runMigrations(Statement statement) throws SQLException {
        List<Migration> migrations = getMigrations();
        int executedMigrations = 0;
        int skippedMigrations = 0;

        System.out.println("Total de migrations disponíveis: " + migrations.size());

        for (Migration migration : migrations) {
            if (!isMigrationExecuted(statement, migration.getVersion())) {
                System.out.println(
                        "📄 Executando migration " + migration.getVersion() + ": " + migration.getDescription());
                try {
                    migration.execute(statement);
                    recordMigration(statement, migration);
                    System.out.println("✓ Migration " + migration.getVersion() + " executada com sucesso!");
                    executedMigrations++;
                } catch (SQLException e) {
                    System.err
                            .println("✗ Erro ao executar migration " + migration.getVersion() + ": " + e.getMessage());
                    throw e;
                }
            } else {
                System.out.println("⏭ Migration " + migration.getVersion() + " já foi executada anteriormente");
                skippedMigrations++;
            }
        }

        System.out.println("📊 Resumo das migrations:");
        System.out.println("  - Executadas: " + executedMigrations);
        System.out.println("  - Ignoradas: " + skippedMigrations);
        System.out.println("  - Total: " + migrations.size());
    }

    /**
     * Verifica se uma migration já foi executada.
     */
    private boolean isMigrationExecuted(Statement statement, String version) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + MIGRATIONS_TABLE + " WHERE version = '" + version + "'";
        ResultSet resultSet = statement.executeQuery(sql);
        return resultSet.next() && resultSet.getInt(1) > 0;
    }

    /**
     * Registra uma migration como executada.
     */
    private void recordMigration(Statement statement, Migration migration) throws SQLException {
        String sql = "INSERT INTO " + MIGRATIONS_TABLE + " (version, description) VALUES ('" +
                migration.getVersion() + "', '" + migration.getDescription() + "')";
        statement.execute(sql);
    }

    /**
     * Retorna todas as migrations ordenadas por versão.
     */
    private List<Migration> getMigrations() {
        List<Migration> migrations = new ArrayList<>();

        // Migration 1: Criar tabela de usuários
        migrations.add(new Migration("001", "Criar tabela usuarios") {
            @Override
            public void execute(Statement statement) throws SQLException {
                String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                        "id TEXT PRIMARY KEY, " +
                        "nome TEXT NOT NULL, " +
                        "email TEXT NOT NULL UNIQUE, " +
                        "senha TEXT NOT NULL, " +
                        "senha_hash TEXT NOT NULL, " +
                        "tipo_usuario TEXT NOT NULL CHECK (tipo_usuario IN ('CLIENTE', 'FUNCIONARIO')), " +
                        "endereco TEXT, " +
                        "numero_telefone TEXT, " +
                        "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP" +
                        ")";
                statement.execute(sql);
                statement.execute("CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email)");
            }
        });

        // Migration 2: Criar tabela de clientes
        migrations.add(new Migration("002", "Criar tabela clientes") {
            @Override
            public void execute(Statement statement) throws SQLException {
                String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                        "id_usuario TEXT PRIMARY KEY, " +
                        "endereco TEXT NOT NULL, " +
                        "numero_telefone INTEGER NOT NULL, " +
                        "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE" +
                        ")";
                statement.execute(sql);
            }
        });

        // Migration 3: Criar tabela de funcionários
        migrations.add(new Migration("003", "Criar tabela funcionarios") {
            @Override
            public void execute(Statement statement) throws SQLException {
                String sql = "CREATE TABLE IF NOT EXISTS funcionarios (" +
                        "id_usuario TEXT PRIMARY KEY, " +
                        "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE" +
                        ")";
                statement.execute(sql);
            }
        });

        migrations.add(new Migration("004", "Criar tabela veiculos") {
            @Override
            public void execute(Statement statement) throws SQLException {
                String sql = ""
                        + "CREATE TABLE IF NOT EXISTS veiculos ("
                        + "num_chassi INTEGER PRIMARY KEY, "
                        + "id_cliente TEXT NOT NULL, "
                        + "modelo TEXT NOT NULL, "
                        + "quilometragem REAL DEFAULT 0, "
                        + "preco REAL, "
                        + "cor TEXT, "
                        + "ano_fabricacao INTEGER, "
                        + "status TEXT DEFAULT 'ATIVO', "
                        + "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, "
                        + "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP, "
                        + "FOREIGN KEY (id_cliente) REFERENCES clientes(id_usuario) ON DELETE CASCADE"
                        + ")";
                statement.execute(sql);
                // índice no cliente
                statement.execute("CREATE INDEX IF NOT EXISTS idx_veiculos_cliente ON veiculos(id_cliente)");
            }
        });

        // Migration 5: Criar tabela de serviços
        migrations.add(new Migration("005", "Criar tabela servicos") {
            @Override
            public void execute(Statement statement) throws SQLException {
                String sql = ""
                        + "CREATE TABLE IF NOT EXISTS servicos ("
                        + "id_servico INTEGER PRIMARY KEY, "
                        + "tipo TEXT NOT NULL, "
                        + "descricao TEXT, "
                        + "preco REAL NOT NULL, "
                        + "status TEXT DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'EM_ANDAMENTO', 'CONCLUIDO', 'CANCELADO')), "
                        + "forma_pagamento TEXT, "
                        + "num_chassi INTEGER NOT NULL, " // FK ajustado
                        + "id_funcionario TEXT NOT NULL, "
                        + "data_inicio DATETIME, "
                        + "data_conclusao DATETIME, "
                        + "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, "
                        + "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP, "
                        + "FOREIGN KEY (num_chassi) REFERENCES veiculos(num_chassi) ON DELETE CASCADE, "
                        + "FOREIGN KEY (id_funcionario) REFERENCES funcionarios(id_usuario) ON DELETE CASCADE"
                        + ")";
                statement.execute(sql);
                statement.execute("CREATE INDEX IF NOT EXISTS idx_servicos_veiculo ON servicos(num_chassi)");
                statement.execute("CREATE INDEX IF NOT EXISTS idx_servicos_funcionario ON servicos(id_funcionario)");
                statement.execute("CREATE INDEX IF NOT EXISTS idx_servicos_status ON servicos(status)");
            }
        });

        // Migration 6: Criar tabela de agendamento
        migrations.add(new Migration("006", "Criar tabela agendamentos") {
            @Override
            public void execute(Statement statement) throws SQLException {
                String sql = ""
                        + "CREATE TABLE IF NOT EXISTS agendamentos ("
                        + "id TEXT PRIMARY KEY, "
                        + "id_cliente TEXT NOT NULL, "
                        + "num_chassi INTEGER NOT NULL, " // FK ajustado
                        + "id_funcionario TEXT, "
                        + "data_hora DATETIME NOT NULL, "
                        + "status TEXT DEFAULT 'AGENDADO' CHECK (status IN ('AGENDADO', 'EM_ANDAMENTO', 'CONCLUIDO', 'CANCELADO')), "
                        + "observacoes TEXT, "
                        + "preco_total REAL, "
                        + "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, "
                        + "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP, "
                        + "FOREIGN KEY (id_cliente) REFERENCES clientes(id_usuario) ON DELETE CASCADE, "
                        + "FOREIGN KEY (num_chassi) REFERENCES veiculos(num_chassi) ON DELETE CASCADE, "
                        + "FOREIGN KEY (id_funcionario) REFERENCES funcionarios(id_usuario) ON DELETE SET NULL"
                        + ")";
                statement.execute(sql);
                statement.execute("CREATE INDEX IF NOT EXISTS idx_agendamentos_cliente ON agendamentos(id_cliente)");
                statement.execute("CREATE INDEX IF NOT EXISTS idx_agendamentos_veiculo ON agendamentos(num_chassi)");
                statement.execute(
                        "CREATE INDEX IF NOT EXISTS idx_agendamentos_funcionario ON agendamentos(id_funcionario)");
                statement.execute("CREATE INDEX IF NOT EXISTS idx_agendamentos_data ON agendamentos(data_hora)");
            }
        });
        // Migration 7: Criar tabela de junção agendamento_servicos
        migrations.add(new Migration("007", "Criar tabela agendamento_servicos") {
            @Override
            public void execute(Statement statement) throws SQLException {
                String sql = "CREATE TABLE IF NOT EXISTS agendamento_servicos (" +
                        "id_agendamento TEXT NOT NULL, " +
                        "id_servico INTEGER NOT NULL, " +
                        "preco_acordado REAL, " +
                        "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "PRIMARY KEY (id_agendamento, id_servico), " +
                        "FOREIGN KEY (id_agendamento) REFERENCES agendamentos(id) ON DELETE CASCADE, " +
                        "FOREIGN KEY (id_servico) REFERENCES servicos(id_servico) ON DELETE CASCADE" +
                        ")";
                statement.execute(sql);
            }
        });

        // Migration 8: Inserir dados iniciais
        migrations.add(new Migration("008", "Inserir dados iniciais") {
            @Override
            public void execute(Statement statement) throws SQLException {
                // Verifica se já existem dados
                ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM usuarios");
                if (rs.next() && rs.getInt(1) == 0) {
                    // Gera hash da senha admin123
                    String senhaHash = com.ufersa.sistemalavajato.util.PasswordUtils.hashPassword("admin123");

                    // Insere usuário admin
                    String insertAdmin = "INSERT INTO usuarios (id, nome, email, senha, senha_hash, tipo_usuario) " +
                            "VALUES ('admin', 'Administrador', 'admin@lavajato.com', 'admin123', ?, 'FUNCIONARIO')";
                    java.sql.PreparedStatement ps = statement.getConnection().prepareStatement(insertAdmin);
                    ps.setString(1, senhaHash);
                    ps.execute();
                    ps.close();

                    // Insere funcionário admin
                    String insertFuncionario = "INSERT INTO funcionarios (id_usuario) VALUES ('admin')";
                    statement.execute(insertFuncionario);

                    System.out.println("Usuário admin criado - Email: admin@lavajato.com | Senha: admin123");
                }
            }
        });

        return migrations;
    }

    /**
     * Classe abstrata para representar uma migration.
     */
    private abstract class Migration {
        private final String version;
        private final String description;

        public Migration(String version, String description) {
            this.version = version;
            this.description = description;
        }

        public String getVersion() {
            return version;
        }

        public String getDescription() {
            return description;
        }

        public abstract void execute(Statement statement) throws SQLException;
    }

    /**
     * Força a re-execução de todas as migrations (cuidado - apaga dados).
     */
    public void resetDatabase() throws SQLException {
        try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {

            // Remove todas as tabelas
            statement.execute("DROP TABLE IF EXISTS agendamento_servicos");
            statement.execute("DROP TABLE IF EXISTS agendamentos");
            statement.execute("DROP TABLE IF EXISTS servicos");
            statement.execute("DROP TABLE IF EXISTS veiculos");
            statement.execute("DROP TABLE IF EXISTS funcionarios");
            statement.execute("DROP TABLE IF EXISTS clientes");
            statement.execute("DROP TABLE IF EXISTS usuarios");
            statement.execute("DROP TABLE IF EXISTS " + MIGRATIONS_TABLE);

            // Reinicializa
            isInitialized = false;
            initializeDatabase();

            System.out.println("Banco de dados resetado com sucesso!");
        }
    }

    /**
     * Verifica se o banco está inicializado.
     */
    public boolean isInitialized() {
        return isInitialized;
    }

}
