/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laptrinhmang.movie.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author New
 */
public class RSAUtil {
    
    public static String Encrypt(String data, PublicKey publicKey) {
        
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            byte encryptOut[] = c.doFinal(data.getBytes());
            String strEncrypt = Base64.getEncoder().encodeToString(encryptOut);
            return  strEncrypt;
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.err.println(e);
        }
        return null;
    }
    public static String Decrypt(String data, PrivateKey privateKey) {
        
        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(data));
            return new String(decryptOut);
        } catch (InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            System.err.println(e);
        }
        return null;
    }
}
