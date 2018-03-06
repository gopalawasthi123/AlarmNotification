package com.example.gopalawasthi.alarmnotification;

import android.util.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Gopal Awasthi on 04-03-2018.
 */


public class SmsCrypt {

    private static final String CIPHER_ALGORITHM = "AES";
    private static final String RANDOM_GENERATOR_ALGORITHM = "SHA1PRNG";
    private static final int RANDOM_KEY_SIZE = 128;
    //for encryption of the text
    public static String encrypt(String password, String data) throws Exception {
     byte[] secretKey = generateKey(password.getBytes());
        byte[] clear = data.getBytes();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, CIPHER_ALGORITHM);
        Cipher cipher =Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec);
        byte [] encrypted = cipher.doFinal(clear);
    String encryptedString = Base64.encodeToString(encrypted,Base64.DEFAULT);
     return  encryptedString;
}
// for decription of the text
 public  static String decrypt ( String password, String data) throws Exception{
        byte [] secretkey = generateKey(password.getBytes());

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretkey,CIPHER_ALGORITHM);
        Cipher cipher =Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
        byte [] encrypted = Base64.decode(data,Base64.DEFAULT);
        byte [] decypt = cipher.doFinal(encrypted);
  return new String (decypt);

 }
 //generate Key algorithm
    public static byte[] generateKey(byte [] seed) throws Exception{
        KeyGenerator keyGenerator = KeyGenerator.getInstance(CIPHER_ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance(RANDOM_GENERATOR_ALGORITHM);
        secureRandom.setSeed(seed);
        keyGenerator.init(RANDOM_KEY_SIZE,secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }
}

