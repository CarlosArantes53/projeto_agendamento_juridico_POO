package dao.base;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import config.DatabaseConfig;

public abstract class BaseDAO {
    
    protected Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection();
    }
    
    protected void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar ResultSet: " + e.getMessage());
        }
        
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Erro ao fechar Statement: " + e.getMessage());
        }
        
        DatabaseConfig.closeConnection(conn);
    }
    
    protected <T> T executeQuery(String sql, PreparedStatementSetter setter, 
                              ResultSetMapper<T> mapper) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            if (setter != null) {
                setter.setParameters(stmt);
            }
            
            rs = stmt.executeQuery();
            
            return mapper.map(rs);
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
    
    protected int executeUpdate(String sql, PreparedStatementSetter setter) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            
            if (setter != null) {
                setter.setParameters(stmt);
            }
            
            return stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    protected int executeInsert(String sql, PreparedStatementSetter setter) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int idGerado = -1;
        
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            if (setter != null) {
                setter.setParameters(stmt);
            }
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                }
            }
            
            return idGerado;
        } finally {
            closeResources(conn, stmt, rs);
        }
    }
     
    @FunctionalInterface
    protected interface PreparedStatementSetter {
        void setParameters(PreparedStatement stmt) throws SQLException;
    }
    
    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
    
    protected <T> List<T> mapList(ResultSet rs, Function<ResultSet, T> mapper) throws SQLException {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapper.apply(rs));
        }
        return list;
    }
}