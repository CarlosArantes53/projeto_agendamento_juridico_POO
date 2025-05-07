package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorUtil {
    
    public static boolean validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }
    
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
    
    public static boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return false;
        }
        
        String numerosApenas = telefone.replaceAll("[^0-9]", "");
        
        return numerosApenas.length() >= 8;
    }
    
    public static boolean validarDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String numerosApenas = documento.replaceAll("[^0-9]", "");
        
        // Verifica se é CPF (11 dígitos)
        if (numerosApenas.length() == 11) {
            return validarCPF(numerosApenas);
        } 
        // Verifica se é CNPJ (14 dígitos)
        else if (numerosApenas.length() == 14) {
            return validarCNPJ(numerosApenas);
        }
        
        return false;
    }

    private static boolean validarCPF(String cpf) {
        // Verifica CPFs conhecidos inválidos
        if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
            cpf.equals("22222222222") || cpf.equals("33333333333") ||
            cpf.equals("44444444444") || cpf.equals("55555555555") ||
            cpf.equals("66666666666") || cpf.equals("77777777777") ||
            cpf.equals("88888888888") || cpf.equals("99999999999")) {
            return false;
        }
        
        // Verifica o primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;
        
        if (dv1 != (cpf.charAt(9) - '0')) {
            return false;
        }
        
        // Verifica o segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;
        
        return dv2 == (cpf.charAt(10) - '0');
    }

    private static boolean validarCNPJ(String cnpj) {
        // Verifica CNPJs conhecidos inválidos
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") ||
            cnpj.equals("22222222222222") || cnpj.equals("33333333333333") ||
            cnpj.equals("44444444444444") || cnpj.equals("55555555555555") ||
            cnpj.equals("66666666666666") || cnpj.equals("77777777777777") ||
            cnpj.equals("88888888888888") || cnpj.equals("99999999999999")) {
            return false;
        }
        
        // Verifica o primeiro dígito verificador
        int soma = 0;
        int peso = 2;
        
        for (int i = 11; i >= 0; i--) {
            soma += (cnpj.charAt(i) - '0') * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        
        int resto = soma % 11;
        int dv1 = (resto < 2) ? 0 : 11 - resto;
        
        if (dv1 != (cnpj.charAt(12) - '0')) {
            return false;
        }
        
        // Verifica o segundo dígito verificador
        soma = 0;
        peso = 2;
        
        for (int i = 12; i >= 0; i--) {
            soma += (cnpj.charAt(i) - '0') * peso;
            peso = (peso == 9) ? 2 : peso + 1;
        }
        
        resto = soma % 11;
        int dv2 = (resto < 2) ? 0 : 11 - resto;
        
        return dv2 == (cnpj.charAt(13) - '0');
    }
}