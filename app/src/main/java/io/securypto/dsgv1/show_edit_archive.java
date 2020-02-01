package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.nio.charset.StandardCharsets;

import io.securypto.DSGV1.R;

public class show_edit_archive extends AppCompatActivity {


    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.show_edit_archive);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("archivemode","Text & Notes");
        editor.commit();



Bundle extras = getIntent().getExtras();
if(extras != null) {


    dialog = new ProgressDialog(this);
    dialog.setMessage(getResources().getString(R.string.Decrypting_in_progress));
    dialog.setCancelable(false);
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    dialog.show();


    final String archivefiletoshow = extras.getString("archivefiletoshow");

    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
    final String vault_passwd = globalVariable.get_vault_passwd();
    final String vault_name_short = globalVariable.get_vault_name_short();
    final String current_valt_Priv_key = globalVariable.get_current_valt_Priv_key();


    new Thread() {
        public void run() {


            //enc desc using AES
            String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, archivefiletoshow);

            //gen false name
            encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

            String selected_file_by_userNOW = "DSG_ARCHIVE_" + vault_name_short + "_" + encrypted_desc_to_use_as_file_name;


            Context context1 = getApplicationContext();
            String inhoudtextunenc = babak.read_file(context1, selected_file_by_userNOW);

            final String inhoudtextdecrypted = babak.dec_a_text_using_RSA_AND_AES(getApplicationContext(), inhoudtextunenc);


            runOnUiThread(new Runnable() {
                public void run() {
                    // Update UI elements
                    TextView textViewmsg34f = findViewById(R.id.usertexttoencfieldarchive);
                    textViewmsg34f.setText(inhoudtextdecrypted);

                    TextView textViewmsg3ba = findViewById(R.id.filenametouseadarchivenametosafe);
                    textViewmsg3ba.setText(archivefiletoshow);
                    dialog.dismiss();

                }
            });


        }
    }.start();


}












        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));
    }





    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
    }










//singel confirm dialog

    public void confirmDialogDemooverride(View View) {

        EditText editText4 = (EditText) findViewById(R.id.filenametouseadarchivenametosafe);
        String desc = editText4.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();


        //enc desc using AES
        String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
        //gen false name
        encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

        String filenametowrite_archive = "DSG_ARCHIVE_" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;


        if(desc.equals("")){
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Subject_cant_be_empty), Toast.LENGTH_SHORT).show();

        }else {


            File file_dest = getApplicationContext().getFileStreamPath(filenametowrite_archive);
            if (file_dest == null || !file_dest.exists()) {
                encusertextarchive();
            } else {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.WARNING);
                builder.setMessage("\""+desc+"\" " + getResources().getString(R.string.Exist));
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

                    //Context context_file_exist= getApplicationContext();

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        encusertextarchive();

                    }


                });


                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();


            }
        }

    }







    public void encusertextarchive() {

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Data_Encryption));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        new Thread() {
            public void run() {




        EditText editText3 = (EditText) findViewById(R.id.usertexttoencfieldarchive);
        String text_to_enc1 = editText3.getText().toString();

        EditText editText4 = (EditText) findViewById(R.id.filenametouseadarchivenametosafe);
        String desc = editText4.getText().toString();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();


                //enc desc using AES
                String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
                //gen false name
                encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

                String filenametowrite_archive = "DSG_ARCHIVE_" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;

                //enc using RSA&AES COMBINE
                String encedtext = babak.enc_a_text_using_RSA_AND_AES(getApplicationContext(), text_to_enc1);
                babak.write(getApplicationContext(), filenametowrite_archive, encedtext);





                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        dialog.dismiss();

                        //Toast.makeText(getApplicationContext(), "Record has been saved!", Toast.LENGTH_SHORT).show();


                        Intent myIntent333316c = new Intent(getBaseContext(),   archive.class);
                        startActivity(myIntent333316c);
                        finish();

                    }
                });



            }
        }.start();


    }








    public void gotovaultpage(View View){
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   archive.class);
        startActivity(myIntentgotofirstpage);


    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            // Intent myIntentgotofirstpagea = new Intent(getBaseContext(),   login_succes.class);
            //  startActivity(myIntentgotofirstpagea);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }








}
