package gui.common;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import util.ValidadorUtil;

public class FormValidator {
    
    public static boolean camposObrigatorios(Component parent, JTextComponent... campos) {
        for (JTextComponent campo : campos) {
            if (campo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, 
                    "Todos os campos são obrigatórios!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                campo.requestFocus();
                return false;
            }
        }
        return true;
    }
    
    public static boolean validarEmail(Component parent, JTextComponent campoEmail) {
        String email = campoEmail.getText().trim();
        
        if (!ValidadorUtil.validarEmail(email)) {
            JOptionPane.showMessageDialog(parent, 
                "Formato de e-mail inválido!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            campoEmail.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static boolean validarSenha(Component parent, JTextComponent campoSenha) {
        String senha = campoSenha.getText();
        
        if (!ValidadorUtil.validarSenha(senha)) {
            JOptionPane.showMessageDialog(parent, 
                "A senha deve ter pelo menos 8 caracteres, incluindo letras, números e caracteres especiais.", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            campoSenha.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static boolean validarSenhasIguais(Component parent, 
            JTextComponent campoSenha, JTextComponent campoConfirmacao) {
        
        String senha = campoSenha.getText();
        String confirmacao = campoConfirmacao.getText();
        
        if (!senha.equals(confirmacao)) {
            JOptionPane.showMessageDialog(parent, 
                "As senhas não coincidem!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            campoConfirmacao.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static boolean validarTelefone(Component parent, JTextComponent campoTelefone) {
        String telefone = campoTelefone.getText().trim();
        
        if (!ValidadorUtil.validarTelefone(telefone)) {
            JOptionPane.showMessageDialog(parent, 
                "Formato de telefone inválido!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            campoTelefone.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static boolean validarDocumento(Component parent, JTextComponent campoDocumento) {
        String documento = campoDocumento.getText().trim();
        
        if (!ValidadorUtil.validarDocumento(documento)) {
            JOptionPane.showMessageDialog(parent, 
                "CPF/CNPJ inválido!", 
                "Erro", JOptionPane.ERROR_MESSAGE);
            campoDocumento.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static void mostrarErro(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void mostrarSucesso(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static boolean confirmar(Component parent, String mensagem, String titulo) {
        int resposta = JOptionPane.showConfirmDialog(
            parent,
            mensagem,
            titulo,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        return resposta == JOptionPane.YES_OPTION;
    }
}