package com.t1908e.utils;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CypherUtil {
    private static HashMap<String, Object> keys = new HashMap<String, Object>();
    //gen key only one time
    public static Map<String, Object> getRSAKeys() throws Exception {
        if(!keys.isEmpty()) {
            return keys;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return  keys;
    }

    // Decrypt using RSA private key
    public static String decryptMessage(String encryptedText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedText)));
    }

    // Encrypt using RSA public key
    public static String encryptMessage(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public static void main(String[] args) {
        try {
            String plainText = "Hello World!";

            // Generate public and private keys using RSA
            Map<String, Object> keys = getRSAKeys();

            PrivateKey privateKey = (PrivateKey) keys.get("private");
            PublicKey publicKey = (PublicKey) keys.get("public");

            String encryptedText = encryptMessage(plainText, publicKey);
            String descryptedText = decryptMessage(encryptedText, privateKey);

            System.out.println("input:" + plainText);
            System.out.println("encrypted:" + encryptedText);
            System.out.println("decrypted:" + descryptedText);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}
