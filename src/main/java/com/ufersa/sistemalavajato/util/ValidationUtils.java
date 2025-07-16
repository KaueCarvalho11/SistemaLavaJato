package com.ufersa.sistemalavajato.util;

import java.util.regex.Pattern;

/**
 * Utilitário para validação de dados.
 */
public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\(?\\d{2}\\)?[\\s-]?\\d{4,5}[\\s-]?\\d{4}$");

    /**
     * Valida formato de email.
     * 
     * @param email Email a ser validado
     * @return true se válido, false caso contrário
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Valida formato de telefone brasileiro.
     * 
     * @param phone Telefone a ser validado
     * @return true se válido, false caso contrário
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Valida se o nome é válido.
     * 
     * @param name Nome a ser validado
     * @return true se válido, false caso contrário
     */
    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String trimmedName = name.trim();
        return trimmedName.length() >= 2 && trimmedName.length() <= 100;
    }

    /**
     * Sanitiza string removendo caracteres especiais perigosos.
     * 
     * @param input String a ser sanitizada
     * @return String sanitizada
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        return input.trim()
                .replaceAll("[<>\"'%;()&+]", "")
                .replaceAll("\\s+", " ");
    }

    /**
     * Valida se o endereço é válido.
     * 
     * @param address Endereço a ser validado
     * @return true se válido, false caso contrário
     */
    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            return false;
        }

        String trimmedAddress = address.trim();
        return trimmedAddress.length() >= 5 && trimmedAddress.length() <= 200;
    }

    /**
     * Valida se o ID é válido (não nulo e não vazio).
     * 
     * @param id ID a ser validado
     * @return true se válido, false caso contrário
     */
    public static boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty();
    }
}
