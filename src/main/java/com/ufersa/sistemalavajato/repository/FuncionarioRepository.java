package com.ufersa.sistemalavajato.repository;

import com.ufersa.sistemalavajato.model.Funcionario;
import com.ufersa.sistemalavajato.util.PasswordUtils;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository para operações CRUD da entidade Funcionario.
 * Lida com as tabelas 'usuarios' e 'funcionarios'.
 */
public class FuncionarioRepository extends BaseRepository<Funcionario> {

    @Override
    public void save(Funcionario funcionario) throws SQLException {
        /*
         * Para salvar um funcionário, precisamos inserir em duas tabelas:
         * Na tabela 'usuarios' com os dados principais e na tabela 'funcionarios' com o
         * ID do usuário
         * para marcá-lo como funcionário.
         * Usamos uma transação para garantir que ambas as operações aconteçam ou
         * nenhuma aconteça.
         */

        String sqlUsuario = "INSERT INTO usuarios (id, nome, email, senha, senha_hash, tipo_usuario) VALUES (?, ?, ?, ?, ?, 'FUNCIONARIO')";
        String sqlFuncionario = "INSERT INTO funcionarios (id_usuario) VALUES (?)";

        // A ferramenta executeTransaction vem da BaseRepository
        executeTransaction(connection -> {
            // Inseririndo na tabela 'usuarios'
            try (PreparedStatement stmtUsuario = connection.prepareStatement(sqlUsuario)) {
                stmtUsuario.setString(1, funcionario.getId());
                stmtUsuario.setString(2, funcionario.getNome());
                stmtUsuario.setString(3, funcionario.getEmail());
                stmtUsuario.setString(4, funcionario.getSenha());
                stmtUsuario.setString(5, PasswordUtils.hashPassword(funcionario.getSenha()));
                stmtUsuario.executeUpdate();
            }

            // Inseririndo na tabela 'funcionarios'
            try (PreparedStatement stmtFuncionario = connection.prepareStatement(sqlFuncionario)) {
                stmtFuncionario.setString(1, funcionario.getId());
                stmtFuncionario.executeUpdate();
            }
        });
    }

    @Override
    public Funcionario findById(String id) throws SQLException {
        // Usamos um JOIN para garantir que o usuário encontrado é de fato um
        // funcionário.
        String sql = "SELECT u.* FROM usuarios u " +
                "JOIN funcionarios f ON u.id = f.id_usuario " +
                "WHERE u.id = ?";

        return findOne(sql, this::mapResultSetToFuncionario, id);
    }

    public Funcionario EncontrarPorEmail(String email) throws SQLException {
        String sql = "SELECT u.* FROM usuarios u " +
                "JOIN funcionarios f ON u.id = f.id_usuario " +
                "WHERE u.email = ?";

        return findOne(sql, (rs) -> {
            // Aqui a gente monta um funcionário com os dados do banco
            return new Funcionario(
                    rs.getString("id"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("senha"));
        }, email);
    }

    @Override
    public void update(Funcionario funcionario) throws SQLException {
        // Por enquanto, a atualização afeta apenas a tabela de usuários.
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";

        executeUpdate(sql,
                funcionario.getNome(),
                funcionario.getEmail(),
                funcionario.getSenha(),
                funcionario.getId());
    }

    @Override
    public void delete(String id) throws SQLException {
        // Devido "ON DELETE CASCADE" que definimos no banco de dados,
        // ao deletar o usuário, o registro correspondente em 'funcionarios' será
        // apagado automaticamente.
        String sql = "DELETE FROM usuarios WHERE id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public List<Funcionario> findAll() throws SQLException {
        // Assim como no findById, usamos JOIN para listar apenas os usuários que são
        // funcionários.
        String sql = "SELECT u.* FROM usuarios u " +
                "JOIN funcionarios f ON u.id = f.id_usuario " +
                "ORDER BY u.nome";

        return findMany(sql, this::mapResultSetToFuncionario);
    }

    /**
     * Método "tradutor" que converte uma linha do banco de dados em um objeto
     * Funcionario.
     */
    private Funcionario mapResultSetToFuncionario(ResultSet rs) throws SQLException {
        return new Funcionario(
                rs.getString("id"),
                rs.getString("nome"),
                rs.getString("email"),
                null // senha não carregada por segurança
        );
    }
}