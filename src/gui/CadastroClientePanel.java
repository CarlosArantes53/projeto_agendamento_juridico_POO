package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import dao.ClienteDAO;
import modelo.Cliente;
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
        
        // Set background color
        setBackground(UIConstants.BACKGROUND_COLOR);
        
        setLayout(new BorderLayout());
        
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(UIConstants.PANEL_PADDING);
        painelPrincipal.setBackground(UIConstants.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel lblTitulo = new JLabel();
        UIConstants.setupSubtitleLabel(lblTitulo, "Cadastro de Cliente");
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        painelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Nome label
        JLabel lblNome = new JLabel();
        UIConstants.setupLabel(lblNome, "Nome Completo:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPrincipal.add(lblNome, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNome = new JTextField(20);
        painelPrincipal.add(txtNome, gbc);
        
        // Documento label
        JLabel lblDocumento = new JLabel();
        UIConstants.setupLabel(lblDocumento, "CPF/CNPJ:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblDocumento, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtDocumento = new JTextField(20);
        painelPrincipal.add(txtDocumento, gbc);
        
        // Telefone label
        JLabel lblTelefone = new JLabel();
        UIConstants.setupLabel(lblTelefone, "Telefone:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblTelefone, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTelefone = new JTextField(20);
        painelPrincipal.add(txtTelefone, gbc);
        
        // Email label
        JLabel lblEmail = new JLabel();
        UIConstants.setupLabel(lblEmail, "E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        painelPrincipal.add(txtEmail, gbc);
        
        // Endereço label
        JLabel lblEndereco = new JLabel();
        UIConstants.setupLabel(lblEndereco, "Endereço Completo:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblEndereco, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEndereco = new JTextArea(4, 20);
        txtEndereco.setLineWrap(true);
        txtEndereco.setWrapStyleWord(true);
        JScrollPane scrollEndereco = new JScrollPane(txtEndereco);
        painelPrincipal.add(scrollEndereco, gbc);
        
        // Buttons panel
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton btnCadastrar = new JButton();
        UIConstants.setupPrimaryButton(btnCadastrar, "Cadastrar");
        
        JButton btnVoltar = new JButton();
        UIConstants.setupDangerButton(btnVoltar, "Voltar");
        
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
                mainFrame.mostrarPainel(MainFrame.HOME_PANEL);
            }
        });
        
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnVoltar);
        
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
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
                mainFrame.mostrarPainel(MainFrame.HOME_PANEL);
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