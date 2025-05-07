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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.UsuarioDAO;
import util.UIConstants;
import util.Validador;

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
        
        // Set background color
        UIConstants.setupPanel(this);
        
        setLayout(new BorderLayout());
        
        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBorder(UIConstants.PANEL_PADDING);
        painelPrincipal.setBackground(UIConstants.BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel lblTitulo = new JLabel();
        UIConstants.setupSubtitleLabel(lblTitulo, "Recuperação de Senha");
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        painelPrincipal.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Email label
        JLabel lblEmail = new JLabel();
        UIConstants.setupLabel(lblEmail, "E-mail:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblEmail, gbc);
        
        // Email input
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        painelPrincipal.add(txtEmail, gbc);
        
        // Nova Senha label
        JLabel lblNovaSenha = new JLabel();
        UIConstants.setupLabel(lblNovaSenha, "Nova Senha:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblNovaSenha, gbc);
        
        // Nova Senha input
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtNovaSenha = new JPasswordField(20);
        painelPrincipal.add(txtNovaSenha, gbc);
        
        // Confirmar Senha label
        JLabel lblConfirmarSenha = new JLabel();
        UIConstants.setupLabel(lblConfirmarSenha, "Confirmar Senha:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        painelPrincipal.add(lblConfirmarSenha, gbc);
        
        // Confirmar Senha input
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtConfirmarSenha = new JPasswordField(20);
        painelPrincipal.add(txtConfirmarSenha, gbc);
        
        // Buttons panel
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelBotoes.setBackground(UIConstants.BACKGROUND_COLOR);
        
        JButton btnAtualizar = new JButton();
        UIConstants.setupPrimaryButton(btnAtualizar, "Atualizar Senha");
        
        JButton btnVoltar = new JButton();
        UIConstants.setupDangerButton(btnVoltar, "Voltar");
        
        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarSenha();
            }
        });
        
        btnVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
                mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
            }
        });
        
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnVoltar);
        
        add(painelPrincipal, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }
    
    private void atualizarSenha() {
        String email = txtEmail.getText().trim();
        String novaSenha = new String(txtNovaSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());
        
        if (email.isEmpty() || novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Todos os campos são obrigatórios!", 
                "Campos em branco", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!Validador.validarEmail(email)) {
            JOptionPane.showMessageDialog(this, "Formato de e-mail inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!novaSenha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, 
                "As senhas não coincidem!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!Validador.validarSenha(novaSenha)) {
            JOptionPane.showMessageDialog(this, 
                "A senha deve ter pelo menos 8 caracteres, incluindo letras, números e caracteres especiais.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            if (!usuarioDAO.emailExiste(email)) {
                JOptionPane.showMessageDialog(this, 
                    "E-mail não encontrado no sistema!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean sucesso = usuarioDAO.atualizarSenha(email, novaSenha);
            
            if (sucesso) {
                JOptionPane.showMessageDialog(this, 
                    "Senha atualizada com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                limparCampos();
                mainFrame.mostrarPainel(MainFrame.LOGIN_PANEL);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao atualizar a senha!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao acessar o banco de dados: " + ex.getMessage(), 
                "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limparCampos() {
        txtEmail.setText("");
        txtNovaSenha.setText("");
        txtConfirmarSenha.setText("");
    }
}