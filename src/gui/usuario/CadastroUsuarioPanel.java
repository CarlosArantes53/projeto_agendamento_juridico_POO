package gui.usuario;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import dao.UsuarioDAO;
import gui.MainFrame;
import gui.common.ActionPanel;
import gui.common.FormPanel;
import gui.common.FormValidator;
import modelo.Usuario;
import modelo.Usuario.TipoUsuario;
import gui.util.UIConstants;

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
        
        setBackground(UIConstants.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        
        FormPanel formPanel = new FormPanel();
        
        
        formPanel.addTitle("Cadastro de novo usuário");
        
        txtNome = formPanel.addTextField("Nome Completo:", null);
        txtEmail = formPanel.addTextField("E-mail:", null);
        txtSenha = formPanel.addPasswordField("Senha:");
        txtTelefone = formPanel.addTextField("Telefone:", null);

        JPanel tipoUsuarioPanel = criarPainelTipoUsuario();
        formPanel.addComponent("Tipo de Usuário:", tipoUsuarioPanel);
        
        ActionPanel actionPanel = new ActionPanel();
        
        actionPanel.addPrimaryButton("Cadastrar", this::cadastrarUsuario);
        actionPanel.addDangerButton("Voltar", e -> {
            limparCampos();
            mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
        });
        
        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JPanel criarPainelTipoUsuario() {
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
        
        return painelTipo;
    }
    
    private void cadastrarUsuario(ActionEvent e) {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        String telefone = txtTelefone.getText().trim();
        TipoUsuario tipoUsuario = rdbAdvogado.isSelected() ? TipoUsuario.ADVOGADO : TipoUsuario.SECRETARIO;
        
        if (!FormValidator.camposObrigatorios(this, txtNome, txtEmail, txtSenha, txtTelefone)) {
            return;
        }
        
        if (!FormValidator.validarEmail(this, txtEmail)) {
            return;
        }
        
        if (!FormValidator.validarSenha(this, txtSenha)) {
            return;
        }
        
        if (!FormValidator.validarTelefone(this, txtTelefone)) {
            return;
        }
        
        try {
            if (usuarioDAO.emailExiste(email)) {
                FormValidator.mostrarErro(this, "Este e-mail já está cadastrado no sistema!");
                return;
            }
            
            Usuario usuario = new Usuario(nome, email, senha, telefone, tipoUsuario);
            
            int idGerado = usuarioDAO.inserir(usuario);
            
            if (idGerado > 0) {
                FormValidator.mostrarSucesso(this, "Usuário criado, e-mail de confirmação enviado");
                
                limparCampos();
                mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
            } else {
                FormValidator.mostrarErro(this, "Erro ao cadastrar usuário!");
            }
            
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
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