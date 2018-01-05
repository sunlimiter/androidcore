package com.tywho.common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import okio.ByteString;

/**
 * Created by limit on 2017/3/31.
 * md5摘要，aes加密解密
 */

public class MD5Util {
    /**
     * aes加密
     */
    private static String MASTERPASSWORD = "tywhopassword";
    private final static String HEX = "0123456789ABCDEF";
    private final static int JELLY_BEAN_4_2 = 17;


    public static String MD5(String pass) {
        return ByteString.encodeUtf8(pass).md5().hex();
    }


//    public static void main(String[] args) {
//        try {
//            // 使用该方法
//            String originalText = "123456";
//            String encryptingCode = encrypt(MASTERPASSWORD, originalText);
//            System.out.println("加密结果为 " + encryptingCode);
//            System.out.println(encryptingCode.length());
//            String decryptingCode = decrypt(MASTERPASSWORD, encryptingCode);
//            System.out.println("解密结果" + decryptingCode);
//
//            System.out.println(MD5("123456"));
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    /**
     * 加密
     *
     * @param key 密钥
     * @param src 加密文本
     * @return
     * @throws Exception
     */
    public static String encrypt(String key, String src) throws Exception {
        if (TextUtils.isEmpty(src)) return null;
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] result = encrypt(rawKey, src.getBytes());
        return toHex(result);
    }

    public static String encrypt(String src) throws Exception {
        return encrypt(MASTERPASSWORD, src);
    }

    /**
     * 解密
     *
     * @param key       密钥
     * @param encrypted 待揭秘文本
     * @return
     * @throws Exception
     */
    public static String decrypt(String key, String encrypted) throws Exception {
        if (TextUtils.isEmpty(encrypted)) return null;
        byte[] rawKey = getRawKey(key.getBytes());
        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }

    public static String decrypt(String encrypted) throws Exception {
        return decrypt(MASTERPASSWORD, encrypted);
    }

    /**
     * 获取256位的加密密钥
     *
     * @param seed
     * @return
     * @throws Exception
     */
    @SuppressLint("TrulyRandom")
    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = null;
        // 在4.2以上版本中，SecureRandom获取方式发生了改变
        if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN_4_2) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        // 256 bits or 128 bits,192bits
        kgen.init(256, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }

    /**
     * 真正的加密过程
     *
     * @param key
     * @param src
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] key, byte[] src) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src);
        return encrypted;
    }

    /**
     * 真正的解密过程
     *
     * @param key
     * @param encrypted
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] key, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
