package dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import dao.base.BaseDAO;
import modelo.ExcecaoDisponibilidade;

public class ExcecaoDisponibilidadeDAO extends BaseDAO {
    
    public int inserir(ExcecaoDisponibilidade excecao) throws SQLException {
        String sql = "INSERT INTO excecao_disponibilidade (id_usuario, data_inicio, data_fim, motivo) " +
                     "VALUES (?, ?, ?, ?)";
        
        return executeInsert(sql, stmt -> {
            stmt.setInt(1, excecao.getIdUsuario());
            stmt.setDate(2, Date.valueOf(excecao.getDataInicio()));
            stmt.setDate(3, Date.valueOf(excecao.getDataFim()));
            stmt.setString(4, excecao.getMotivo());
        });
    }
    
    public List<ExcecaoDisponibilidade> listarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM excecao_disponibilidade WHERE id_usuario = ? ORDER BY data_inicio";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, idUsuario),
            rs -> mapList(rs, t -> {
                try {
                    return mapearExcecao(t);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public List<ExcecaoDisponibilidade> listarExcecoesAtivas(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM excecao_disponibilidade " +
                    "WHERE id_usuario = ? AND data_fim >= CURRENT_DATE " +
                    "ORDER BY data_inicio";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, idUsuario),
            rs -> mapList(rs, t -> {
                try {
                    return mapearExcecao(t);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public ExcecaoDisponibilidade buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM excecao_disponibilidade WHERE id = ?";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, id),
            rs -> {
                if (rs.next()) {
                    return mapearExcecao(rs);
                }
                return null;
            }
        );
    }
    
    public boolean atualizar(ExcecaoDisponibilidade excecao) throws SQLException {
        String sql = "UPDATE excecao_disponibilidade SET data_inicio = ?, data_fim = ?, motivo = ? " +
                     "WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> {
            stmt.setDate(1, Date.valueOf(excecao.getDataInicio()));
            stmt.setDate(2, Date.valueOf(excecao.getDataFim()));
            stmt.setString(3, excecao.getMotivo());
            stmt.setInt(4, excecao.getId());
        });
        
        return linhasAfetadas > 0;
    }
    
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM excecao_disponibilidade WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> stmt.setInt(1, id));
        
        return linhasAfetadas > 0;
    }
    
    public boolean verificarDataFutura(ExcecaoDisponibilidade excecao) {
        LocalDate hoje = LocalDate.now();
        return !excecao.getDataInicio().isBefore(hoje);
    }
    
    public boolean verificarDatasFim(ExcecaoDisponibilidade excecao) {
        return !excecao.getDataFim().isBefore(excecao.getDataInicio());
    }
    
    private ExcecaoDisponibilidade mapearExcecao(ResultSet rs) throws SQLException {
        ExcecaoDisponibilidade excecao = new ExcecaoDisponibilidade();
        excecao.setId(rs.getInt("id"));
        excecao.setIdUsuario(rs.getInt("id_usuario"));
        excecao.setDataInicio(rs.getDate("data_inicio").toLocalDate());
        excecao.setDataFim(rs.getDate("data_fim").toLocalDate());
        excecao.setMotivo(rs.getString("motivo"));
        return excecao;
    }
}