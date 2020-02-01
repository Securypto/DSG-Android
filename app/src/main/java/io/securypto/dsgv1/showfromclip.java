package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.securypto.DSGV1.R;

public class showfromclip extends AppCompatActivity {



    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_showfromclip);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String dec_msg_from_clip = extras.getString("EXTRA_DEC_MSG_FROM_CLIP");


        TextView textViewmsg1 = findViewById(R.id.usertexttodecfield2);
        textViewmsg1.setText(dec_msg_from_clip);

        //Toast.makeText(this, "2 "+dec_msg_from_clip, Toast.LENGTH_LONG).show();

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

    public void encusertextarchive(View View) {


        EditText editText4 = (EditText) findViewById(R.id.filenametouseadarchivenametosafe);
        String desc = editText4.getText().toString();


        if(desc.equals("")){
            Toast.makeText(getApplicationContext(), R.string.Subject_cant_be_empty, Toast.LENGTH_SHORT).show();

        }else {
            encusertextarchive_green_light();

        }

    }









    public void encusertextarchive_green_light() {

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Data_Encryption));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        new Thread() {
            public void run() {




                EditText editText3 = (EditText) findViewById(R.id.usertexttodecfield2);
                String text_to_enc1 = editText3.getText().toString();

                EditText editText4 = (EditText) findViewById(R.id.filenametouseadarchivenametosafe);
                String desc = editText4.getText().toString();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("y-M-d HH:mm:ss");
                String timetouse = mdformat.format(calendar.getTime());

                desc = desc +" | "+timetouse;

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

                        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("archivemode","Text & Notes");
                        editor.commit();


                        Intent myIntent333316c = new Intent(getBaseContext(),   archive.class);
                        startActivity(myIntent333316c);
                        finish();





                    }
                });



            }
        }.start();


    }






    public void gotovaultpage(View View){
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   login_succes.class);
        startActivity(myIntentgotofirstpage);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
