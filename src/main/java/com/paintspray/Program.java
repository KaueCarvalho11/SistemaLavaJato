package com.paintspray;

import com.paintspray.config.DatabaseConnection;


public class Program {
    public static void main(String[] args) {
        // Inicializa conexão com banco de dados e cria tabelas
        DatabaseConnection.getInstance().getConnection();

        System.out.println("=== Paint-Spray - Sistema de Gestão ===");
        System.out.println("Banco de dados inicializado com sucesso!\n");
    }
}
