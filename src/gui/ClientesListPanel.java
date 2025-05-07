package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import dao.ClienteDAO;
import modelo.Cliente;
import util.IconManager;
import util.UIConstants;

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
        
        JPanel headerPanel = createHeaderPanel();
        JPanel contentPanel = createContentPanel();
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Title section
        JLabel lblTitle = new JLabel("Clientes Cadastrados");
        lblTitle.setForeground(UIConstants.TEXT_COLOR);
        lblTitle.setFont(UIConstants.SUBTITLE_FONT);
        
        // Button section
        JButton btnAdicionar = new JButton("Adicionar Cliente");
        UIConstants.setupPrimaryButton(btnAdicionar, "Adicionar Cliente");
        
        // Add icon to button if available
        ImageIcon addIcon = IconManager.loadIcon(IconManager.ICON_ADD, 16, 16);
        if (addIcon != null) {
            btnAdicionar.setIcon(addIcon);
        }
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        btnPanel.add(btnAdicionar);
        
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPainelCadastroCliente();
            }
        });
        
        panel.add(lblTitle, BorderLayout.WEST);
        panel.add(btnPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(UIConstants.PANEL_BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        // Create table model
        String[] colunas = {"ID", "Nome", "Documento", "Telefone", "E-mail", "Ações"};
        tableModel = new DefaultTableModel(colunas, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Apenas a coluna de ações é editável
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        
        // Create and configure table
        tblClientes = new JTable(tableModel);
        UIConstants.setupTable(tblClientes);
        tblClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Set custom renderer for all cells
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
                
                // Center align the ID column
                if (column == 0) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else if (column == 5) { // Action column
                    setHorizontalAlignment(SwingConstants.CENTER);
                    c.setBackground(new Color(50, 50, 80));
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }
                
                // Add some padding
                setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
                
                return c;
            }
        });
        
        // Set column widths
        TableColumn idColumn = tblClientes.getColumnModel().getColumn(0);
        idColumn.setMaxWidth(60);
        idColumn.setPreferredWidth(60);
        
        TableColumn actionColumn = tblClientes.getColumnModel().getColumn(5);
        actionColumn.setMinWidth(120);
        actionColumn.setPreferredWidth(120);
        actionColumn.setMaxWidth(120);
        
        // Set action column renderer and editor
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
        
        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(tblClientes);
        scrollPane.getViewport().setBackground(UIConstants.PANEL_BACKGROUND);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Add to panel
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
                // Show empty state
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
                
                // Remove the table and show the empty state
                removeAll();
                add(createHeaderPanel(), BorderLayout.NORTH);
                add(emptyPanel, BorderLayout.CENTER);
                revalidate();
                repaint();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar clientes: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editarCliente(int row) {
        int idCliente = (int) tableModel.getValueAt(row, 0);
        
        try {
            Cliente cliente = clienteDAO.buscarPorId(idCliente);
            if (cliente != null) {
                mainFrame.mostrarPainelEditarCliente(cliente);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar cliente: " + e.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void excluirCliente(int row) {
        int idCliente = (int) tableModel.getValueAt(row, 0);
        String nomeCliente = (String) tableModel.getValueAt(row, 1);
        
        int confirma = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja excluir o cliente '" + nomeCliente + "'?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirma == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = clienteDAO.excluir(idCliente);
                
                if (sucesso) {
                    tableModel.removeRow(row);
                    JOptionPane.showMessageDialog(this, 
                        "Cliente excluído com sucesso!", 
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Check if we need to show the empty state
                    if (tableModel.getRowCount() == 0) {
                        carregarClientes();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Erro ao excluir cliente!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir cliente: " + e.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    interface TableActionListener {
        void onEdit(int row);
        void onDelete(int row);
    }
}