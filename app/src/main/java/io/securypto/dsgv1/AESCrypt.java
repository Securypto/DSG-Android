package io.securypto.dsgv1;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import static android.util.Base64.encodeToString;
import static java.util.Base64.getDecoder;


public class AESCrypt {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String secret, String strToEncrypt)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            //api 26
           // return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

           // api 8
            byte[] tijdelijk2 = cipher.doFinal(strToEncrypt.getBytes("utf-8"));
            return android.util.Base64.encodeToString(tijdelijk2, android.util.Base64.DEFAULT);



        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting1: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String secret, String strToDecrypt)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            //api 26
           // return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));

            //appi8
            return new String(cipher.doFinal(android.util.Base64.decode(strToDecrypt, android.util.Base64.DEFAULT)));


        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting2: " + e.toString());
        }
        return null;
    }






}



