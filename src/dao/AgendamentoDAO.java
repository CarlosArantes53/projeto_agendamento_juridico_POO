package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import dao.base.BaseDAO;
import modelo.Agendamento;
import modelo.Agendamento.Status;

public class AgendamentoDAO extends BaseDAO {
    
    public Resultado inserir(Agendamento agendamento) throws SQLException {
        Connection conn = null;
        CallableStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareCall("{CALL criar_agendamento(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            
            stmt.setDate(1, Date.valueOf(agendamento.getDataAtendimento()));
            stmt.setTime(2, Time.valueOf(agendamento.getHoraInicio()));
            stmt.setInt(3, agendamento.getDuracao());
            stmt.setInt(4, agendamento.getIdCliente());
            stmt.setInt(5, agendamento.getIdAdvogado());
            stmt.setInt(6, agendamento.getIdTipoAtendimento());
            stmt.setString(7, agendamento.getDescricao());
            stmt.setInt(8, agendamento.getIdUsuarioCriador());
            stmt.registerOutParameter(9, Types.INTEGER);
            stmt.registerOutParameter(10, Types.VARCHAR);
            
            stmt.execute();
            
            int idAgendamento = stmt.getInt(9);
            String mensagem = stmt.getString(10);
            
            return new Resultado(idAgendamento, mensagem, idAgendamento > 0);
            
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    public List<Agendamento> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        String sql = "SELECT a.*, c.nome as nome_cliente, u.nome as nome_advogado, " +
                    "t.nome as tipo_atendimento, t.cor " +
                    "FROM agendamentos a " +
                    "JOIN clientes c ON a.id_cliente = c.id " +
                    "JOIN usuarios u ON a.id_advogado = u.id " +
                    "JOIN tipos_atendimento t ON a.id_tipo_atendimento = t.id " +
                    "WHERE a.data_atendimento BETWEEN ? AND ? " +
                    "ORDER BY a.data_atendimento, a.hora_inicio";
        
        return executeQuery(sql, 
            stmt -> {
                stmt.setDate(1, Date.valueOf(dataInicio));
                stmt.setDate(2, Date.valueOf(dataFim));
            },
            rs -> mapList(rs, t -> {
                try {
                    return mapearAgendamento(t);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public List<Agendamento> listarPorAdvogado(int idAdvogado, LocalDate dataInicio, LocalDate dataFim) throws SQLException {
        String sql = "SELECT a.*, c.nome as nome_cliente, u.nome as nome_advogado, " +
                    "t.nome as tipo_atendimento, t.cor " +
                    "FROM agendamentos a " +
                    "JOIN clientes c ON a.id_cliente = c.id " +
                    "JOIN usuarios u ON a.id_advogado = u.id " +
                    "JOIN tipos_atendimento t ON a.id_tipo_atendimento = t.id " +
                    "WHERE a.id_advogado = ? AND a.data_atendimento BETWEEN ? AND ? " +
                    "ORDER BY a.data_atendimento, a.hora_inicio";
        
        return executeQuery(sql, 
            stmt -> {
                stmt.setInt(1, idAdvogado);
                stmt.setDate(2, Date.valueOf(dataInicio));
                stmt.setDate(3, Date.valueOf(dataFim));
            },
            rs -> mapList(rs, t -> {
                try {
                    return mapearAgendamento(t);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public List<Agendamento> listarPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT a.*, c.nome as nome_cliente, u.nome as nome_advogado, " +
                    "t.nome as tipo_atendimento, t.cor " +
                    "FROM agendamentos a " +
                    "JOIN clientes c ON a.id_cliente = c.id " +
                    "JOIN usuarios u ON a.id_advogado = u.id " +
                    "JOIN tipos_atendimento t ON a.id_tipo_atendimento = t.id " +
                    "WHERE a.id_cliente = ? " +
                    "ORDER BY a.data_atendimento DESC, a.hora_inicio";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, idCliente),
            rs -> mapList(rs, t -> {
                try {
                    return mapearAgendamento(t);
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            })
        );
    }
    
    public Agendamento buscarPorId(int id) throws SQLException {
        String sql = "SELECT a.*, c.nome as nome_cliente, u.nome as nome_advogado, " +
                    "t.nome as tipo_atendimento, t.cor " +
                    "FROM agendamentos a " +
                    "JOIN clientes c ON a.id_cliente = c.id " +
                    "JOIN usuarios u ON a.id_advogado = u.id " +
                    "JOIN tipos_atendimento t ON a.id_tipo_atendimento = t.id " +
                    "WHERE a.id = ?";
        
        return executeQuery(sql, 
            stmt -> stmt.setInt(1, id),
            rs -> {
                if (rs.next()) {
                    return mapearAgendamento(rs);
                }
                return null;
            }
        );
    }
    
    public boolean atualizarStatus(int id, Status novoStatus) throws SQLException {
        String sql = "UPDATE agendamentos SET status = ? WHERE id = ?";
        
        int linhasAfetadas = executeUpdate(sql, stmt -> {
            stmt.setString(1, novoStatus.name());
            stmt.setInt(2, id);
        });
        
        return linhasAfetadas > 0;
    }
    
    public boolean verificarDisponibilidade(int idAdvogado, LocalDate data, 
                                           LocalTime horaInicio, LocalTime horaFim) throws SQLException {
        Connection conn = null;
        CallableStatement stmt = null;
        
        try {
            conn = getConnection();
            stmt = conn.prepareCall("{CALL verificar_disponibilidade_advogado(?, ?, ?, ?, ?)}");
            
            stmt.setInt(1, idAdvogado);
            stmt.setDate(2, Date.valueOf(data));
            stmt.setTime(3, Time.valueOf(horaInicio));
            stmt.setTime(4, Time.valueOf(horaFim));
            stmt.registerOutParameter(5, Types.BOOLEAN);
            
            stmt.execute();
            
            return stmt.getBoolean(5);
            
        } finally {
            closeResources(conn, stmt, null);
        }
    }
    
    private Agendamento mapearAgendamento(ResultSet rs) throws SQLException {
        Agendamento agendamento = new Agendamento();
        agendamento.setId(rs.getInt("id"));
        agendamento.setDataAtendimento(rs.getDate("data_atendimento").toLocalDate());
        agendamento.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        agendamento.setDuracao(rs.getInt("duracao"));
        agendamento.setIdCliente(rs.getInt("id_cliente"));
        agendamento.setNomeCliente(rs.getString("nome_cliente"));
        agendamento.setIdAdvogado(rs.getInt("id_advogado"));
        agendamento.setNomeAdvogado(rs.getString("nome_advogado"));
        agendamento.setIdTipoAtendimento(rs.getInt("id_tipo_atendimento"));
        agendamento.setNomeTipoAtendimento(rs.getString("tipo_atendimento"));
        agendamento.setDescricao(rs.getString("descricao"));
        agendamento.setStatus(Status.valueOf(rs.getString("status")));
        agendamento.setIdUsuarioCriador(rs.getInt("id_usuario_criador"));
        agendamento.setCor(rs.getString("cor"));
        return agendamento;
    }
    
    public static class Resultado {
        private int id;
        private String mensagem;
        private boolean sucesso;
        
        public Resultado(int id, String mensagem, boolean sucesso) {
            this.id = id;
            this.mensagem = mensagem;
            this.sucesso = sucesso;
        }
        
        public int getId() {
            return id;
        }
        
        public String getMensagem() {
            return mensagem;
        }
        
        public boolean isSucesso() {
            return sucesso;
        }
    }
}