package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import dao.base.BaseDAO;
import modelo.TipoAtendimento;

public class TipoAtendimentoDAO extends BaseDAO {
    
    public int inserir(TipoAtendimento tipo) throws SQLException {
        String sql = "INSERT INTO tipos_atendimento (nome, duracao_padrao, cor) " +
                     "VALUES (?, ?, ?)";
        
        return executeInsert(sql, stmt -> {
            stmt.setString(1, tipo.getNome());
            stmt.setInt(2, tipo.getDuracaoPadrao());
            stmt.setString(3, tipo.getCor());
        });
    }
    
    public List<TipoAtendimento> listarTodos() throws SQLException {
        String sql = "SELECT * FROM tipos_atendimento ORDER BY nome";
        
        return executeQuery(sql, 
            null,
            rs -> mapList(rs, t -> {
                try {
                    return mapearTipoAtendimento(t);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public TipoAtendimento buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM tipos_atendimento WHERE id = ?";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, id),
            rs -> {
                if (rs.next()) {
                    return mapearTipoAtendimento(rs);
                }
                return null;
            }
        );
    }
    
    public boolean atualizar(TipoAtendimento tipo) throws SQLException {
        String sql = "UPDATE tipos_atendimento SET nome = ?, duracao_padrao = ?, cor = ? " +
                     "WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> {
            stmt.setString(1, tipo.getNome());
            stmt.setInt(2, tipo.getDuracaoPadrao());
            stmt.setString(3, tipo.getCor());
            stmt.setInt(4, tipo.getId());
        });
        
        return linhasAfetadas > 0;
    }
    
    public boolean excluir(int id) throws SQLException {
        // Verificar se o tipo está sendo usado em algum agendamento
        String sqlVerificar = "SELECT COUNT(*) FROM agendamentos WHERE id_tipo_atendimento = ?";
        
        boolean emUso = executeQuery(sqlVerificar, 
            stmt -> stmt.setInt(1, id),
            rs -> {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        );
        
        if (emUso) {
            return false;
        }
        
        String sql = "DELETE FROM tipos_atendimento WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> stmt.setInt(1, id));
        
        return linhasAfetadas > 0;
    }
    
    private TipoAtendimento mapearTipoAtendimento(ResultSet rs) throws SQLException {
        TipoAtendimento tipo = new TipoAtendimento();
        tipo.setId(rs.getInt("id"));
        tipo.setNome(rs.getString("nome"));
        tipo.setDuracaoPadrao(rs.getInt("duracao_padrao"));
        tipo.setCor(rs.getString("cor"));
        return tipo;
    }
}