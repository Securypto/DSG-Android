package io.securypto.dsgv1;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;


import android.content.ClipboardManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

import io.securypto.DSGV1.R;


public class firstpage extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static String passwd_to_login_value = "a";
    public static String account_to_login_value = "b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);




/*
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if ("android.intent.action.SEND".equals(action) && type != null && "text/plain".equals(type)) {
            Toast.makeText(getApplicationContext(), ""+intent.getStringExtra("android.intent.extra.TEXT"), Toast.LENGTH_SHORT).show();
        }
*/




        startvideo();

        onSharedIntent();



    }






    public void onSharedIntent() {
        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        String receivedType = receiverdIntent.getType();


        if (receivedAction != null) {

            if (receivedAction.equals(Intent.ACTION_SEND)) {

                // check mime type
                if (receivedType.startsWith("text/")) {

                    String receivedText = receiverdIntent.getStringExtra(Intent.EXTRA_TEXT);
                    if (receivedText != null) {
                        //do your stuff
                        Toast.makeText(getApplicationContext(), receivedText, Toast.LENGTH_SHORT).show();
                    }
                } else if (receivedType.startsWith("image/")) {

                    Uri receiveUri = (Uri) receiverdIntent
                            .getParcelableExtra(Intent.EXTRA_STREAM);

                    if (receiveUri != null) {
                        //do your stuff

                        String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/sendbyuser.jpg";
                        babak.savefile(getApplicationContext(), receiveUri, destinationFilename);
                        //Toast.makeText(getApplicationContext(), receiveUri.toString(), Toast.LENGTH_SHORT).show();


                    }
                }

            } else if (receivedAction.equals(Intent.ACTION_MAIN)) {

                // Log.e(TAG, "onSharedIntent: nothing shared" );
            }
        }
    }





    public void startvideo() {

      // correct convert
    //ffmpeg -i introorg1.mp4 -an -vcodec libx264 -crf 26 -s 800x480 intro1.mp4

        VideoView v = (VideoView) findViewById(R.id.videoView);
        Uri uri= Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro3);
        v.setVideoURI(uri);
        v.start();
        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override    public void onPrepared(MediaPlayer mediaPlayer)
            {
                mediaPlayer.setLooping(true);
            }
        });
    }





    /** Called when the user taps the Send button */
    public void sendpasswd(View view) {










        //    Intent intent = new Intent(this, firstpage.class);

        EditText account_to_login = (EditText) findViewById(R.id.account_to_login);
        String vault_name = account_to_login.getText().toString();
       // intent.putExtra(account_to_login_value, vault_name);

        EditText passwd_to_login = (EditText) findViewById(R.id.passwd_to_login);
        String vault_passwd = passwd_to_login.getText().toString();
     //   intent.putExtra(passwd_to_login_value, vault_passwd);


        if (!babak.validate_vault_passwd(vault_passwd) || !babak.validate_vault_name(vault_name)) {
            Toast.makeText(firstpage.this, "Vault Name or Password can't be empty!", Toast.LENGTH_LONG).show();
        } else {



            //enc vault name first, remove all non alphanum and use the first 20ch
            String vault_name_enc_by_aes = AESCrypt.encrypt(vault_passwd, vault_name).replaceAll("[^A-Za-z0-9]", "");
            String vault_name_short = vault_name_enc_by_aes.substring(0, Math.min(vault_name_enc_by_aes.length(), 20));



            String dataToEncrypt = "DigiSafeGuard";
            String decrypted = "NOTDigiSafeGuard";

            //lees_pubic_key
            Context context_lees_pubic_key = getApplicationContext();
            String publicKeyBytesBase64_gelezen = encclass.read_pubic_key(vault_passwd, context_lees_pubic_key, vault_name_short);
            //lees_private_key
            Context context_lees_private_key = getApplicationContext();
            String privateKeyBytesBase64_gelezen = encclass.read_private_key(vault_passwd, context_lees_private_key, vault_name_short);
            // enc a text using RSA
            String encrypted = encclass.encryptRSAToString(publicKeyBytesBase64_gelezen, dataToEncrypt);
            // dec a text using RSA
            decrypted = encclass.decryptRSAToString(privateKeyBytesBase64_gelezen, encrypted);


            if (dataToEncrypt.equals(decrypted)) {
                // Have a party

                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                globalVariable.set_vault_name(vault_name);
                globalVariable.set_vault_name_short(vault_name_short);
                globalVariable.set_vault_passwd(vault_passwd);
                globalVariable.set_current_valt_Pub_key(publicKeyBytesBase64_gelezen);
                globalVariable.set_current_valt_Priv_key(privateKeyBytesBase64_gelezen);

               // Toast.makeText(getApplicationContext(), ""+decrypted, Toast.LENGTH_SHORT).show();

                Intent myIntent5 = new Intent(getBaseContext(),   login_succes.class);
                startActivity(myIntent5);



/*

//read clip
                myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData abc = myClipboard.getPrimaryClip();
                ClipData.Item item = abc.getItemAt(0);
                String textfromclipboard = item.getText().toString();
//reset clip
                ClipData myClip;
                String text = "";
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);


                try {
                    String[] msgstukken = textfromclipboard.split("\\DSGSEPERATOR", -1);
                    String msgtype = msgstukken[0];
                    String keyfordecryption = msgstukken[1];
                    String msgtodecrypte = msgstukken[2];

                    //first dec the key using our own pub
                    String decryptedaeskey = encclass.decryptRSAToString(privateKeyBytesBase64_gelezen, keyfordecryption);
                    //now decrypte the sended data
                    String final_msg = AESCrypt.decrypt(decryptedaeskey, msgtodecrypte);


                    if (msgtype.equals("DSGMSG") && final_msg != null) {


                       // Toast.makeText(this, "1 "+final_msg, Toast.LENGTH_LONG).show();
                        Intent intentq = new Intent(this, showfromclip.class);
                        Bundle extras = new Bundle();
                        extras.putString("EXTRA_DEC_MSG_FROM_CLIP",final_msg);
                        intentq.putExtras(extras);
                        startActivity(intentq);


                    }
                    else
                    {
                        Intent myIntent5 = new Intent(getBaseContext(),   login_succes.class);
                        startActivity(myIntent5);
                    }


                }catch (Exception e) {
                    // Handle the error/exception
                    Intent myIntent5 = new Intent(getBaseContext(),   login_succes.class);
                    startActivity(myIntent5);
                }



*/

























            } else {
                // Be really, really sad
                Toast.makeText(this, "Wrong credentials!", Toast.LENGTH_LONG).show();

            //   File file = new File(getBaseContext().getExternalFilesDir(null)+"/testv11.txt");
             //   shareFile(file);


            }





    }

    }






    public void goto_page_new_installation(View View){
        Intent myIntent_page_new_installation = new Intent(getBaseContext(),   page_new_installation.class);
        startActivity(myIntent_page_new_installation);
    }





    public void goto_settings(View View){
        Intent myIntent2s = new Intent(getBaseContext(),   settings.class);
        startActivity(myIntent2s);
    }


/*
    public void gotohelppage1(View View){
        Intent myIntent2 = new Intent(getBaseContext(),   qrshowdata.class);
        startActivity(myIntent2);
    }
*/


    public void gotobackup(View View){
        Intent myIntent24 = new Intent(getBaseContext(),   backup.class);
        startActivity(myIntent24);
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }









/*
    private void shareFile(File file) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://"+file.getAbsolutePath()));

        //if you need
        //intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Sharing File Subject);
        //intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File Description");

        startActivity(Intent.createChooser(intentShareFile, "Share File"));

    }

*/





}
