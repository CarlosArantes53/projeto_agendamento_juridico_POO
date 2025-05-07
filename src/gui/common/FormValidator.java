package gui.common;

import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import util.ValidadorUtil;

public class FormValidator {
    
    /**
     * Verifica se todos os campos de texto informados estão preenchidos
     */
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
    
    /**
     * Valida um e-mail usando o ValidadorUtil
     */
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
    
    /**
     * Valida uma senha usando o ValidadorUtil
     */
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
    
    /**
     * Valida se duas senhas coincidem
     */
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
    
    /**
     * Valida um telefone usando o ValidadorUtil
     */
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
    
    /**
     * Valida um documento (CPF ou CNPJ) usando o ValidadorUtil
     */
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
    
    /**
     * Exibe uma mensagem de erro genérica
     */
    public static void mostrarErro(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Exibe uma mensagem de sucesso
     */
    public static void mostrarSucesso(Component parent, String mensagem) {
        JOptionPane.showMessageDialog(parent, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Exibe uma confirmação sim/não
     * @return true se o usuário confirmar, false caso contrário
     */
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