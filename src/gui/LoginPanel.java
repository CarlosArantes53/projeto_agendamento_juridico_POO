package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.UsuarioDAO;
import modelo.Usuario;
import util.UIConstants;

public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioDAO usuarioDAO;
    private MainFrame mainFrame;
    
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        usuarioDAO = new UsuarioDAO();
        
        // Set background color
        setBackground(UIConstants.BACKGROUND_COLOR);
        
        setLayout(new BorderLayout());
        
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(UIConstants.PANEL_PADDING);
        painelPrincipal.setBackground(UIConstants.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Logo
        JLabel lblLogo = new JLabel();
        try {
            ImageIcon imageIcon = new ImageIcon("src\\gui\\images\\hermes_logo.png");
            // Optional: resize the logo if needed
            Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(image));
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem: " + e.getMessage());
            lblLogo.setText("HERMES");
            lblLogo.setForeground(UIConstants.TEXT_COLOR);
            lblLogo.setFont(UIConstants.TITLE_FONT);
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 20, 5);
        painelPrincipal.add(lblLogo, gbc);
        
        // Title
        JLabel lblTitulo = new JLabel();
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        UIConstants.setupSubtitleLabel(lblTitulo, "HERMES - Agendamentos jurídicos");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        painelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Email label
        JLabel lblEmail = new JLabel();
        UIConstants.setupLabel(lblEmail, "E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblEmail, gbc);
        
        // Email input
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        painelPrincipal.add(txtEmail, gbc);
        
        // Password label
        JLabel lblSenha = new JLabel();
        UIConstants.setupLabel(lblSenha, "Senha:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblSenha, gbc);
        
        // Password input
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSenha = new JPasswordField(20);
        painelPrincipal.add(txtSenha, gbc);
        
        // Buttons panel
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton btnLogin = new JButton();
        UIConstants.setupPrimaryButton(btnLogin, "Entrar");
        
        JButton btnCadastrar = new JButton();
        UIConstants.setupSecondaryButton(btnCadastrar, "Criar Nova Conta");
        
        JButton btnEsqueciSenha = new JButton();
        UIConstants.setupWarningButton(btnEsqueciSenha, "Esqueci minha senha");
        
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPainel(MainFrame.CADASTRO_PANEL);
            }
        });
        
        btnEsqueciSenha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.mostrarPainel(MainFrame.RECUPERAR_SENHA_PANEL);
            }
        });
        
        painelBotoes.add(btnLogin);
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnEsqueciSenha);
        
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    public void preencherCamposParaTeste(String email, String senha) {
        txtEmail.setText(email);
        txtSenha.setText(senha);
    }
    
    private void realizarLogin() {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        
        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Informe o e-mail e a senha para continuar!", 
                "Campos em branco", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            
            if (usuario != null) {
                limparCampos();
                mainFrame.mostrarPainelHome(usuario);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "E-mail ou senha incorretos!", 
                    "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao acessar o banco de dados: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        txtEmail.setText("");
        txtSenha.setText("");
    }
}