package com.paintspray.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilitário para operações relacionadas a senhas.
 */
public class PasswordUtils {

    private static final String ALGORITHM = "SHA-256";
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Gera um hash da senha com salt.
     * 
     * @param password Senha em texto plano
     * @return Hash da senha com salt
     */
    public static String hashPassword(String password) {
        try {
            // Gera salt aleatório
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);

            // Combina senha com salt
            String saltedPassword = password + Base64.getEncoder().encodeToString(salt);

            // Gera hash
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(saltedPassword.getBytes());

            // Retorna salt + hash em Base64
            return Base64.getEncoder().encodeToString(salt) + ":" +
                    Base64.getEncoder().encodeToString(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash da senha", e);
        }
    }

    /**
     * Valida se a senha corresponde ao hash armazenado.
     * 
     * @param password   Senha em texto plano
     * @param storedHash Hash armazenado (salt:hash)
     * @return true se a senha é válida, false caso contrário
     */
    public static boolean validatePassword(String password, String storedHash) {
        try {
            // Separa salt e hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHashBytes = Base64.getDecoder().decode(parts[1]);

            // Recria hash com a senha fornecida
            String saltedPassword = password + Base64.getEncoder().encodeToString(salt);
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] newHash = digest.digest(saltedPassword.getBytes());

            // Compara os hashes
            return MessageDigest.isEqual(storedHashBytes, newHash);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Valida se a senha atende aos critérios mínimos.
     * 
     * @param password Senha a ser validada
     * @return true se válida, false caso contrário
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Verifica se contém pelo menos uma letra e um número
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            }

            if (hasLetter && hasDigit) {
                break;
            }
        }

        return hasLetter && hasDigit;
    }
}
