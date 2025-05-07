package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dao.UsuarioDAO;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;
import util.UIConstants;
import util.Validador;

public class CadastroUsuarioPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JTextField txtTelefone;
    private JRadioButton rdbAdvogado;
    private JRadioButton rdbSecretario;
    private UsuarioDAO usuarioDAO;
    private MainFrame mainFrame;
    
    public CadastroUsuarioPanel(MainFrame mainFrame) {
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
        
        // Title
        JLabel lblTitulo = new JLabel();
        UIConstants.setupSubtitleLabel(lblTitulo, "Cadastro de novo usuário");
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
        
        // Email label
        JLabel lblEmail = new JLabel();
        UIConstants.setupLabel(lblEmail, "E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblEmail, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        painelPrincipal.add(txtEmail, gbc);
        
        // Senha label
        JLabel lblSenha = new JLabel();
        UIConstants.setupLabel(lblSenha, "Senha:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblSenha, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSenha = new JPasswordField(20);
        painelPrincipal.add(txtSenha, gbc);
        
        // Telefone label
        JLabel lblTelefone = new JLabel();
        UIConstants.setupLabel(lblTelefone, "Telefone:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblTelefone, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTelefone = new JTextField(20);
        painelPrincipal.add(txtTelefone, gbc);
        
        // Tipo de usuário label
        JLabel lblTipoUsuario = new JLabel();
        UIConstants.setupLabel(lblTipoUsuario, "Tipo de Usuário:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblTipoUsuario, gbc);
        
        gbc.gridx = 1;
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTipo.setBackground(UIConstants.BACKGROUND_COLOR);
        
        rdbAdvogado = new JRadioButton();
        UIConstants.setupRadioButton(rdbAdvogado, "Advogado");
        
        rdbSecretario = new JRadioButton();
        UIConstants.setupRadioButton(rdbSecretario, "Secretário");
        
        rdbAdvogado.setSelected(true);
        
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rdbAdvogado);
        grupoTipo.add(rdbSecretario);
        
        painelTipo.add(rdbAdvogado);
        painelTipo.add(rdbSecretario);
        painelPrincipal.add(painelTipo, gbc);
        
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
                cadastrarUsuario();
            }
        });
        
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
                mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
            }
        });
        
        painelBotoes.add(btnCadastrar);
        painelBotoes.add(btnVoltar);
        
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
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
                mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
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
    }
}