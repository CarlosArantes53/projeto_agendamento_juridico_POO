package gui.agendamento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import dao.AgendamentoDAO;
import gui.MainFrame;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import gui.util.UIConstants;
import modelo.Agendamento;
import modelo.Usuario;

public class DetalhesAgendamentoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Agendamento agendamento;
    private AgendamentoDAO agendamentoDAO;
    private MainFrame mainFrame;
    private JComboBox<Agendamento.Status> cbStatus;
    
    public DetalhesAgendamentoPanel(MainFrame mainFrame, Usuario usuario, Agendamento agendamento) {
        this.mainFrame = mainFrame;
        this.agendamento = agendamento;
        this.agendamentoDAO = new AgendamentoDAO();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        HeaderPanel headerPanel = new HeaderPanel("Detalhes do Agendamento");
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JPanel dadosPanel = criarPainelDados();
        JPanel acoesPanel = criarPainelAcoes();
        
        contentPanel.add(dadosPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        contentPanel.add(acoesPanel);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelDados() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIConstants.ROW_EVEN);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode(agendamento.getCor()), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        
        JLabel lblTitulo = new JLabel(agendamento.getNomeTipoAtendimento());
        lblTitulo.setForeground(UIConstants.HIGHLIGHT_TEXT_COLOR);
        lblTitulo.setFont(UIConstants.TITLE_FONT);
        lblTitulo.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel lblData = criarLabel("Data:", agendamento.getDataAtendimento().format(dateFormatter));
        JLabel lblHorario = criarLabel("Horário:", 
                agendamento.getHoraInicio().format(timeFormatter) + " - " + 
                agendamento.getHoraFim().format(timeFormatter));
        JLabel lblDuracao = criarLabel("Duração:", agendamento.getDuracao() + " minutos");
        JLabel lblCliente = criarLabel("Cliente:", agendamento.getNomeCliente());
        JLabel lblAdvogado = criarLabel("Advogado:", agendamento.getNomeAdvogado());
        JLabel lblStatus = criarLabel("Status Atual:", agendamento.getStatus().toString());
        
        
        JLabel lblDescricao = new JLabel("Descrição/Observações:");
        lblDescricao.setForeground(UIConstants.TEXT_COLOR);
        lblDescricao.setFont(UIConstants.LABEL_FONT);
        lblDescricao.setAlignmentX(LEFT_ALIGNMENT);
        
        JTextArea txtDescricao = new JTextArea(agendamento.getDescricao());
        txtDescricao.setFont(UIConstants.LABEL_FONT);
        txtDescricao.setBackground(UIConstants.PANEL_BACKGROUND);
        txtDescricao.setForeground(UIConstants.TEXT_COLOR);
        txtDescricao.setLineWrap(true);
        txtDescricao.setWrapStyleWord(true);
        txtDescricao.setEditable(false);
        txtDescricao.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        txtDescricao.setRows(5);
        
        JScrollPane scrollPane = new JScrollPane(txtDescricao);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR));
        scrollPane.setAlignmentX(LEFT_ALIGNMENT);
        
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblData);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblHorario);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblDuracao);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblCliente);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblAdvogado);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblStatus);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblDescricao);
        panel.add(Box.createVerticalStrut(5));
        panel.add(scrollPane);
        
        return panel;
    }
    
    private JLabel criarLabel(String titulo, String valor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(UIConstants.ROW_EVEN);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(UIConstants.TEXT_SECONDARY);
        lblTitulo.setFont(UIConstants.LABEL_FONT);
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setForeground(UIConstants.TEXT_COLOR);
        lblValor.setFont(UIConstants.LABEL_FONT);
        
        panel.add(lblTitulo);
        panel.add(lblValor);
        
        return lblValor;
    }
    
    private JPanel criarPainelAcoes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR),
                "Gerenciar Agendamento",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP,
                UIConstants.SUBTITLE_FONT,
                UIConstants.TEXT_COLOR
        ));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        statusPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel lblNovoStatus = new JLabel("Alterar Status:");
        lblNovoStatus.setForeground(UIConstants.TEXT_COLOR);
        lblNovoStatus.setFont(UIConstants.LABEL_FONT);
        
        cbStatus = new JComboBox<>(Agendamento.Status.values());
        cbStatus.setSelectedItem(agendamento.getStatus());
        
        statusPanel.add(lblNovoStatus);
        statusPanel.add(cbStatus);
        
        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botoesPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        botoesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JButton btnSalvar = new JButton("Salvar Alterações");
        UIConstants.setupPrimaryButton(btnSalvar, "Salvar Alterações");
        btnSalvar.addActionListener(this::salvarAlteracoes);
        
        JButton btnVoltar = new JButton("Voltar");
        UIConstants.setupSecondaryButton(btnVoltar, "Voltar");
        btnVoltar.addActionListener(e -> mainFrame.mostrarPainelCalendario());
        
        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnVoltar);
        
        panel.add(statusPanel, BorderLayout.WEST);
        panel.add(botoesPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void salvarAlteracoes(ActionEvent e) {
        Agendamento.Status novoStatus = (Agendamento.Status) cbStatus.getSelectedItem();
        
        if (novoStatus == agendamento.getStatus()) {
            FormValidator.mostrarErro(this, "Nenhuma alteração foi feita no status!");
            return;
        }
        
        try {
            boolean sucesso = agendamentoDAO.atualizarStatus(agendamento.getId(), novoStatus);
            
            if (sucesso) {
                FormValidator.mostrarSucesso(this, "Status do agendamento atualizado com sucesso!");
                mainFrame.mostrarPainelCalendario();
            } else {
                FormValidator.mostrarErro(this, "Erro ao atualizar o status do agendamento!");
            }
            
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
}