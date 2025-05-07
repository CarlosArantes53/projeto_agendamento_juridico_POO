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
        
        // Formulário principal
        FormPanel formPanel = new FormPanel();
        
        // Título
        formPanel.addTitle("Recuperação de Senha");
        
        // Campos de texto
        txtEmail = formPanel.addTextField("E-mail:", null);
        txtNovaSenha = formPanel.addPasswordField("Nova Senha:");
        txtConfirmarSenha = formPanel.addPasswordField("Confirmar Senha:");
        
        // Painel de botões
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
        // Validar campos obrigatórios
        if (!FormValidator.camposObrigatorios(this, txtEmail, txtNovaSenha, txtConfirmarSenha)) {
            return;
        }
        
        // Validar formato de email
        if (!FormValidator.validarEmail(this, txtEmail)) {
            return;
        }
        
        // Validar senhas iguais
        if (!FormValidator.validarSenhasIguais(this, txtNovaSenha, txtConfirmarSenha)) {
            return;
        }
        
        // Validar formato de senha
        if (!FormValidator.validarSenha(this, txtNovaSenha)) {
            return;
        }
        
        try {
            // Verificar se email existe
            if (!usuarioDAO.emailExiste(email)) {
                FormValidator.mostrarErro(this, "E-mail não encontrado no sistema!");
                return;
            }
            
            // Atualizar senha
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