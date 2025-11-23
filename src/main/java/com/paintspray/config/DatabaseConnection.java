package com.paintspray.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private final String URL = "jdbc:sqlite:paintspray.db"; 

    private DatabaseConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(URL);
            
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
            
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new DatabaseConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            
            // REQUISITO KANBAN: Tabela clientes independente (sem FK para usuarios)
            stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                "id TEXT PRIMARY KEY, " +           
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "numero_telefone TEXT, " +          
                "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP)");

            // Ajuste necessário para veículos (Chassi como TEXTO)
            stmt.execute("CREATE TABLE IF NOT EXISTS veiculos (" +
                    "num_chassi TEXT PRIMARY KEY, " + 
                    "modelo TEXT NOT NULL, " +
                    "cor TEXT, " +
                    "ano INTEGER, " +
                    "id_cliente INTEGER, " +
                    "FOREIGN KEY(id_cliente) REFERENCES clientes(id) ON DELETE CASCADE)");

            // REQUISITO KANBAN: Atualizar tabela servicos (Campo 'tipo' pronto para os novos Enums)
            stmt.execute("CREATE TABLE IF NOT EXISTS servicos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tipo TEXT NOT NULL, " + // Vai receber 'Pintura', 'Funilaria', etc.
                    "descricao TEXT, " +
                    "preco REAL, " +
                    "status TEXT, " +
                    "num_chassi TEXT, " +
                    "data_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "data_conclusao TIMESTAMP, " +
                    "FOREIGN KEY(num_chassi) REFERENCES veiculos(num_chassi) ON DELETE CASCADE)");
        }
    }
}