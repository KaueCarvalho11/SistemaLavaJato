package main.java.com.ufersa.sistemalavajato.repository;

import com.ufersa.sistemalavajato.model.Usuario;
import com.ufersa.sistemalavajato.model.Cliente;
import com.ufersa.sistemalavajato.model.Funcionario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UsuarioRepository extends BaseRepository<Usuario> {

    @Override
    public void save(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (id, nome, email, senha) VALUES (?, ?, ?, ?)";

        executeUpdate(sql,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha());
    }

    @Override
    public Usuario findById(String id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        return findOne(sql, this::mapResultSetToUsuario, id);
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, senha = ? WHERE id = ?";

        executeUpdate(sql,
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getId());
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        executeUpdate(sql, id);
    }

    @Override
    public List<Usuario> findAll() throws SQLException {
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        return findMany(sql, this::mapResultSetToUsuario);
    }

    /**
     * Busca usuário por email.
     * 
     * @param email Email do usuário
     * @return Usuario encontrado ou null
     * @throws SQLException Se houver erro na operação
     */
    public Usuario findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        return findOne(sql, this::mapResultSetToUsuario, email);
    }

    /**
     * Busca usuários por nome (busca parcial).
     * 
     * @param nome Nome ou parte do nome a ser buscado
     * @return Lista de usuários encontrados
     * @throws SQLException Se houver erro na operação
     */
    public List<Usuario> findByNome(String nome) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE nome LIKE ? ORDER BY nome";
        return findMany(sql, this::mapResultSetToUsuario, "%" + nome + "%");
    }

    /**
     * Verifica se um usuário existe pelo ID.
     * 
     * @param id ID do usuário
     * @return true se existe, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean existsById(String id) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE id = ?";
        return exists(sql, id);
    }

    /**
     * Verifica se um email já está em uso.
     * 
     * @param email Email a ser verificado
     * @return true se já existe, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE email = ?";
        return exists(sql, email);
    }

  
    /**
     * Atualiza apenas a senha do usuário.
     * 
     * @param id    ID do usuário
     * @param senha Nova senha
     * @throws SQLException Se houver erro na operação
     */
    public void updateSenha(String id, String senha) throws SQLException {
        String sql = "UPDATE usuarios SET senha = ? WHERE id = ?";
        executeUpdate(sql, senha, id);
    }

    /**
     * Realiza autenticação do usuário.
     * 
     * @param email Email do usuário
     * @param senha Senha para verificação
     * @return Usuario autenticado ou null se não encontrado
     * @throws SQLException Se houver erro na operação
     */
    public Usuario authenticate(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        return findOne(sql, this::mapResultSetToUsuario, email, senha);
    }

    /**
     * Mapeia ResultSet para objeto Usuario.
     * Como Usuario é abstrato, retorna uma implementação concreta baseada no
     * contexto.
     * 
     * @param rs ResultSet do banco de dados
     * @return Usuario mapeado
     * @throws SQLException Se houver erro no mapeamento
     */
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        String id = rs.getString("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        // Por padrão, retorna como Cliente (pode ser ajustado baseado em algum campo
        // tipo)
        // Se existir um campo 'tipo' na tabela, pode ser usado para determinar o tipo
        // correto
        return new Cliente(id, nome, email, senha, "", "");
    }

    /**
     * Verifica se um usuário é cliente.
     * 
     * @param id ID do usuário
     * @return true se é cliente, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean isCliente(String id) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE id_usuario = ?";
        return exists(sql, id);
    }

    /**
     * Verifica se um usuário é funcionário.
     * 
     * @param id ID do usuário
     * @return true se é funcionário, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean isFuncionario(String id) throws SQLException {
        String sql = "SELECT 1 FROM funcionarios WHERE id_usuario = ?";
        return exists(sql, id);
    }

    /**
     * Busca todos os usuários que são clientes.
     * 
     * @return Lista de usuários que são clientes
     * @throws SQLException Se houver erro na operação
     */
    public List<Usuario> findAllClientes() throws SQLException {
        String sql = "SELECT u.* FROM usuarios u " +
                "INNER JOIN clientes c ON u.id = c.id_usuario " +
                "ORDER BY u.nome";
        return findMany(sql, this::mapResultSetToUsuario);
    }

    /**
     * Busca todos os usuários que são funcionários.
     * 
     * @return Lista de usuários que são funcionários
     * @throws SQLException Se houver erro na operação
     */
    public List<Usuario> findAllFuncionarios() throws SQLException {
        String sql = "SELECT u.* FROM usuarios u " +
                "INNER JOIN funcionarios f ON u.id = f.id_usuario " +
                "ORDER BY u.nome";
        return findMany(sql, this::mapResultSetToUsuario);
    }
}
