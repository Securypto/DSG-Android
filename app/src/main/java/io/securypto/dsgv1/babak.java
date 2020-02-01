package io.securypto.dsgv1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import io.securypto.DSGV1.R;


public class babak {



    public static String read_file_external_with_line_break(String filename) {
        try {
           // FileInputStream fis = context1.openFileInput(filename);
            FileInputStream fis = new FileInputStream(filename);
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



    public static String read_file_external_no_line_break(String filename) {

        String ret = "";

        try {

            FileInputStream inputStream = new FileInputStream(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

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


    /*

     //geen idee of het werkt

    public static String stream_to_string(FileInputStream fis) {
        try {

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

*/



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


    public static boolean is_it_a_archive_file(String s, String vault_name_short, String lookfor) {

        String tosearch=lookfor+vault_name_short+"_";

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





    public static boolean is_it_a_enc_msg_file(String s) {

        //DSG_backup_
        if(s.indexOf("Encrypted_data") > -1) {
            return true;
        }
        else{
            return false;
        }
    }




    public static boolean does_a_scu_wallet_exist(String s, String vault_name_short, String lookfor) {

        String tosearch=lookfor+vault_name_short+"_";

        if(s.indexOf(tosearch) > -1 ) {
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




    public static String write_external_downloaddir(Context context, String filenametowrite, String texttoschrijven) {


        try {

            // Adds a line to the file
            File testFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filenametowrite);
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, false /*append*/));
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



    public static void sharefile(Context context, String filepath, String share_title, String type) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+filepath));
        intentShareFile.setType(type);
        context.startActivity(Intent.createChooser(intentShareFile, share_title));

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













    public static boolean saveFile(Context context, String name, Uri sourceuri, String destination) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        InputStream input = null;
        boolean hasError = false;

        try {
            if (isVirtualFile(context, sourceuri)) {
                input = getInputStreamForVirtualFile(context, sourceuri, getMimeType(name));
            } else {
                input = context.getContentResolver().openInputStream(sourceuri);
            }

                int originalsize = input.available();

                bis = new BufferedInputStream(input);
                bos = new BufferedOutputStream(new FileOutputStream(destination));
                byte[] buf = new byte[originalsize];
                bis.read(buf);
                do {
                    bos.write(buf);
                } while (bis.read(buf) != -1);

        } catch (Exception e) {
            e.printStackTrace();
            hasError = true;
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            } catch (Exception ignored) {
            }
        }

        return !hasError;
    }


    private static boolean isVirtualFile(Context context, Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!DocumentsContract.isDocumentUri(context, uri)) {
                return false;
            }
            Cursor cursor = context.getContentResolver().query(
                    uri,
                    new String[]{DocumentsContract.Document.COLUMN_FLAGS},
                    null, null, null);
            int flags = 0;
            if (cursor.moveToFirst()) {
                flags = cursor.getInt(0);
            }
            cursor.close();
            return (flags & DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT) != 0;
        } else {
            return false;
        }
    }


    private static InputStream getInputStreamForVirtualFile(Context context, Uri uri, String mimeTypeFilter)
            throws IOException {

        ContentResolver resolver = context.getContentResolver();
        String[] openableMimeTypes = resolver.getStreamTypes(uri, mimeTypeFilter);
        if (openableMimeTypes == null || openableMimeTypes.length < 1) {
            throw new FileNotFoundException();
        }
        return resolver
                .openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)
                .createInputStream();
    }



    private static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }




















    public static File savebitmap(Bitmap bmp, String destinationFilename) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);

        File f = new File(destinationFilename);

        f.createNewFile();
        FileOutputStream fo = new FileOutputStream(f);
        fo.write(bytes.toByteArray());
        fo.close();
        return f;
    }





    public static String scanQRImage(Bitmap bMap) {
        String contents = null;

        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Reader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        }
        catch (Exception e) {
            Log.e("QrTest", "Error decoding barcode", e);
        }
        return contents;
    }











    public static String checkifusercanlogin(Context context, String vault_passwd, String vault_name)
    {

        //enc vault name first, remove all non alphanum and use the first 20ch
        String vault_name_enc_by_aes = AESCrypt.encrypt(vault_passwd, vault_name).replaceAll("[^A-Za-z0-9]", "");
        String vault_name_short = vault_name_enc_by_aes.substring(0, Math.min(vault_name_enc_by_aes.length(), 20));



        String dataToEncrypt = "DigiSafeGuard";
        String decrypted = "NOTDigiSafeGuard";

        //lees_pubic_key
        String publicKeyBytesBase64_gelezen = encclass.read_pubic_key(vault_passwd, context, vault_name_short);
        //lees_private_key
        String privateKeyBytesBase64_gelezen = encclass.read_private_key(vault_passwd, context, vault_name_short);
        // enc a text using RSA
        String encrypted = encclass.encryptRSAToString(publicKeyBytesBase64_gelezen, dataToEncrypt);
        // dec a text using RSA
        decrypted = encclass.decryptRSAToString(privateKeyBytesBase64_gelezen, encrypted);


        if (dataToEncrypt.equals(decrypted)) {
            // Have a party

            final GlobalClass globalVariable = (GlobalClass) context;
            globalVariable.set_vault_name(vault_name);
            globalVariable.set_vault_name_short(vault_name_short);
            globalVariable.set_vault_passwd(vault_passwd);
            globalVariable.set_current_valt_Pub_key(publicKeyBytesBase64_gelezen);
            globalVariable.set_current_valt_Priv_key(privateKeyBytesBase64_gelezen);

            return "yes";
        }
        else
        {
            return "no";
        }



    }









    public static String enc_a_text_using_RSA_AND_AES(Context context, String text_to_enc_using_rsa_and_aes) {

        GlobalClass globalVariable = (GlobalClass) context;
        String current_valt_Pub_key  = globalVariable.get_current_valt_Pub_key();


        // First create a rand string 20ch long to use as passwd to enc user data
        String randomstring = babak.randomAlphaNumeric(20);
        //enc data using AES + just created randstring
        String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, text_to_enc_using_rsa_and_aes);
        //enc the randstring using vault RSA pub key
        String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(current_valt_Pub_key, randomstring);
        //now put together
        String enc_text_ready_to_return  = encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;
        return enc_text_ready_to_return;
    }





    public static String dec_a_text_using_RSA_AND_AES(Context context, String enc_to_text_using_rsa_and_aes) {

        GlobalClass globalVariable = (GlobalClass) context;
        String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();

        String[] msgstukken = enc_to_text_using_rsa_and_aes.split("\\DSGSEPERATOR", -1);
        String keyfordecryption = msgstukken[0];
        String msgtodecrypte = msgstukken[1];
        //first dec the key using our own pub
        String decryptedaeskey = encclass.decryptRSAToString(current_valt_Priv_key, keyfordecryption);
        //now decrypte the sended data
        String final_msg = AESCrypt.decrypt(decryptedaeskey, msgtodecrypte);


        return final_msg;
    }






    public static String dec_a_text_using_RSA_AND_AES_full_text(Context context, String enc_to_text_using_rsa_and_aes) {
        try {
        GlobalClass globalVariable = (GlobalClass) context;
        String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();

        String[] msgstukken = enc_to_text_using_rsa_and_aes.split("\\DSGSEPERATOR", -1);
        String msgtype = msgstukken[0];
        String keyfordecryption = msgstukken[1];
        String msgtodecrypte = msgstukken[2];

        //first dec the key using our own pub
        String decryptedaeskey = encclass.decryptRSAToString(current_valt_Priv_key, keyfordecryption);
        //now decrypte the sended data
        String final_msg = AESCrypt.decrypt(decryptedaeskey, msgtodecrypte);


        return final_msg;

        }catch (Exception e) {
            return null;
        }
    }







    public static void cleandownloaddir(Context context)
    {


            String[] allefiles = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).list();

            for (String each_file_name : allefiles) {
                if (!babak.is_it_a_backup_file(each_file_name)) {

                   // listValue_backups.add(each_file_name);
                    File f = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+each_file_name);
                    boolean d = f.delete();

                }
            }




    }



    public static void cleaninternal(Context context)
    {

        context.deleteFile("audio_to_enc.3gp");

    }







    public static String qr_file_to_text_and_dec_to_get_msg(Context context, String destinationFilename) {
        String final_msg;

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(destinationFilename);
            String decoded = babak.scanQRImage(bitmap);

            String[] msgstukken = decoded.split("\\:", -1);
            String msgtype = msgstukken[0];

            if (msgtype.equals("DSGMSGPART")) {
                final_msg = babak.dec_a_text_using_RSA_AND_AES_full_text(context, msgstukken[4]);
            }

            else
            {
                final_msg = null;
            }


            return final_msg;

        }catch (Exception e) {
            return null;
        }
    }




    public static String qr_file_to_text_and_dec_to_get_vault_address(String destinationFilename) {
        String final_msg;

        try {
            Bitmap bitmap = BitmapFactory.decodeFile(destinationFilename);
            String decoded = babak.scanQRImage(bitmap);

            String[] msgstukken = decoded.split("\\:", -1);
            String msgtype = msgstukken[0];

            if (msgtype.equals("DigiSafeGuard-PUBLIC-KEY")) {
                final_msg = msgstukken[1];
            }

            else
            {
                final_msg = null;
            }


            return final_msg;

        }catch (Exception e) {
            return null;
        }
    }









    public static void startvideo(Context context, VideoView v) {

        // correct convert
        //ffmpeg -i introorg1.mp4 -an -vcodec libx264 -crf 26 -s 800x480 intro1.mp4

        if (babak.isEmulator()) {
            //do nothing
        } else {



        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.intro1);
        v.setVideoURI(uri);
        v.start();
        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });


    }

    }


    public static void startvideointro(Context context, VideoView v) {

        // correct convert
        //ffmpeg -i introorg1.mp4 -an -vcodec libx264 -crf 26 -s 800x480 intro1.mp4

        if (babak.isEmulator()) {
            //do nothing
        } else {


            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.intro3);
            v.setVideoURI(uri);
            v.start();
            v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                }
            });

        }

    }





    public static boolean isEmulator() {

        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);

       // return Build.FINGERPRINT.startsWith("genericss                ");

    }






    // Converting Bitmap image to Base64.encode String type
    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    // Converting File to Base64.encode String type using Method
    public static String getStringFile(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }



/*

    public static boolean doesUserHavePermission_Rexternal(Context context)
    {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean doesUserHavePermission_Wexternal(Context context)
    {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean doesUserHavePermission_audio(Context context)
    {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean doesUserHavePermission_camera(Context context)
    {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean doesUserHavePermission_internet(Context context)
    {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.INTERNET);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean doesUserHavePermission_networkstate(Context context)
    {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
        return result == PackageManager.PERMISSION_GRANTED;
    }



 */




    public static void checkloginstatsu(Context context, Context BaseContext, Activity activity)
    {
        //check login otherwise go to firstpage
        final GlobalClass globalVariable = (GlobalClass) context;
        final String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();
        final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();

        //to reset msg everywhere beside qrshow.java
        globalVariable.set_current_data_array_part(0);


        if (current_valt_Priv_key == null || current_valt_Pub_key == null || current_valt_Priv_key.length() < 10 || current_valt_Pub_key.length() < 10 ) {

            Intent i = BaseContext.getPackageManager().
                    getLaunchIntentForPackage(BaseContext.getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(i);
            //activity.finishAffinity();
            restartapp(context);






        }

    }










    public static void restartapp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }




/*


    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }

        return false;
    }








    public static String getScreenOrientation(Context context){
        final int screenOrientation = ((WindowManager)  context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (screenOrientation) {
            case Surface.ROTATION_0:
                return "SCREEN_ORIENTATION_PORTRAIT";
            case Surface.ROTATION_90:
                return "SCREEN_ORIENTATION_LANDSCAPE";
            case Surface.ROTATION_180:
                return "SCREEN_ORIENTATION_REVERSE_PORTRAIT";
            default:
                return "SCREEN_ORIENTATION_REVERSE_LANDSCAPE";
        }
    }


*/


    public static void checkscreenshotstatus(SharedPreferences settings, Window window) {
        //check if screenshot needs to be disabled
        if(!settings.getString("screenshot", "").equals("On"))
        {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
    }




}
