package gui.usuario;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.UsuarioDAO;
import gui.MainFrame;
import gui.common.ActionPanel;
import gui.common.FormPanel;
import gui.common.FormValidator;
import gui.util.UIConstants;

public class RecuperarSenhaPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtEmail;
    private JPasswordField txtNovaSenha;
    private JPasswordField txtConfirmarSenha;
    private UsuarioDAO usuarioDAO;
    private MainFrame mainFrame;
    
    public RecuperarSenhaPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        usuarioDAO = new UsuarioDAO();
        
        UIConstants.setupPanel(this);
        setLayout(new BorderLayout());
        
        FormPanel formPanel = new FormPanel();
        
        formPanel.addTitle("Recuperação de Senha");
        
        txtEmail = formPanel.addTextField("E-mail:", null);
        txtNovaSenha = formPanel.addPasswordField("Nova Senha:");
        txtConfirmarSenha = formPanel.addPasswordField("Confirmar Senha:");
        
        ActionPanel actionPanel = new ActionPanel();
        
        actionPanel.addPrimaryButton("Atualizar Senha", this::atualizarSenha);
        actionPanel.addDangerButton("Voltar", e -> {
            limparCampos();
            mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
        });
        
        add(formPanel, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);
    }
    
    private void atualizarSenha(ActionEvent e) {
        String email = txtEmail.getText().trim();
        String novaSenha = new String(txtNovaSenha.getPassword());
        if (!FormValidator.camposObrigatorios(this, txtEmail, txtNovaSenha, txtConfirmarSenha)) {
            return;
        }
        
        if (!FormValidator.validarEmail(this, txtEmail)) {
            return;
        }
        
        if (!FormValidator.validarSenhasIguais(this, txtNovaSenha, txtConfirmarSenha)) {
            return;
        }
        
        if (!FormValidator.validarSenha(this, txtNovaSenha)) {
            return;
        }
        
        try {
            if (!usuarioDAO.emailExiste(email)) {
                FormValidator.mostrarErro(this, "E-mail não encontrado no sistema!");
                return;
            }
            
            boolean sucesso = usuarioDAO.atualizarSenha(email, novaSenha);
            
            if (sucesso) {
                FormValidator.mostrarSucesso(this, "Senha atualizada com sucesso!");
                limparCampos();
                mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
            } else {
                FormValidator.mostrarErro(this, "Erro ao atualizar a senha!");
            }
        } catch (SQLException ex) {
            FormValidator.mostrarErro(this, "Erro ao acessar o banco de dados: " + ex.getMessage());
        }
    }
    
    private void limparCampos() {
        txtEmail.setText("");
        txtNovaSenha.setText("");
        txtConfirmarSenha.setText("");
    }
}