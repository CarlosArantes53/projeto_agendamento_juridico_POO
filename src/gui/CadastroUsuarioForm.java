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
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        painelPrincipal.add(new JLabel("Nome Completo:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNome = new JTextField(20);
        painelPrincipal.add(txtNome, gbc);
        
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
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelPrincipal.add(new JLabel("Telefone:"), gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtTelefone = new JTextField(20);
        painelPrincipal.add(txtTelefone, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        painelPrincipal.add(new JLabel("Tipo de Usuário:"), gbc);
        
        gbc.gridx = 1;
        JPanel painelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rdbAdvogado = new JRadioButton("Advogado");
        rdbSecretario = new JRadioButton("Secretário");
        rdbAdvogado.setSelected(true);
        
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rdbAdvogado);
        grupoTipo.add(rdbSecretario);
        
        painelTipo.add(rdbAdvogado);
        painelTipo.add(rdbSecretario);
        painelPrincipal.add(painelTipo, gbc);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnLimpar = new JButton("Limpar");
        
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
