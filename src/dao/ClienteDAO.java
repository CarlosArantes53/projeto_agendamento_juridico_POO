package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Cliente;

public class ClienteDAO {
 
    public int inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nome, documento, telefone, email, endereco) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idGerado = -1;
        
        try {
            conn = ConexaoDB.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getDocumento());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    cliente.setId(idGerado);
                }
            }
            
            return idGerado;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            ConexaoDB.closeConnection(conn);
        }
    }
    
    public boolean documentoExiste(String documento) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE documento = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoDB.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, documento);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
            return false;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            ConexaoDB.closeConnection(conn);
        }
    }
    
    public List<Cliente> listarTodos() throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY nome";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoDB.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            List<Cliente> clientes = new ArrayList<>();
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setDocumento(rs.getString("documento"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                cliente.setEndereco(rs.getString("endereco"));
                
                clientes.add(cliente);
            }
            
            return clientes;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            ConexaoDB.closeConnection(conn);
        }
    }
    
    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoDB.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNome(rs.getString("nome"));
                cliente.setDocumento(rs.getString("documento"));
                cliente.setTelefone(rs.getString("telefone"));
                cliente.setEmail(rs.getString("email"));
                cliente.setEndereco(rs.getString("endereco"));
                
                return cliente;
            }
            
            return null;
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            ConexaoDB.closeConnection(conn);
        }
    }
    
    public boolean atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nome = ?, documento = ?, telefone = ?, email = ?, endereco = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexaoDB.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getDocumento());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getEndereco());
            stmt.setInt(6, cliente.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } finally {
            if (stmt != null) stmt.close();
            ConexaoDB.closeConnection(conn);
        }
    }
    
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexaoDB.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        } finally {
            if (stmt != null) stmt.close();
            ConexaoDB.closeConnection(conn);
        }
    }
}