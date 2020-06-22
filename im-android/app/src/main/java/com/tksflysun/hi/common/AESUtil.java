package com.tksflysun.hi.common;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidParameterException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESUtil {
    private final static byte key[] = "1234567812345678".getBytes();
    private final static byte[] iv = "1234567812345678".getBytes();
    private final static String transfornation = "";


    /**
     * Base64 decode then AES decrypt
     *
     * @param data Data to decrypt
     * @return Decrypted bytes
     * @throws Exception Decrypt exception
     */
    public static String decrypt(String data) {
        try {
            if (data == null || data.length() == 0
                    || key == null || key.length < 16
                    || iv == null || iv.length < 16
            ) {
                return "";
            }
            byte[] textBytes = Base64.decode(data, Base64.DEFAULT);
            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec newKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            return new String(cipher.doFinal(textBytes));
        } catch (Throwable e) {
            Log.e("解密失败", e.getMessage());
            return "";
        }
    }

    /**
     * AES encrypt then base64 decode
     *
     * @param data Data to encrypt
     * @return Encrypted bytes
     * @throws Exception Encrypt exception
     */
    public static String encrypt(String data) {
        try {
            if (data == null || data.length() == 0
                    || key == null || key.length == 0
                    || iv == null || iv.length == 0) {
                return "";
            }

            AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec newKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
            return Base64.encodeToString(cipher.doFinal(data.getBytes()), Base64.DEFAULT);
        } catch (Throwable e) {
            Log.e("加密失败", e.getMessage());
            return "";
        }

    }
}