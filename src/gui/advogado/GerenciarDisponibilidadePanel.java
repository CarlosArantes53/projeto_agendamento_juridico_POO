package gui.advogado;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.DisponibilidadeAdvogadoDAO;
import gui.MainFrame;
import gui.common.FormPanel;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import gui.common.TableActionListener;
import gui.util.TableActionCellEditor;
import gui.util.TableActionCellRenderer;
import gui.util.UIConstants;
import modelo.DisponibilidadeAdvogado;
import modelo.Usuario;

public class GerenciarDisponibilidadePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    private DisponibilidadeAdvogadoDAO disponibilidadeDAO;
    private JTable tblDisponibilidade;
    private DefaultTableModel tableModel;
    private JComboBox<String> cbDiaSemana;
    private JSpinner spnHoraInicio;
    private JSpinner spnHoraFim;
    private MainFrame mainFrame;
    
    private static final String[] DIAS_SEMANA = {
        "Domingo", "Segunda-feira", "Terça-feira", "Quarta-feira", 
        "Quinta-feira", "Sexta-feira", "Sábado"
    };
    
    public GerenciarDisponibilidadePanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuarioLogado = usuario;
        this.disponibilidadeDAO = new DisponibilidadeAdvogadoDAO();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        HeaderPanel headerPanel = new HeaderPanel("Gerenciar Disponibilidade");
        headerPanel.addButton("Exceções", "calendar.png", e -> abrirPainelExcecoes());
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JPanel formPanel = criarFormularioDisponibilidade();
        JPanel tabelaPanel = criarTabelaDisponibilidade();
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tabelaPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        carregarDisponibilidades();
    }
    
    private JPanel criarFormularioDisponibilidade() {
        JPanel painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBackground(UIConstants.PANEL_BACKGROUND);
        painelForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Adicionar Disponibilidade");
        lblTitulo.setForeground(UIConstants.TEXT_COLOR);
        lblTitulo.setFont(UIConstants.SUBTITLE_FONT);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        FormPanel formFields = new FormPanel();
        
        cbDiaSemana = new JComboBox<>(DIAS_SEMANA);
        formFields.addComponent("Dia da Semana:", cbDiaSemana);
        
        SpinnerDateModel modelInicio = new SpinnerDateModel();
        spnHoraInicio = new JSpinner(modelInicio);
        JSpinner.DateEditor editorInicio = new JSpinner.DateEditor(spnHoraInicio, "HH:mm");
        spnHoraInicio.setEditor(editorInicio);
        formFields.addComponent("Horário de Início:", spnHoraInicio);
        
        SpinnerDateModel modelFim = new SpinnerDateModel();
        spnHoraFim = new JSpinner(modelFim);
        JSpinner.DateEditor editorFim = new JSpinner.DateEditor(spnHoraFim, "HH:mm");
        spnHoraFim.setEditor(editorFim);
        formFields.addComponent("Horário de Fim:", spnHoraFim);
        
        // Definir valores padrão para os spinners
        java.util.Calendar calInicio = java.util.Calendar.getInstance();
        calInicio.set(java.util.Calendar.HOUR_OF_DAY, 8);
        calInicio.set(java.util.Calendar.MINUTE, 0);
        spnHoraInicio.setValue(calInicio.getTime());
        
        java.util.Calendar calFim = java.util.Calendar.getInstance();
        calFim.set(java.util.Calendar.HOUR_OF_DAY, 18);
        calFim.set(java.util.Calendar.MINUTE, 0);
        spnHoraFim.setValue(calFim.getTime());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JButton btnAdicionar = new JButton("Adicionar");
        UIConstants.setupPrimaryButton(btnAdicionar, "Adicionar");
        btnAdicionar.addActionListener(this::adicionarDisponibilidade);
        
        btnPanel.add(btnAdicionar);
        
        JButton btnVoltar = new JButton("Voltar");
        UIConstants.setupSecondaryButton(btnVoltar, "Voltar");
        btnVoltar.addActionListener(e -> mainFrame.voltarParaHome());
        
        btnPanel.add(btnVoltar);
        
        painelForm.add(lblTitulo);
        painelForm.add(Box.createVerticalStrut(15));
        painelForm.add(formFields);
        painelForm.add(Box.createVerticalStrut(10));
        painelForm.add(btnPanel);
        
        return painelForm;
    }
    
    private JPanel criarTabelaDisponibilidade() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Disponibilidades Cadastradas");
        lblTitulo.setForeground(UIConstants.TEXT_COLOR);
        lblTitulo.setFont(UIConstants.SUBTITLE_FONT);
        
        String[] colunas = {"ID", "Dia da Semana", "Horário Início", "Horário Fim", ""};
        tableModel = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        tblDisponibilidade = new JTable(tableModel);
        UIConstants.setupTable(tblDisponibilidade);
        
        tblDisponibilidade.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, 
                    int row, int column) {
                
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                if (column == 0) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                return c;
            }
        });
        
        // Configurar coluna de ações
        tblDisponibilidade.getColumnModel().getColumn(0).setMaxWidth(50);
        tblDisponibilidade.getColumnModel().getColumn(0).setMinWidth(50);
        
        TableActionCellRenderer actionRenderer = new TableActionCellRenderer(false, true, false);
        TableActionCellEditor actionEditor = new TableActionCellEditor(
            TableActionListener.create(
                row -> {}, // Não utilizado
                this::excluirDisponibilidade
            )
        );
        
        tblDisponibilidade.getColumnModel().getColumn(4).setCellRenderer(actionRenderer);
        tblDisponibilidade.getColumnModel().getColumn(4).setCellEditor(actionEditor);
        tblDisponibilidade.getColumnModel().getColumn(4).setMinWidth(70);
        tblDisponibilidade.getColumnModel().getColumn(4).setMaxWidth(70);
        
        JScrollPane scrollPane = new JScrollPane(tblDisponibilidade);
        scrollPane.getViewport().setBackground(UIConstants.PANEL_BACKGROUND);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void carregarDisponibilidades() {
        tableModel.setRowCount(0);
        
        try {
            List<DisponibilidadeAdvogado> disponibilidades = disponibilidadeDAO.listarPorUsuario(usuarioLogado.getId());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            
            for (DisponibilidadeAdvogado disp : disponibilidades) {
                tableModel.addRow(new Object[] {
                    disp.getId(),
                    disp.getNomeDiaSemana(),
                    disp.getHorarioInicio().format(formatter),
                    disp.getHorarioFim().format(formatter),
                    ""
                });
            }
            
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao carregar disponibilidades: " + e.getMessage());
        }
    }
    
    private void adicionarDisponibilidade(ActionEvent e) {
        int diaSemana = cbDiaSemana.getSelectedIndex();
        
        java.util.Date horaInicioDate = (java.util.Date) spnHoraInicio.getValue();
        java.util.Date horaFimDate = (java.util.Date) spnHoraFim.getValue();
        
        java.util.Calendar calInicio = java.util.Calendar.getInstance();
        calInicio.setTime(horaInicioDate);
        LocalTime horaInicio = LocalTime.of(
                calInicio.get(java.util.Calendar.HOUR_OF_DAY),
                calInicio.get(java.util.Calendar.MINUTE));
        
        java.util.Calendar calFim = java.util.Calendar.getInstance();
        calFim.setTime(horaFimDate);
        LocalTime horaFim = LocalTime.of(
                calFim.get(java.util.Calendar.HOUR_OF_DAY),
                calFim.get(java.util.Calendar.MINUTE));
        
        // Validar horários
        if (horaInicio.isAfter(horaFim) || horaInicio.equals(horaFim)) {
            FormValidator.mostrarErro(this, "O horário de início deve ser anterior ao horário de fim!");
            return;
        }
        
        DisponibilidadeAdvogado disponibilidade = new DisponibilidadeAdvogado(
                usuarioLogado.getId(), diaSemana, horaInicio, horaFim);
        
        try {
            // Verificar sobreposição
            if (disponibilidadeDAO.verificarSobreposicao(disponibilidade)) {
                FormValidator.mostrarErro(this, 
                        "Já existe disponibilidade cadastrada para este dia e horário!");
                return;
            }
            
            int id = disponibilidadeDAO.inserir(disponibilidade);
            
            if (id > 0) {
                FormValidator.mostrarSucesso(this, "Disponibilidade cadastrada com sucesso!");
                carregarDisponibilidades();
            } else {
                FormValidator.mostrarErro(this, "Erro ao cadastrar disponibilidade!");
            }
            
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
    
    private void excluirDisponibilidade(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String diaSemana = (String) tableModel.getValueAt(row, 1);
        String horario = tableModel.getValueAt(row, 2) + " - " + tableModel.getValueAt(row, 3);
        
        if (FormValidator.confirmar(this,
                "Deseja excluir a disponibilidade para " + diaSemana + " " + horario + "?",
                "Confirmar Exclusão")) {
            
            try {
                boolean sucesso = disponibilidadeDAO.excluir(id);
                
                if (sucesso) {
                    FormValidator.mostrarSucesso(this, "Disponibilidade excluída com sucesso!");
                    carregarDisponibilidades();
                } else {
                    FormValidator.mostrarErro(this, "Erro ao excluir disponibilidade!");
                }
            } catch (SQLException ex) {
                FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
            }
        }
    }
    
    private void abrirPainelExcecoes() {
        mainFrame.mostrarPainelExcecaoDisponibilidade(usuarioLogado);
    }
}