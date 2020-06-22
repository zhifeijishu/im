package com.tksflysun.im.common.util;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tksflysun.im.common.constants.Constants;
import org.springframework.util.StringUtils;

public class AESCBCUtil {
    private static final Logger logger = LoggerFactory.getLogger(AESCBCUtil.class);

    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    static {
        if (Security.getProvider("BC") == null) {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        } else {
            Security.removeProvider("BC");
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        }
    }

    /**
     * 
     * @param content
     * @param key
     * @param ivStr
     * @return
     */
    public static byte[] encrypt(byte[] content, byte[] key, byte[] ivStr) {

        SecretKeySpec skeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(ivStr);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            return cipher.doFinal(content);

        } catch (Exception e) {
            logger.error("AES encryption operation has exception,content:{},password:{}", content, key, e);
        }
        return null;
    }

    /**
     * 
     * @param content
     * @param key
     * @param ivStr
     * @return
     */
    public static String encryptBase64(byte[] content, byte[] key, byte[] ivStr) {

        SecretKeySpec skeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM, "BC");
            IvParameterSpec iv = new IvParameterSpec(ivStr);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encryptedData = cipher.doFinal(content);
            return Base64.encodeBase64String(encryptedData);

        } catch (Exception e) {
            logger.error("AES encryption operation has exception,content:{},password:{}", content, key, e);
        }
        return null;
    }

    /**
     * 
     * @param content
     * @param key
     * @param ivStr
     * @return
     */
    public static String decrypt(byte[] content, byte[] key, byte[] ivStr) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM, "BC");
            IvParameterSpec iv = new IvParameterSpec(ivStr);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(content);
            return new String(original);
        } catch (Exception e) {
            logger.error("AES decryption operation has exception,content:{},password:{}", content, key, e);
        }
        return null;
    }

    public static String encrypt(String src, String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new Exception("key不满足条件");
        }
        if (key.length() < 16) {
            int diff = 16 - key.length();
            while (diff-- > 0) {
                key += "0";
            }
        }
        if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return byte2hex(encrypted);
    }

    public static String decrypt(String src, String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            throw new Exception("key不满足条件");
        }
        if (key.length() < 16) {
            int diff = 16 - key.length();
            while (diff-- > 0) {
                key += "0";
            }
        }
        if (key.length() > 16) {
            key = key.substring(0, 16);
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = hex2byte(src);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

    public static byte[] hex2byte(String strhex) {
        if (strhex == null) {
            return null;
        }
        int l = strhex.length();
        if (l % 2 == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / 2; i++) {
            b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
                    16);
        }
        return b;
    }

    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        // 返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            // AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes(Charset.forName("UTF-8"))));
            // 生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            logger.error("生成加密密钥失败！", ex);
        }

        return null;
    }

}
