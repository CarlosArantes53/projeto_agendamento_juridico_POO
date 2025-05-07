package dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import dao.base.BaseDAO;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;

public class UsuarioDAO extends BaseDAO {
 
    public int inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, telefone, tipo_usuario) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        return executeInsert(sql, stmt -> {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getTelefone());
            stmt.setString(5, usuario.getTipoUsuario().toString());
        });
    }
    
    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        
        return executeQuery(sql, 
            stmt -> stmt.setString(1, email),
            rs -> {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        );
    }
    
    public Usuario autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha = ?";
        
        return executeQuery(sql, 
            stmt -> {
                stmt.setString(1, email);
                stmt.setString(2, senha);
            },
            rs -> {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
                return null;
            }
        );
    }
    
    public boolean atualizarSenha(String email, String novaSenha) throws SQLException {
        String sql = "UPDATE usuarios SET senha = ? WHERE email = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> {
            stmt.setString(1, novaSenha);
            stmt.setString(2, email);
        });
        
        return linhasAfetadas > 0;
    }
    
    public boolean atualizarPerfil(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, tipo_usuario = ? WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getTipoUsuario().toString());
            stmt.setInt(5, usuario.getId());
        });
        
        return linhasAfetadas > 0;
    }
    
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setTipoUsuario(TipoUsuario.valueOf(rs.getString("tipo_usuario")));
        return usuario;
    }
}