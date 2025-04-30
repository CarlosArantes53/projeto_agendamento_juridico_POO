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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.UsuarioDAO;
import modelo.Usuario;

public class LoginForm extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioDAO usuarioDAO;
    
    public void preencherCamposParaTeste(String email, String senha) {
        txtEmail.setText(email);
        txtSenha.setText(senha);
    }
    
    public LoginForm() {
        usuarioDAO = new UsuarioDAO();
        
        setTitle("HERMES - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel lblTitulo = new JLabel("HERMES - Agendamentos jurídicos");
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        painelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        painelPrincipal.add(new JLabel("E-mail:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        painelPrincipal.add(txtEmail, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelPrincipal.add(new JLabel("Senha:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSenha = new JPasswordField(20);
        painelPrincipal.add(txtSenha, gbc);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnLogin = new JButton("Entrar");
        JButton btnCadastrar = new JButton("Criar Nova Conta");
        
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarLogin();
            }
        });
        
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirTelaCadastro();
            }
        });
        
        painelBotoes.add(btnLogin);
        painelBotoes.add(btnCadastrar);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painelPrincipal, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
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
                abrirTelaHome(usuario);
                dispose();
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
    
    private void abrirTelaCadastro() {
        setVisible(false);
        
        new CadastroUsuarioForm().setVisible(true);
    }
    
    private void abrirTelaHome(Usuario usuario) {
        new HomeForm(usuario).setVisible(true);
    }
}