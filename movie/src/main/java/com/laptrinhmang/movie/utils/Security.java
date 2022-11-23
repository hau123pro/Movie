
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.laptrinhmang.movie.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author New
 */
public class Security {
    
    private SecretKeySpec skeySpec = null;
    private IvParameterSpec iv = null;
    public Security(String key){   
        skeySpec = new SecretKeySpec(key.getBytes(), "TripleDES");
        iv = new IvParameterSpec("Security".getBytes());
    }
    public String Encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,iv);
            byte[] byteEncrypted = cipher.doFinal(data.getBytes());
            String strEncrypt = Base64.getEncoder().encodeToString(byteEncrypted);
            return strEncrypt;
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
            System.err.println(e);
        }
        return null;
    }
    public String Decrypt(String data){
         try {
            Cipher cipher = Cipher.getInstance("TripleDES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] byteDecrypted = cipher.doFinal(Base64.getDecoder().decode(data));
            return new String(byteDecrypted);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException e) {
             System.err.println(e);
        }
        return null;
    }
}
    