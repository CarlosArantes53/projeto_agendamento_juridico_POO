package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validador {
    
    // Validar formato de email
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        // Padrão para validação de email
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }
    
    // Validar senha (min 8 caracteres, letras, números e caracteres especiais)
    public static boolean validarSenha(String senha) {
        if (senha == null || senha.length() < 8) {
            return false;
        }
        
        boolean temLetra = false;
        boolean temNumero = false;
        boolean temEspecial = false;
        
        for (char c : senha.toCharArray()) {
            if (Character.isLetter(c)) {
                temLetra = true;
            } else if (Character.isDigit(c)) {
                temNumero = true;
            } else {
                temEspecial = true;
            }
        }
        
        return temLetra && temNumero && temEspecial;
    }
    
    // Validar telefone (formato simples)
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String numerosApenas = telefone.replaceAll("[^0-9]", "");
        
        // Verifica se tem pelo menos 8 dígitos (número básico)
        return numerosApenas.length() >= 8;
    }
}
