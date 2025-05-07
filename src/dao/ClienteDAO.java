package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dao.base.BaseDAO;
import modelo.Cliente;

public class ClienteDAO extends BaseDAO {
 
    public int inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, documento, telefone, email, endereco) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        return executeInsert(sql, stmt -> {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getDocumento());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
        });
    }
    
    public boolean documentoExiste(String documento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE documento = ?";
        
        return executeQuery(sql, 
            stmt -> stmt.setString(1, documento),
            rs -> {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        );
    }
    
    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY nome";
        
        return executeQuery(sql, 
            null,
            rs -> mapList(rs, t -> {
                try {
                    return mapearCliente(t);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, id),
            rs -> {
                if (rs.next()) {
                    return mapearCliente(rs);
                }
                return null;
            }
        );
    }
    
    public boolean atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, documento = ?, telefone = ?, email = ?, endereco = ? " +
                     "WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getDocumento());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
            stmt.setInt(6, cliente.getId());
        });
        
        return linhasAfetadas > 0;
    }
    
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> stmt.setInt(1, id));
        
        return linhasAfetadas > 0;
    }
    
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setDocumento(rs.getString("documento"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setEmail(rs.getString("email"));
        cliente.setEndereco(rs.getString("endereco"));
        return cliente;
    }
}