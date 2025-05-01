package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;
import util.Validador;

public class CadastroUsuarioForm extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final Color BACKGROUND_COLOR = new Color(18,18,19);
    private static final Color TEXT_COLOR = Color.WHITE;
    
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JTextField txtTelefone;
    private JRadioButton rdbAdvogado;
    private JRadioButton rdbSecretario;
    private UsuarioDAO usuarioDAO;
    
    public CadastroUsuarioForm() {
        
        usuarioDAO = new UsuarioDAO();
        
        setTitle("Cadastro de Usuário");
        setSize(480, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        painelPrincipal.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel lblTitulo = new JLabel("Cadastro de novo usuário");
        lblTitulo.setForeground(TEXT_COLOR);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        painelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel lblNome = new JLabel("Nome Completo:");
        lblNome.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelPrincipal.add(lblNome, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNome = new JTextField(20);
        painelPrincipal.add(txtNome, gbc);
        
        JLabel lblEmail = new JLabel("E-mail:");
        lblEmail.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        painelPrincipal.add(txtEmail, gbc);
        
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblSenha, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSenha = new JPasswordField(20);
        painelPrincipal.add(txtSenha, gbc);
        
        JLabel lblTelefone = new JLabel("Telefone:");
        lblTelefone.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblTelefone, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTelefone = new JTextField(20);
        painelPrincipal.add(txtTelefone, gbc);
        
        JLabel lblTipoUsuario = new JLabel("Tipo de Usuário:");
        lblTipoUsuario.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblTipoUsuario, gbc);
        
        gbc.gridx = 1;
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTipo.setBackground(BACKGROUND_COLOR);
        
        rdbAdvogado = new JRadioButton("Advogado");
        rdbAdvogado.setForeground(TEXT_COLOR);
        rdbAdvogado.setBackground(BACKGROUND_COLOR);
        
        rdbSecretario = new JRadioButton("Secretário");
        rdbSecretario.setForeground(TEXT_COLOR);
        rdbSecretario.setBackground(BACKGROUND_COLOR);
        
        rdbAdvogado.setSelected(true);
        
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rdbAdvogado);
        grupoTipo.add(rdbSecretario);
        
        painelTipo.add(rdbAdvogado);
        painelTipo.add(rdbSecretario);
        painelPrincipal.add(painelTipo, gbc);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoes.setBackground(BACKGROUND_COLOR);
        
        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.setBackground(new Color(0, 150, 136));
        btnCadastrar.setForeground(Color.BLACK);
        btnCadastrar.setFocusPainted(false);
        
        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBackground(new Color(211, 47, 47));
        btnLimpar.setForeground(Color.BLACK);
        btnLimpar.setFocusPainted(false);
        
        btnCadastrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarUsuario();
            }
        });
        
        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnLimpar);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(painelPrincipal, BorderLayout.CENTER);
        getContentPane().add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void cadastrarUsuario() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String telefone = txtTelefone.getText().trim();
        TipoUsuario tipoUsuario = rdbAdvogado.isSelected() ? TipoUsuario.ADVOGADO : TipoUsuario.SECRETARIO;
        
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || telefone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarEmail(email)) {
            JOptionPane.showMessageDialog(this, "Formato de e-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarSenha(senha)) {
            JOptionPane.showMessageDialog(this, 
                "A senha deve ter pelo menos 8 caracteres, incluindo letras, números e caracteres especiais.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarTelefone(telefone)) {
            JOptionPane.showMessageDialog(this, "Formato de telefone inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (usuarioDAO.emailExiste(email)) {
                JOptionPane.showMessageDialog(this, "Este e-mail já está cadastrado no sistema!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Usuario usuario = new Usuario(nome, email, senha, telefone, tipoUsuario);
            
            int idGerado = usuarioDAO.inserir(usuario);
            
            if (idGerado > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Usuário criado, e-mail de confirmação enviado", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                limparCampos();
                
                dispose();
                new LoginForm().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário!", 
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
        txtEmail.setText("");
        txtSenha.setText("");
        txtTelefone.setText("");
        rdbAdvogado.setSelected(true);
        txtNome.requestFocus();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CadastroUsuarioForm().setVisible(true);
            }
        });
    }
}