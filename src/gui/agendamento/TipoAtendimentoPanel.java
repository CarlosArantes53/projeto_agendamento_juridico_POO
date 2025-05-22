package gui.agendamento;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import dao.TipoAtendimentoDAO;
import gui.MainFrame;
import gui.common.FormPanel;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import gui.common.TableActionListener;
import gui.util.TableActionCellEditor;
import gui.util.TableActionCellRenderer;
import gui.util.UIConstants;
import modelo.TipoAtendimento;
import modelo.Usuario;

public class TipoAtendimentoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private TipoAtendimentoDAO tipoAtendimentoDAO;
    private JTable tblTipos;
    private DefaultTableModel tableModel;
    private JTextField txtNome;
    private JSpinner spnDuracao;
    private JButton btnCor;
    private Color corSelecionada = Color.BLUE;
    private MainFrame mainFrame;
    
    public TipoAtendimentoPanel(MainFrame mainFrame, Usuario usuario) {
        this.mainFrame = mainFrame;
        this.tipoAtendimentoDAO = new TipoAtendimentoDAO();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        HeaderPanel headerPanel = new HeaderPanel("Tipos de Atendimento");
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JPanel formPanel = criarFormularioTipo();
        JPanel tabelaPanel = criarTabelaTipos();
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tabelaPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        carregarTipos();
    }
    
    private JPanel criarFormularioTipo() {
        JPanel painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        painelForm.setBackground(UIConstants.PANEL_BACKGROUND);
        painelForm.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel lblTitulo = new JLabel("Adicionar Tipo de Atendimento");
        lblTitulo.setForeground(UIConstants.TEXT_COLOR);
        lblTitulo.setFont(UIConstants.SUBTITLE_FONT);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        FormPanel formFields = new FormPanel();
        
        txtNome = formFields.addTextField("Nome:", null);
        
        spnDuracao = new JSpinner(new SpinnerNumberModel(60, 15, 480, 15));
        formFields.addComponent("Duração (minutos):", spnDuracao);
        
        JPanel corPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        corPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        btnCor = new JButton();
        btnCor.setPreferredSize(new java.awt.Dimension(30, 30));
        btnCor.setBackground(corSelecionada);
        btnCor.addActionListener(e -> selecionarCor());
        
        JButton btnEscolherCor = new JButton("Escolher Cor");
        UIConstants.setupSecondaryButton(btnEscolherCor, "Escolher Cor");
        btnEscolherCor.addActionListener(e -> selecionarCor());
        
        corPanel.add(btnCor);
        corPanel.add(btnEscolherCor);
        
        formFields.addComponent("Cor:", corPanel);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JButton btnAdicionar = new JButton("Adicionar");
        UIConstants.setupPrimaryButton(btnAdicionar, "Adicionar");
        btnAdicionar.addActionListener(this::adicionarTipo);
        
        JButton btnVoltar = new JButton("Voltar");
        UIConstants.setupSecondaryButton(btnVoltar, "Voltar");
        btnVoltar.addActionListener(e -> mainFrame.voltarParaHome());
        
        btnPanel.add(btnAdicionar);
        btnPanel.add(btnVoltar);
        
        painelForm.add(lblTitulo);
        painelForm.add(Box.createVerticalStrut(15));
        painelForm.add(formFields);
        painelForm.add(Box.createVerticalStrut(10));
        painelForm.add(btnPanel);
        
        return painelForm;
    }
    
    private JPanel criarTabelaTipos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Tipos de Atendimento Cadastrados");
        lblTitulo.setForeground(UIConstants.TEXT_COLOR);
        lblTitulo.setFont(UIConstants.SUBTITLE_FONT);
        
        String[] colunas = {"ID", "Nome", "Duração (min)", "Cor", ""};
        tableModel = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 2) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        tblTipos = new JTable(tableModel);
        UIConstants.setupTable(tblTipos);
        
        tblTipos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, 
                    int row, int column) {
                
                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                
                if (column == 0 || column == 2) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 3) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                    
                    String colorHex = (String) value;
                    try {
                        Color color = Color.decode(colorHex);
                        setBackground(color);
                        setForeground(getContrastColor(color));
                    } catch (NumberFormatException e) {
                        setBackground(UIConstants.PANEL_BACKGROUND);
                        setForeground(UIConstants.TEXT_COLOR);
                    }
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                if (isSelected) {
                    if (column != 3) {
                        setBackground(UIConstants.ROW_SELECTED);
                        setForeground(Color.WHITE);
                    }
                } else if (column != 3) {
                    if (row % 2 == 0) {
                        setBackground(UIConstants.ROW_EVEN);
                    } else {
                        setBackground(UIConstants.ROW_ODD);
                    }
                    setForeground(UIConstants.TEXT_COLOR);
                }
                
                return c;
            }
        });
        
        // Configurar coluna de ações
        tblTipos.getColumnModel().getColumn(0).setMaxWidth(50);
        tblTipos.getColumnModel().getColumn(0).setMinWidth(50);
        
        tblTipos.getColumnModel().getColumn(2).setMaxWidth(100);
        tblTipos.getColumnModel().getColumn(2).setMinWidth(100);
        
        tblTipos.getColumnModel().getColumn(3).setMaxWidth(80);
        tblTipos.getColumnModel().getColumn(3).setMinWidth(80);
        
        TableActionCellRenderer actionRenderer = new TableActionCellRenderer(false, true, false);
        TableActionCellEditor actionEditor = new TableActionCellEditor(
            TableActionListener.create(
                row -> {}, // Não utilizado
                this::excluirTipo
            )
        );
        
        tblTipos.getColumnModel().getColumn(4).setCellRenderer(actionRenderer);
        tblTipos.getColumnModel().getColumn(4).setCellEditor(actionEditor);
        tblTipos.getColumnModel().getColumn(4).setMinWidth(70);
        tblTipos.getColumnModel().getColumn(4).setMaxWidth(70);
        
        JScrollPane scrollPane = new JScrollPane(tblTipos);
        scrollPane.getViewport().setBackground(UIConstants.PANEL_BACKGROUND);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void carregarTipos() {
        tableModel.setRowCount(0);
        
        try {
            List<TipoAtendimento> tipos = tipoAtendimentoDAO.listarTodos();
            
            for (TipoAtendimento tipo : tipos) {
                tableModel.addRow(new Object[] {
                    tipo.getId(),
                    tipo.getNome(),
                    tipo.getDuracaoPadrao(),
                    tipo.getCor(),
                    ""
                });
            }
            
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao carregar tipos de atendimento: " + e.getMessage());
        }
    }
    
    private void selecionarCor() {
        Color novaCor = JColorChooser.showDialog(
                this, "Escolha uma cor", corSelecionada);
        
        if (novaCor != null) {
            corSelecionada = novaCor;
            btnCor.setBackground(corSelecionada);
        }
    }
    
    private String corParaHexString(Color cor) {
        return String.format("#%02x%02x%02x", cor.getRed(), cor.getGreen(), cor.getBlue());
    }
    
    private Color getContrastColor(Color cor) {
        double luminance = 0.299 * cor.getRed() + 0.587 * cor.getGreen() + 0.114 * cor.getBlue();
        return luminance > 128 ? Color.BLACK : Color.WHITE;
    }
    
    private void adicionarTipo(ActionEvent e) {
        String nome = txtNome.getText().trim();
        int duracao = (Integer) spnDuracao.getValue();
        String corHex = corParaHexString(corSelecionada);
        
        if (nome.isEmpty()) {
            FormValidator.mostrarErro(this, "Informe um nome para o tipo de atendimento!");
            return;
        }
        
        TipoAtendimento tipo = new TipoAtendimento(nome, duracao, corHex);
        
        try {
            int id = tipoAtendimentoDAO.inserir(tipo);
            
            if (id > 0) {
                FormValidator.mostrarSucesso(this, "Tipo de atendimento cadastrado com sucesso!");
                limparCampos();
                carregarTipos();
            } else {
                FormValidator.mostrarErro(this, "Erro ao cadastrar tipo de atendimento!");
            }
            
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
    
    private void excluirTipo(int row) {
        int id = (int) tableModel.getValueAt(row, 0);
        String nome = (String) tableModel.getValueAt(row, 1);
        
        if (FormValidator.confirmar(this,
                "Deseja excluir o tipo de atendimento '" + nome + "'?\n" +
                "Isso não afetará agendamentos existentes, mas impedirá a criação " +
                "de novos agendamentos com este tipo.",
                "Confirmar Exclusão")) {
            
            try {
                boolean sucesso = tipoAtendimentoDAO.excluir(id);
                
                if (sucesso) {
                    FormValidator.mostrarSucesso(this, "Tipo de atendimento excluído com sucesso!");
                    carregarTipos();
                } else {
                    FormValidator.mostrarErro(this, 
                            "Não é possível excluir este tipo de atendimento porque " +
                            "ele está sendo usado em agendamentos existentes.");
                }
            } catch (SQLException ex) {
                FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
            }
        }
    }
    
    private void limparCampos() {
        txtNome.setText("");
        spnDuracao.setValue(60);
        corSelecionada = Color.BLUE;
        btnCor.setBackground(corSelecionada);
    }
}