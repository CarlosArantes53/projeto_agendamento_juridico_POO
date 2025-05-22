package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import dao.base.BaseDAO;
import modelo.DisponibilidadeAdvogado;

public class DisponibilidadeAdvogadoDAO extends BaseDAO {
    
    public int inserir(DisponibilidadeAdvogado disponibilidade) throws SQLException {
        String sql = "INSERT INTO disponibilidade_advogado (id_usuario, dia_semana, horario_inicio, horario_fim) " +
                     "VALUES (?, ?, ?, ?)";
        
        return executeInsert(sql, stmt -> {
            stmt.setInt(1, disponibilidade.getIdUsuario());
            stmt.setInt(2, disponibilidade.getDiaSemana());
            stmt.setTime(3, Time.valueOf(disponibilidade.getHorarioInicio()));
            stmt.setTime(4, Time.valueOf(disponibilidade.getHorarioFim()));
        });
    }
    
    public List<DisponibilidadeAdvogado> listarPorUsuario(int idUsuario) throws SQLException {
        String sql = "SELECT * FROM disponibilidade_advogado WHERE id_usuario = ? ORDER BY dia_semana, horario_inicio";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, idUsuario),
            rs -> mapList(rs, t -> {
                try {
                    return mapearDisponibilidade(t);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public DisponibilidadeAdvogado buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM disponibilidade_advogado WHERE id = ?";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, id),
            rs -> {
                if (rs.next()) {
                    return mapearDisponibilidade(rs);
                }
                return null;
            }
        );
    }
    
    public boolean atualizar(DisponibilidadeAdvogado disponibilidade) throws SQLException {
        String sql = "UPDATE disponibilidade_advogado SET dia_semana = ?, horario_inicio = ?, horario_fim = ? " +
                     "WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> {
            stmt.setInt(1, disponibilidade.getDiaSemana());
            stmt.setTime(2, Time.valueOf(disponibilidade.getHorarioInicio()));
            stmt.setTime(3, Time.valueOf(disponibilidade.getHorarioFim()));
            stmt.setInt(4, disponibilidade.getId());
        });
        
        return linhasAfetadas > 0;
    }
    
    public boolean excluir(int id) throws SQLException {
        String sql = "DELETE FROM disponibilidade_advogado WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> stmt.setInt(1, id));
        
        return linhasAfetadas > 0;
    }
    
    public boolean excluirPorUsuario(int idUsuario) throws SQLException {
        String sql = "DELETE FROM disponibilidade_advogado WHERE id_usuario = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> stmt.setInt(1, idUsuario));
        
        return linhasAfetadas > 0;
    }
    
    public boolean verificarSobreposicao(DisponibilidadeAdvogado disponibilidade) throws SQLException {
        String sql = "SELECT COUNT(*) FROM disponibilidade_advogado " +
                    "WHERE id_usuario = ? AND dia_semana = ? " +
                    "AND ((horario_inicio <= ? AND horario_fim > ?) OR " +
                    "     (horario_inicio < ? AND horario_fim >= ?) OR " +
                    "     (horario_inicio >= ? AND horario_fim <= ?))";
        
        if (disponibilidade.getId() > 0) {
            sql += " AND id != ?";
        }
        
        Time horaInicio = Time.valueOf(disponibilidade.getHorarioInicio());
        Time horaFim = Time.valueOf(disponibilidade.getHorarioFim());
        
        return executeQuery(sql, 
            stmt -> {
                stmt.setInt(1, disponibilidade.getIdUsuario());
                stmt.setInt(2, disponibilidade.getDiaSemana());
                stmt.setTime(3, horaInicio);
                stmt.setTime(4, horaInicio);
                stmt.setTime(5, horaFim);
                stmt.setTime(6, horaFim);
                stmt.setTime(7, horaInicio);
                stmt.setTime(8, horaFim);
                
                if (disponibilidade.getId() > 0) {
                    stmt.setInt(9, disponibilidade.getId());
                }
            },
            rs -> {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        );
    }
    
    private DisponibilidadeAdvogado mapearDisponibilidade(ResultSet rs) throws SQLException {
        DisponibilidadeAdvogado disponibilidade = new DisponibilidadeAdvogado();
        disponibilidade.setId(rs.getInt("id"));
        disponibilidade.setIdUsuario(rs.getInt("id_usuario"));
        disponibilidade.setDiaSemana(rs.getInt("dia_semana"));
        disponibilidade.setHorarioInicio(rs.getTime("horario_inicio").toLocalTime());
        disponibilidade.setHorarioFim(rs.getTime("horario_fim").toLocalTime());
        return disponibilidade;
    }
}