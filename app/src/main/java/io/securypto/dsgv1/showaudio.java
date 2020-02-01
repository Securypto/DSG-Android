package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.jsibbold.zoomage.ZoomageView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.securypto.DSGV1.R;

public class showaudio extends AppCompatActivity {


    private MediaPlayer   player = null;



    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_show_audio);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("archivemode","Audio Files");
        editor.commit();


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
           // String archivefiletoshow = extras.getString("archivefiletoshow");
           // TextView textViewmsg3ba = findViewById(R.id.filenametouseadarchivenametosafe);
           // textViewmsg3ba.setText(archivefiletoshow);


            TextView textViewmsg3ba = findViewById(R.id.filenametouseadarchivenametosafe);
            textViewmsg3ba.setVisibility(View.GONE);
            Button button_enc_msg_from_outside = (Button) findViewById(R.id.button_enc_msg_from_outside);
            button_enc_msg_from_outside.setVisibility(View.GONE);
        }




        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String tmp_data1  = globalVariable.get_tmp_data1();

        byte[] decoded = Base64.decode(tmp_data1, Base64.DEFAULT);


        String destinationFilenameplayer = getFilesDir() + "/audio_to_enc.3gp";

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destinationFilenameplayer));
            bos.write(decoded);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }



        player = new MediaPlayer();
        try {
            player.setDataSource(destinationFilenameplayer);
            player.prepare();
            player.start();

        } catch (IOException e) {

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



                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                String text_to_enc1  = globalVariable.get_tmp_data1();


                EditText editText4 = (EditText) findViewById(R.id.filenametouseadarchivenametosafe);
                String desc = editText4.getText().toString();

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("y-M-d HH:mm:ss");
                String timetouse = mdformat.format(calendar.getTime());

                desc = desc +" | "+timetouse;



                //final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                final String vault_name_short  = globalVariable.get_vault_name_short();
                final String vault_passwd  = globalVariable.get_vault_passwd();


                //enc desc using AES
                String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
                //gen false name
                encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

                String filenametowrite_archive = "DSG_AUDIO_" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;

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
                        editor.putString("archivemode","Audio Files");
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
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   archive.class);
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
