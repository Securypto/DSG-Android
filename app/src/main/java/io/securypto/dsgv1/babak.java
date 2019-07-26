package io.securypto.dsgv1;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.securypto.DSGV1.R;


public class babak {







     //lees een file
    //gebruik je zo
    //Context context1 = getApplicationContext();
    //String filenametoread = "hello.txt";
    //String inhoudtext = babak.read_file(context1, filenametoread);


    public static String read_file(Context context1, String filename) {
        try {
            FileInputStream fis = context1.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (FileNotFoundException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        } catch (IOException e) {
            return "";
        }
    }




        //write text to file
        //gebruik je zo
        //String texttoschrijven = message1 + " - " + message2;
        //String filenametowrite = "hello.txt";
        //Context context2 = getApplicationContext();
        //babak.write(context2, filenametowrite,texttoschrijven);


    public static String write(Context context2, String filenametowrite, String texttoschrijven) {

        FileOutputStream outputStream;

        try {
            outputStream = context2.openFileOutput(filenametowrite, Context.MODE_PRIVATE);


            outputStream.write(texttoschrijven.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }












    public static Bitmap texttoqr(Context contextqr, String Value, int size) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    size, size, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {


                pixels[offset + x] = bitMatrix.get(x, y) ?
                        contextqr.getResources().getColor(R.color.QRCodeBlackColor):contextqr.getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }








    public static boolean validate_vault_name(String texttovalidate) {


        if(texttovalidate.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }



    public static boolean validate_vault_passwd(String texttovalidate) {

        if(texttovalidate.isEmpty()){
            return false;
        }
        else{
            return true;
        }
    }





    public static boolean is_it_a_passwd_manager_file(String s, String vault_name_short) {

        String tosearch="DSG_PASSWDM_"+vault_name_short+"_";

        //DSG_PASSWDM_
        if(s.indexOf(tosearch) > -1 ) {
            return true;
        }
        else{
            return false;
        }
    }






    public static boolean is_it_a_contact(String s, String vault_name_short) {

        String tosearch="DSG_CONTACTS_"+vault_name_short+"_";

        //DSG_CONTACTS_
        if(s.indexOf(tosearch) > -1 ) {
return true;
        }
else{
    return false;
        }
    }





    public static boolean is_it_a_DSG_file(String s) {

        //DSG_
        if(s.indexOf("DSG_") > -1) {
            return true;
        }
        else{
            return false;
        }
    }




    public static boolean is_it_a_backup_file(String s) {

        //DSG_backup_
        if(s.indexOf("DigisafeGuard_Backup_") > -1) {
            return true;
        }
        else{
            return false;
        }
    }




    public static String randomAlphaNumeric(int count) {

        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder builder = new StringBuilder();

        while (count-- != 0) {

            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());

            builder.append(ALPHA_NUMERIC_STRING.charAt(character));

        }

        return builder.toString();

    }









    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }








    public static String[] splitToNChar(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }










    public static String write_external(Context context, String filenametowrite, String texttoschrijven) {


        try {

            // Adds a line to the file
            File testFile = new File(context.getExternalFilesDir(null), filenametowrite);
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(texttoschrijven);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return "";
    }









    public static void sharetxt(Context context, String subject, String txt, String share_title) {
        Intent sendintent = new Intent(Intent.ACTION_SEND);
        sendintent.setType("text/plain");
        sendintent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendintent.putExtra(Intent.EXTRA_TEXT, txt);
        context.startActivity(Intent.createChooser(sendintent, share_title));
    }






    public static void create_passwd_manager_file(Context context, String vault_name_short, String vault_passwd, String desc, String user, String passwd) {



        //enc desc using AES
        String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);

        //gen false name
        encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);


        String filenametowrite_passwd = "DSG_PASSWDM_" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;
        Context context_passwd = context.getApplicationContext();

        String text_to_enc = desc+"============DSG============"+user+"============DSG============"+passwd;

        //enc using aes
        //text_to_enc = AESCrypt.encrypt(vault_passwd, text_to_enc);

        //enc using RSA
        final GlobalClass globalVariable = (GlobalClass) context.getApplicationContext();
        final String current_valt_Pub_key  = globalVariable.get_current_valt_Pub_key();
        text_to_enc = encclass.encryptRSAToString(current_valt_Pub_key, text_to_enc);



        babak.write(context_passwd, filenametowrite_passwd, text_to_enc);

    }










    public static void zip(String[] _files, String zipFileName, int buffer) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
                    dest));
            byte data[] = new byte[buffer];

            for (int i = 0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, buffer);

                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;

                while ((count = origin.read(data, 0, buffer)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







    public static void unzip(String _zipFile, String _targetLocation) {

        //create target location folder if not exist
        // dirChecker(_targetLocatioan);

        try {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {

                //create dir if required while unzipping
                if (ze.isDirectory()) {
                    //    dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(_targetLocation + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();
                    fout.close();
                }

            }
            zin.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }








    public static void savefile(Context context, Uri sourceuri, String destinationFilename)
    {
        try {


            InputStream in = context.getContentResolver().openInputStream(sourceuri);
            OutputStream out = new FileOutputStream(new File(destinationFilename));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }





}
