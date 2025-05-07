package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dao.ClienteDAO;
import modelo.Cliente;
import util.IconManager;
import util.UIConstants;
import util.Validador;
import util.ValidadorDocumento;

public class CadastroClientePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JTextField txtDocumento;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextArea txtEndereco;
    private ClienteDAO clienteDAO;
    private MainFrame mainFrame;
    
    public CadastroClientePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        clienteDAO = new ClienteDAO();
        
        setLayout(new BorderLayout());
        setBackground(UIConstants.PANEL_BACKGROUND);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UIConstants.HEADER_BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTitulo = new JLabel("Cadastro de Cliente");
        UIConstants.setupSubtitleLabel(lblTitulo, "Cadastro de Cliente");
        headerPanel.add(lblTitulo, BorderLayout.WEST);
        
        // Main form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.weightx = 1.0;
        
        // Nome 
        JLabel lblNome = new JLabel();
        UIConstants.setupLabel(lblNome, "Nome Completo:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.2;
        formPanel.add(lblNome, gbc);
        
        txtNome = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(txtNome, gbc);
        
        // Documento
        JLabel lblDocumento = new JLabel();
        UIConstants.setupLabel(lblDocumento, "CPF/CNPJ:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.2;
        formPanel.add(lblDocumento, gbc);
        
        txtDocumento = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(txtDocumento, gbc);
        
        // Telefone
        JLabel lblTelefone = new JLabel();
        UIConstants.setupLabel(lblTelefone, "Telefone:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.2;
        formPanel.add(lblTelefone, gbc);
        
        txtTelefone = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(txtTelefone, gbc);
        
        // Email
        JLabel lblEmail = new JLabel();
        UIConstants.setupLabel(lblEmail, "E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.2;
        formPanel.add(lblEmail, gbc);
        
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        formPanel.add(txtEmail, gbc);
        
        // Endereço
        JLabel lblEndereco = new JLabel();
        UIConstants.setupLabel(lblEndereco, "Endereço Completo:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(15, 5, 8, 5);
        formPanel.add(lblEndereco, gbc);
        
        txtEndereco = new JTextArea(4, 20);
        txtEndereco.setLineWrap(true);
        txtEndereco.setWrapStyleWord(true);
        txtEndereco.setFont(UIConstants.LABEL_FONT);
        txtEndereco.setBackground(UIConstants.ROW_EVEN);
        txtEndereco.setForeground(UIConstants.TEXT_COLOR);
        txtEndereco.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollEndereco = new JScrollPane(txtEndereco);
        scrollEndereco.setBorder(BorderFactory.createLineBorder(UIConstants.HEADER_BORDER_COLOR));
        
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollEndereco, gbc);
        
        // Footer with buttons
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        footerPanel.setBackground(UIConstants.PANEL_BACKGROUND);
        footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIConstants.HEADER_BORDER_COLOR));
        
        JButton btnCadastrar = new JButton();
        UIConstants.setupPrimaryButton(btnCadastrar, "Cadastrar");
        
        // Add icon if available
        ImageIcon saveIcon = IconManager.loadIcon("save.png", 16, 16);
        if (saveIcon != null) {
            btnCadastrar.setIcon(saveIcon);
        }
        
        JButton btnVoltar = new JButton();
        UIConstants.setupDangerButton(btnVoltar, "Cancelar");
        
        // Add icon if available
        ImageIcon cancelIcon = IconManager.loadIcon("cancel.png", 16, 16);
        if (cancelIcon != null) {
            btnVoltar.setIcon(cancelIcon);
        }
        
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarCliente();
            }
        });
        
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
                mainFrame.voltarParaHome();
            }
        });
        
        footerPanel.add(btnCadastrar);
        footerPanel.add(btnVoltar);
        
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private void cadastrarCliente() {
        String nome = txtNome.getText().trim();
        String documento = txtDocumento.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        String endereco = txtEndereco.getText().trim();
        
        if (nome.isEmpty() || documento.isEmpty() || telefone.isEmpty() || 
            email.isEmpty() || endereco.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidadorDocumento.validarDocumento(documento)) {
            JOptionPane.showMessageDialog(this, "CPF/CNPJ inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarEmail(email)) {
            JOptionPane.showMessageDialog(this, "Formato de e-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarTelefone(telefone)) {
            JOptionPane.showMessageDialog(this, "Formato de telefone inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (clienteDAO.documentoExiste(documento)) {
                JOptionPane.showMessageDialog(this, "Este CPF/CNPJ já está cadastrado no sistema!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Cliente cliente = new Cliente(nome, documento, telefone, email, endereco);
            
            int idGerado = clienteDAO.inserir(cliente);
            
            if (idGerado > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Cliente cadastrado com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                limparCampos();
                mainFrame.voltarParaHome();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar cliente!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao acessar o banco de dados: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtDocumento.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
    }
}