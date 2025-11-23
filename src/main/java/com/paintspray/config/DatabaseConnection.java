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
            
            // Tabela usuarios - Proprietário da oficina
            stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id TEXT PRIMARY KEY, " +
                "nome TEXT NOT NULL, " +
                "email TEXT UNIQUE NOT NULL, " +
                "senha TEXT NOT NULL)");

            // Tabela clientes
            stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                "id TEXT PRIMARY KEY, " +           
                "nome TEXT NOT NULL, " +
                "endereco TEXT, " +
                "numero_telefone TEXT, " +          
                "data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP)");

            // Tabela veículos (ajustada para motos)
            stmt.execute("CREATE TABLE IF NOT EXISTS veiculos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                    "modelo TEXT NOT NULL, " +
                    "cor TEXT, " +
                    "ano_fabricacao INTEGER, " +
                    "id_cliente TEXT NOT NULL, " +
                    "FOREIGN KEY(id_cliente) REFERENCES clientes(id) ON DELETE CASCADE)");

            // Tabela servicos (atualizada com novos campos)
            stmt.execute("CREATE TABLE IF NOT EXISTS servicos (" +
                    "id_servico INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "tipo TEXT NOT NULL, " +
                    "descricao TEXT, " +
                    "preco REAL, " +
                    "status TEXT DEFAULT 'PENDENTE', " +
                    "forma_pagamento TEXT, " +
                    "id_veiculo INTEGER NOT NULL, " +
                    "id_usuario TEXT NOT NULL, " +
                    "FOREIGN KEY(id_veiculo) REFERENCES veiculos(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY(id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE)");
        }
    }
}