package org.dino.util;

import java.security.SecureRandom;
import java.util.UUID;

public class QRCodeIdentifierGenerator {
    
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();
    
    /**
     * Gera um identificador para QR Code com as especificações:
     * - Entre 26 e 35 posições
     * - Contém letras minúsculas (a-z), maiúsculas (A-Z) e números (0-9)
     * 
     * @return String com o identificador gerado
     */
    public static String generateQRCodeIdentifier() {
        // Gera um tamanho aleatório entre 26 e 35
        int length = 26 + random.nextInt(10); // 26 + (0 a 9) = 26 a 35
        
        StringBuilder identifier = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            identifier.append(CHARACTERS.charAt(randomIndex));
        }
        
        return identifier.toString();
    }
    
    /**
     * Alternativa usando UUID como base (sempre 32 caracteres)
     * Remove os hífens do UUID e garante que contenha apenas caracteres válidos
     * 
     * @return String com o identificador baseado em UUID
     */
    public static String generateQRCodeIdentifierFromUUID() {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid.toLowerCase(); // 32 caracteres, apenas letras minúsculas e números
    }
    
    /**
     * Versão que permite especificar o tamanho desejado
     * 
     * @param length Tamanho do identificador (deve estar entre 26 e 35)
     * @return String com o identificador gerado
     * @throws IllegalArgumentException se o tamanho não estiver no intervalo válido
     */
    public static String generateQRCodeIdentifier(int length) {
        if (length < 26 || length > 35) {
            throw new IllegalArgumentException("O tamanho deve estar entre 26 e 35 caracteres");
        }
        
        StringBuilder identifier = new StringBuilder(length);
        
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            identifier.append(CHARACTERS.charAt(randomIndex));
        }
        
        return identifier.toString();
    }
    
    /**
     * Valida se um identificador atende às especificações
     * 
     * @param identifier Identificador a ser validado
     * @return true se válido, false caso contrário
     */
    public static boolean isValidIdentifier(String identifier) {
        if (identifier == null || identifier.length() < 26 || identifier.length() > 35) {
            return false;
        }
        
        // Verifica se contém apenas caracteres válidos
        return identifier.matches("[a-zA-Z0-9]+");
    }
    
    // Exemplo de uso
    public static void main(String[] args) {
        // Método 1: Tamanho aleatório entre 26 e 35
        System.out.println("Identificador aleatório: " + generateQRCodeIdentifier());
        
        // Método 2: Baseado em UUID (sempre 32 caracteres)
        System.out.println("Identificador UUID: " + generateQRCodeIdentifierFromUUID());
        
        // Método 3: Tamanho específico
        System.out.println("Identificador 30 chars: " + generateQRCodeIdentifier(30));
        
        // Teste de validação
        String testId = generateQRCodeIdentifier();
        System.out.println("Identificador válido: " + isValidIdentifier(testId));
        System.out.println("Tamanho: " + testId.length());
    }
}