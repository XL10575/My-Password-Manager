package edu.cwru.passwordmanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


public class PasswordModel {
    private ObservableList<Password> passwords = FXCollections.observableArrayList();

    // !!! DO NOT CHANGE - VERY IMPORTANT FOR GRADING !!!
    static private File passwordFile = new File("passwords.txt");

    static private String separator = "\t";

    static private String passwordFilePassword = "";
    static private byte [] passwordFileKey;
    static private byte [] passwordFileSalt;

    // TODO: You can set this to whatever you like to verify that the password the user entered is correct (finished)
    private static String verifyString = "Cleveland";

    // TODO: Tip: Break down each piece into individual methods, for example: generateSalt(), encryptPassword, generateKey(), saveFile, etc ... (finished)
    // TODO: Use these functions above, and it will make it easier! Once you know encryption, decryption, etc works, you just need to tie them in (finished)
    
    /**
     * Generates a cryptographically secure random salt
     */
    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
    
    /**
     * Generates a key using PBKDF2 from password and salt
     */
    private static byte[] generateKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return factory.generateSecret(spec).getEncoded();
    }
    
    /**
     * Encrypts data using AES
     */
    private static String encryptData(String data, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }
    
    /**
     * Decrypts data using AES
     */
    private static String decryptData(String encryptedData, byte[] key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
    
    /**
     * Saves all passwords to file
     */
    private void saveToFile() throws Exception {
        List<String> lines = new ArrayList<>();
        
        // First line: salt and encrypted token
        String saltBase64 = Base64.getEncoder().encodeToString(passwordFileSalt);
        String encryptedToken = encryptData(verifyString, passwordFileKey);
        lines.add(saltBase64 + separator + encryptedToken);
        
        // Subsequent lines: label and encrypted password
        for (Password password : passwords) {
            String encryptedPassword = encryptData(password.getPassword(), passwordFileKey);
            lines.add(password.getLabel() + separator + encryptedPassword);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(passwordFile))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    /**
     * Loads passwords from file
     */
    private void loadFromFile() throws Exception {
        if (!passwordFile.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile))) {
            String firstLine = reader.readLine();
            if (firstLine != null) {
                // Skip the first line (salt and token) - already processed in verifyPassword
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(separator);
                    if (parts.length == 2) {
                        String label = parts[0];
                        String encryptedPassword = parts[1];
                        String decryptedPassword = decryptData(encryptedPassword, passwordFileKey);
                        passwords.add(new Password(label, decryptedPassword));
                    }
                }
            }
        }
    }

    private void loadPasswords() {
        // TODO: Replace with loading passwords from file, you will want to add them to the passwords list defined above (finished)
        // TODO: Tips: Use buffered reader, make sure you split on separator, make sure you decrypt password (finished)
        try {
            if (passwordFileKey != null) {
                loadFromFile();
            }
        } catch (Exception e) {
            System.err.println("Error loading passwords: " + e.getMessage());
        }
    }

    public PasswordModel() {
        loadPasswords();
    }

    static public boolean passwordFileExists() {
        return passwordFile.exists();
    }

    static public void initializePasswordFile(String password) throws IOException {
        // TODO: Use password to create token and save in file with salt (TIP: Save these just like you would save password) (finished)
        try {
            passwordFile.createNewFile();
            passwordFilePassword = password;
            passwordFileSalt = generateSalt();
            passwordFileKey = generateKey(password, passwordFileSalt);
            
            // Create file with salt and encrypted token
            String saltBase64 = Base64.getEncoder().encodeToString(passwordFileSalt);
            String encryptedToken = encryptData(verifyString, passwordFileKey);
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(passwordFile))) {
                writer.write(saltBase64 + separator + encryptedToken);
                writer.newLine();
            }
        } catch (Exception e) {
            throw new IOException("Failed to initialize password file: " + e.getMessage());
        }
    }

    static public boolean verifyPassword(String password) {
        passwordFilePassword = password; // DO NOT CHANGE

        // TODO: Check first line and use salt to verify that you can decrypt the token using the password from the user (finished)
        // TODO: TIP !!! If you get an exception trying to decrypt, that also means they have the wrong passcode, return false! (finished)
        
        try {
            if (!passwordFile.exists()) {
                return false;
            }
            
            try (BufferedReader reader = new BufferedReader(new FileReader(passwordFile))) {
                String firstLine = reader.readLine();
                if (firstLine != null) {
                    String[] parts = firstLine.split(separator);
                    if (parts.length == 2) {
                        String saltBase64 = parts[0];
                        String encryptedToken = parts[1];
                        
                        passwordFileSalt = Base64.getDecoder().decode(saltBase64);
                        passwordFileKey = generateKey(password, passwordFileSalt);
                        
                        String decryptedToken = decryptData(encryptedToken, passwordFileKey);
                        return verifyString.equals(decryptedToken);
                    }
                }
            }
        } catch (Exception e) {
            // If any exception occurs during decryption, password is wrong
            return false;
        }
        
        return false;
    }

    public ObservableList<Password> getPasswords() {
        return passwords;
    }

    public void deletePassword(int index) {
        passwords.remove(index);

        // TODO: Remove it from file (finished)
        try {
            saveToFile();
        } catch (Exception e) {
            System.err.println("Error saving file after delete: " + e.getMessage());
        }
    }

    public void updatePassword(Password password, int index) {
        passwords.set(index, password);

        // TODO: Update the file with the new password information (finished)
        try {
            saveToFile();
        } catch (Exception e) {
            System.err.println("Error saving file after update: " + e.getMessage());
        }
    }

    public void addPassword(Password password) {
        passwords.add(password);

        // TODO: Add the new password to the file (finished)
        try {
            saveToFile();
        } catch (Exception e) {
            System.err.println("Error saving file after add: " + e.getMessage());
        }
    }
}
