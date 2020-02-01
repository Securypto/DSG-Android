package io.securypto.dsgv1;

import android.content.Context;
import android.util.Base64;

import java.io.File;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;





public class encclass {

    public static KeyPair getKeyPair() {
        KeyPair kp = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            //kpg.initialize(2048);
            //kpg.initialize(2048);
            kpg.initialize(4096);
            kp = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kp;
    }

    public static String encryptRSAToString(String publicKey, String clearText) {
        String encryptedBase64 = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePublic(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(clearText.getBytes("UTF-8"));
            encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedBase64.replaceAll("(\\r|\\n)", "");
    }

    public static String decryptRSAToString(String privateKey, String encryptedBase64) {

        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePrivate(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedString;
    }










    public static String  gen_pub_priv_key_and_safe(String passwd_sended, Context context_key_gen, String account_name_sended){

        //gen key
        KeyPair kop = encclass.getKeyPair();

        //gen public
        PublicKey publicKey = kop.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();
        String publicKeyBytesBase64 = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));

        //gen private
        PrivateKey privateKey = kop.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String privateKeyBytesBase64 = new String(Base64.encode(privateKeyBytes, Base64.DEFAULT));

        //enc public key
        String encrypted_public_key = AESCrypt.encrypt(passwd_sended, publicKeyBytesBase64);

        //enc private key
        String encrypted_private_key = AESCrypt.encrypt(passwd_sended, privateKeyBytesBase64);


        //enc vault name first, remove all non alphanum and use the first 20ch
        String vault_name_enc_by_aes = AESCrypt.encrypt(passwd_sended, account_name_sended).replaceAll("[^A-Za-z0-9]", "");
        String vault_name_short = vault_name_enc_by_aes.substring(0, Math.min(vault_name_enc_by_aes.length(), 20));


        //System.out.println("thank dec" + vault_name_short);

        // save enc public & private key
        String filenametowrite_publicKey = "DSG_VAULT_" + vault_name_short + "_publicKey";
        Context context_pubkey = context_key_gen.getApplicationContext();

        String filenametowrite_privateKey = "DSG_VAULT_" + vault_name_short +"_privateKey";
        Context context_privekey = context_key_gen.getApplicationContext();


        File file = context_pubkey.getFileStreamPath(filenametowrite_publicKey);
        if(file == null || !file.exists()) {

            babak.write(context_pubkey, filenametowrite_publicKey, encrypted_public_key);
            babak.write(context_privekey, filenametowrite_privateKey, encrypted_private_key);
        }




       // System.out.println("pub thank" + encrypted_public_key);
       // System.out.println("pri thank" + encrypted_private_key);

return "";

    }









    public static String read_pubic_key(String passwd_sended, Context context_lees_pubic_key, String user_account){


        String filenametoread_publicKey = "DSG_VAULT_" + user_account + "_publicKey";
        Context read_pubic_key_tmp = context_lees_pubic_key.getApplicationContext();
        String publicKeyBytesBase64_gelezen = babak.read_file(read_pubic_key_tmp, filenametoread_publicKey);


        //dec public key
        try {
        String decerypted_public_key = AESCrypt.decrypt(passwd_sended, publicKeyBytesBase64_gelezen);
            return decerypted_public_key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



    }




    public static String  read_private_key(String passwd_sended, Context context_lees_private_key, String user_account){

        String filenametoread_privateKey = "DSG_VAULT_" + user_account + "_privateKey";
        Context read_private_key_tmp = context_lees_private_key.getApplicationContext();
        String privateKeyBytesBase64_gelezen = babak.read_file(read_private_key_tmp, filenametoread_privateKey);


        //dec private key
        try {
        String decerypted_private_key = AESCrypt.decrypt(passwd_sended, privateKeyBytesBase64_gelezen);
            return decerypted_private_key;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }



    }






}
