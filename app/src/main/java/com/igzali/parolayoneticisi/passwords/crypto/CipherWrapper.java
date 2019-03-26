package com.igzali.parolayoneticisi.passwords.crypto;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CipherWrapper {

    private final static String TRANSFORMATION_ASYMMETRIC = "RSA/ECB/PKCS1Padding";
    private Cipher mCipher;
    private String TAG = CipherWrapper.class.getSimpleName();

    public CipherWrapper() {
        try {
            mCipher = Cipher.getInstance(TRANSFORMATION_ASYMMETRIC);
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String data, Key key) {
        byte[] bytes = null;
        try {
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            bytes = mCipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public synchronized String decrypt(String data, Key key) {
        byte[] decodedData = null;
        try {
            mCipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedData = Base64.decode(data, Base64.DEFAULT);
            decodedData = mCipher.doFinal(encryptedData);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return new String(decodedData, StandardCharsets.UTF_8);
    }

}
