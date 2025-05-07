package gui.cliente;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import dao.ClienteDAO;
import gui.MainFrame;
import gui.common.FormValidator;
import gui.common.HeaderPanel;
import gui.common.TableActionListener;
import gui.util.IconManager;
import gui.util.TableActionCellEditor;
import gui.util.TableActionCellRenderer;
import gui.util.UIConstants;
import modelo.Cliente;

public class ClientesListPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private MainFrame mainFrame;
    private ClienteDAO clienteDAO;
    private JTable tblClientes;
    private DefaultTableModel tableModel;
    
    public ClientesListPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.clienteDAO = new ClienteDAO();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(UIConstants.PANEL_BACKGROUND);
         
        HeaderPanel headerPanel = new HeaderPanel("Cadastro de clientes");
        headerPanel.addButton("Adicionar", IconManager.ICON_ADD, e -> mainFrame.mostrarPainelCadastroCliente());
        
        JPanel contentPanel = createContentPanel();
         
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        String[] colunas = {"ID", "Nome", "Documento", "Telefone", "E-mail", ""};
        tableModel = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        tblClientes = new JTable(tableModel);
        UIConstants.setupTable(tblClientes);
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tblClientes.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    c.setBackground(UIConstants.ROW_SELECTED);
                    c.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(UIConstants.ROW_EVEN);
                    } else {
                        c.setBackground(UIConstants.ROW_ODD);
                    }
                    c.setForeground(Color.WHITE);
                }
                
                if (column == 0) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 5) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                    c.setBackground(new Color(50, 50, 80));
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                
                return c;
            }
        });
        
        TableColumn idColumn = tblClientes.getColumnModel().getColumn(0);
        idColumn.setMaxWidth(60);
        idColumn.setPreferredWidth(60);
        
        TableColumn actionColumn = tblClientes.getColumnModel().getColumn(5);
        actionColumn.setMinWidth(120);
        actionColumn.setPreferredWidth(120);
        actionColumn.setMaxWidth(120);
        
        actionColumn.setCellRenderer(new TableActionCellRenderer());
        actionColumn.setCellEditor(new TableActionCellEditor(new TableActionListener() {
            @Override
            public void onEdit(int row) {
                editarCliente(row);
            }
            
            @Override
            public void onDelete(int row) {
                excluirCliente(row);
            }
        }));
        
        JScrollPane scrollPane = new JScrollPane(tblClientes);
        scrollPane.getViewport().setBackground(UIConstants.PANEL_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void carregarClientes() {
        tableModel.setRowCount(0);
        
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            
            for (Cliente cliente : clientes) {
                tableModel.addRow(new Object[] {
                    cliente.getId(),
                    cliente.getNome(),
                    cliente.getDocumento(),
                    cliente.getTelefone(),
                    cliente.getEmail(),
                    ""
                });
            }
            
            if (clientes.isEmpty()) {
                mostrarEstadoVazio();
            }
            
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao carregar clientes: " + e.getMessage());
        }
    }
    
    private void mostrarEstadoVazio() {
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
        emptyPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        
        JLabel lblEmpty = new JLabel("Nenhum cliente cadastrado");
        lblEmpty.setAlignmentX(CENTER_ALIGNMENT);
        lblEmpty.setForeground(UIConstants.TEXT_SECONDARY);
        lblEmpty.setFont(UIConstants.SUBTITLE_FONT);
        
        JLabel lblSuggestion = new JLabel("Clique em 'Adicionar Cliente' para começar");
        lblSuggestion.setAlignmentX(CENTER_ALIGNMENT);
        lblSuggestion.setForeground(UIConstants.TEXT_SECONDARY);
        lblSuggestion.setFont(UIConstants.LABEL_FONT);
        
        emptyPanel.add(Box.createVerticalGlue());
        emptyPanel.add(lblEmpty);
        emptyPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        emptyPanel.add(lblSuggestion);
        emptyPanel.add(Box.createVerticalGlue());
        
        removeAll();
        
        HeaderPanel headerPanel = new HeaderPanel("Clientes Cadastrados");
        headerPanel.addButton("Adicionar Cliente", IconManager.ICON_ADD, e -> mainFrame.mostrarPainelCadastroCliente());
        
        add(headerPanel, BorderLayout.NORTH);
        add(emptyPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
    
    private void editarCliente(int row) {
        int idCliente = (int) tableModel.getValueAt(row, 0);
        
        try {
            Cliente cliente = clienteDAO.buscarPorId(idCliente);
            if (cliente != null) {
                mainFrame.mostrarPainelEditarCliente(cliente);
            }
        } catch (SQLException e) {
            FormValidator.mostrarErro(this, "Erro ao buscar cliente: " + e.getMessage());
        }
    }
    
    private void excluirCliente(int row) {
        int idCliente = (int) tableModel.getValueAt(row, 0);
        String nomeCliente = (String) tableModel.getValueAt(row, 1);
        
        if (FormValidator.confirmar(this,
                "Tem certeza que deseja excluir o cliente '" + nomeCliente + "'?",
                "Confirmar Exclusão")) {
            
            try {
                boolean sucesso = clienteDAO.excluir(idCliente);
                
                if (sucesso) {
                    tableModel.removeRow(row);
                    FormValidator.mostrarSucesso(this, "Cliente excluído com sucesso!");
                    
                    if (tableModel.getRowCount() == 0) {
                        carregarClientes();
                    }
                } else {
                    FormValidator.mostrarErro(this, "Erro ao excluir cliente!");
                }
            } catch (SQLException e) {
                FormValidator.mostrarErro(this, "Erro ao excluir cliente: " + e.getMessage());
            }
        }
    }
}