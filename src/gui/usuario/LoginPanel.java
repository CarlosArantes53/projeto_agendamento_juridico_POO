package gui.usuario;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import dao.UsuarioDAO;
import gui.MainFrame;
import gui.common.ActionPanel;
import gui.common.FormPanel;
import gui.common.FormValidator;
import modelo.Usuario;
import gui.util.IconManager;
import gui.util.UIConstants;

public class LoginPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private UsuarioDAO usuarioDAO;
    private MainFrame mainFrame;
    
    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        usuarioDAO = new UsuarioDAO();
        
        setBackground(UIConstants.BACKGROUND_COLOR);
        setLayout(new BorderLayout());
        
        FormPanel formPanel = new FormPanel();
        
        JLabel lblLogo = createLogoLabel();
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        logoPanel.add(lblLogo);
        
        formPanel.addTitle("HERMES - Agendamentos jurídicos");
        formPanel.addSpacer(15);
        
        txtEmail = formPanel.addTextField("E-mail:", null);
        txtSenha = formPanel.addPasswordField("Senha:");
        
        ActionPanel actionPanel = new ActionPanel();
        
        actionPanel.addPrimaryButton("Entrar", this::realizarLogin);
        actionPanel.addSecondaryButton("Criar Conta", e -> mainFrame.mostrarPainel(MainFrame.CADASTRO_PANEL));
        actionPanel.addWarningButton("Redefinir senha", e -> mainFrame.mostrarPainel(MainFrame.RECUPERAR_SENHA_PANEL));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        mainPanel.add(logoPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private JLabel createLogoLabel() {
        JLabel lblLogo = new JLabel();
        try {
            // Usar o novo método melhorado para carregar o logo
            ImageIcon imageIcon = IconManager.loadLogo("src\\gui\\images\\hermes_logo.png", 150, 150);
            if (imageIcon != null) {
                lblLogo.setIcon(imageIcon);
                lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
            } else {
                throw new Exception("Não foi possível carregar o logo");
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem: " + e.getMessage());
            lblLogo.setText("HERMES");
            lblLogo.setForeground(UIConstants.TEXT_COLOR);
            lblLogo.setFont(UIConstants.TITLE_FONT);
            lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return lblLogo;
    }
    
    public void preencherCamposParaTeste(String email, String senha) {
        txtEmail.setText(email);
        txtSenha.setText(senha);
    }
    
    private void realizarLogin(ActionEvent e) {
        String email = txtEmail.getText().trim();
        String senha = new String(txtSenha.getPassword());
        
        if (email.isEmpty() || senha.isEmpty()) {
            FormValidator.mostrarErro(this, "Informe o e-mail e a senha para continuar!");
            return;
        }
        
        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            
            if (usuario != null) {
                limparCampos();
                mainFrame.mostrarPainelHome(usuario);
            } else {
                FormValidator.mostrarErro(this, "E-mail ou senha incorretos!");
            }
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
    
    private void limparCampos() {
        txtEmail.setText("");
        txtSenha.setText("");
    }
}