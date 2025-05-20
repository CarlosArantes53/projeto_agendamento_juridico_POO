package gui.advogado;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;

import dao.ExcecaoDisponibilidadeDAO;
import gui.MainFrame;
import gui.common.FormPanel;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import gui.common.TableActionListener;
import gui.util.TableActionCellEditor;
import gui.util.TableActionCellRenderer;
import gui.util.UIConstants;
import modelo.ExcecaoDisponibilidade;
import modelo.Usuario;

public class ExcecaoDisponibilidadePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private Usuario usuarioLogado;
    private ExcecaoDisponibilidadeDAO excecaoDAO;
    private JTable tblExcecoes;
    private DefaultTableModel tableModel;
    private JFormattedTextField txtDataInicio;
    private JFormattedTextField txtDataFim;
    private JTextField txtMotivo;
    private MainFrame mainFrame;
    
    public ExcecaoDisponibilidadePanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.usuarioLogado = usuario;
        this.excecaoDAO = new ExcecaoDisponibilidadeDAO();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        HeaderPanel headerPanel = new HeaderPanel("Exceções de Disponibilidade");
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JPanel formPanel = criarFormularioExcecao();
        JPanel tabelaPanel = criarTabelaExcecoes();
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tabelaPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        carregarExcecoes();
    }
    
    private JPanel criarFormularioExcecao() {
        JPanel painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBackground(UIConstants.PANEL_BACKGROUND);
        painelForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Adicionar Exceção de Disponibilidade");
        lblTitulo.setForeground(UIConstants.TEXT_COLOR);
        lblTitulo.setFont(UIConstants.SUBTITLE_FONT);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        FormPanel formFields = new FormPanel();
        
        // Usando JFormattedTextField com máscara para data no formato DD/MM/AAAA
        try {
            MaskFormatter dateMask = new MaskFormatter("##/##/####");
            dateMask.setPlaceholderCharacter('_');
            
            txtDataInicio = new JFormattedTextField(dateMask);
            txtDataInicio.setColumns(10);
            txtDataInicio.setFont(UIConstants.LABEL_FONT);
            
            txtDataFim = new JFormattedTextField(dateMask);
            txtDataFim.setColumns(10);
            txtDataFim.setFont(UIConstants.LABEL_FONT);
            
            // Preencher com a data atual formatada
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate hoje = LocalDate.now();
            txtDataInicio.setValue(hoje.format(formatter));
            txtDataFim.setValue(hoje.format(formatter));
            
        } catch (ParseException e) {
            e.printStackTrace();
            txtDataInicio = new JFormattedTextField();
            txtDataFim = new JFormattedTextField();
        }
        
        formFields.addComponent("Data Início:", txtDataInicio);
        formFields.addComponent("Data Fim:", txtDataFim);
        
        txtMotivo = formFields.addTextField("Motivo:", null);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JButton btnAdicionar = new JButton("Adicionar");
        UIConstants.setupPrimaryButton(btnAdicionar, "Adicionar");
        btnAdicionar.addActionListener(this::adicionarExcecao);
        
        btnPanel.add(btnAdicionar);
        
        JButton btnVoltar = new JButton("Voltar");
        UIConstants.setupSecondaryButton(btnVoltar, "Voltar");
        btnVoltar.addActionListener(e -> voltarParaGerenciamento());
        
        btnPanel.add(btnVoltar);
        
        painelForm.add(lblTitulo);
        painelForm.add(Box.createVerticalStrut(15));
        painelForm.add(formFields);
        painelForm.add(Box.createVerticalStrut(10));
        painelForm.add(btnPanel);
        
        return painelForm;
    }
    
    private JPanel criarTabelaExcecoes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Exceções Cadastradas");
        lblTitulo.setForeground(UIConstants.TEXT_COLOR);
        lblTitulo.setFont(UIConstants.SUBTITLE_FONT);
        
        String[] colunas = {"ID", "Data Início", "Data Fim", "Motivo", ""};
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
        
        tblExcecoes = new JTable(tableModel);
        UIConstants.setupTable(tblExcecoes);
        
        tblExcecoes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        tblExcecoes.getColumnModel().getColumn(0).setMaxWidth(50);
        tblExcecoes.getColumnModel().getColumn(0).setMinWidth(50);
        
        TableActionCellRenderer actionRenderer = new TableActionCellRenderer(false, true, false);
        TableActionCellEditor actionEditor = new TableActionCellEditor(
            TableActionListener.create(
                row -> {}, // Não utilizado
                this::excluirExcecao
            )
        );
        
        tblExcecoes.getColumnModel().getColumn(4).setCellRenderer(actionRenderer);
        tblExcecoes.getColumnModel().getColumn(4).setCellEditor(actionEditor);
        tblExcecoes.getColumnModel().getColumn(4).setMinWidth(70);
        tblExcecoes.getColumnModel().getColumn(4).setMaxWidth(70);
        
        JScrollPane scrollPane = new JScrollPane(tblExcecoes);
        scrollPane.getViewport().setBackground(UIConstants.PANEL_BACKGROUND);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void carregarExcecoes() {
        tableModel.setRowCount(0);
        
        try {
            List<ExcecaoDisponibilidade> excecoes = excecaoDAO.listarPorUsuario(usuarioLogado.getId());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (ExcecaoDisponibilidade exc : excecoes) {
                tableModel.addRow(new Object[] {
                    exc.getId(),
                    exc.getDataInicio().format(formatter),
                    exc.getDataFim().format(formatter),
                    exc.getMotivo(),
                    ""
                });
            }
            
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao carregar exceções: " + e.getMessage());
        }
    }
    
    private void adicionarExcecao(ActionEvent e) {
        String dataInicioStr = txtDataInicio.getText().trim();
        String dataFimStr = txtDataFim.getText().trim();
        String motivo = txtMotivo.getText().trim();
        
        if (dataInicioStr.contains("_") || dataFimStr.contains("_")) {
            FormValidator.mostrarErro(this, "Preencha as datas de início e fim corretamente!");
            return;
        }
        
        if (motivo.isEmpty()) {
            FormValidator.mostrarErro(this, "Informe o motivo da exceção!");
            return;
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataInicio = LocalDate.parse(dataInicioStr, formatter);
            LocalDate dataFim = LocalDate.parse(dataFimStr, formatter);
            
            ExcecaoDisponibilidade excecao = new ExcecaoDisponibilidade(
                    usuarioLogado.getId(), dataInicio, dataFim, motivo);
            
            // Validar datas
            if (!excecaoDAO.verificarDatasFim(excecao)) {
                FormValidator.mostrarErro(this, "A data de fim deve ser igual ou posterior à data de início!");
                return;
            }
            
            // Validar se a data é futura
            if (!excecaoDAO.verificarDataFutura(excecao)) {
                FormValidator.mostrarErro(this, "As datas devem ser futuras!");
                return;
            }
            
            int id = excecaoDAO.inserir(excecao);
            
            if (id > 0) {
                FormValidator.mostrarSucesso(this, "Exceção cadastrada com sucesso!");
                limparCampos();
                carregarExcecoes();
            } else {
                FormValidator.mostrarErro(this, "Erro ao cadastrar exceção!");
            }
            
        } catch (Exception ex) {
            FormValidator.mostrarErro(this, "Erro ao processar datas: " + ex.getMessage());
        }
    }
    
    private void excluirExcecao(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String periodo = tableModel.getValueAt(row, 1) + " a " + tableModel.getValueAt(row, 2);
        String motivo = (String) tableModel.getValueAt(row, 3);
        
        if (FormValidator.confirmar(this,
                "Deseja excluir a exceção de " + periodo + " (" + motivo + ")?",
                "Confirmar Exclusão")) {
            
            try {
                boolean sucesso = excecaoDAO.excluir(id);
                
                if (sucesso) {
                    FormValidator.mostrarSucesso(this, "Exceção excluída com sucesso!");
                    carregarExcecoes();
                } else {
                    FormValidator.mostrarErro(this, "Erro ao excluir exceção!");
                }
            } catch (SQLException ex) {
                FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
            }
        }
    }
    
    private void limparCampos() {
        // Preencher com a data atual formatada
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate hoje = LocalDate.now();
        txtDataInicio.setValue(hoje.format(formatter));
        txtDataFim.setValue(hoje.format(formatter));
        txtMotivo.setText("");
    }
    
    private void voltarParaGerenciamento() {
        mainFrame.mostrarPainelGerenciarDisponibilidade(usuarioLogado);
    }
}