package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.securypto.DSGV1.R;


public class write_a_msg extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_a_msg);

startvideo();
    }





    public void startvideo() {

        // correct convert
        //ffmpeg -i introorg1.mp4 -an -vcodec libx264 -crf 26 -s 800x480 intro1.mp4

        VideoView v = (VideoView) findViewById(R.id.videoView);
        Uri uri= Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro1);
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



    public void encusertextmethodereal() {


        Intent intent = new Intent(this, write_a_msg.class);

        EditText editText3 = (EditText) findViewById(R.id.usertexttoencfield);
        String texttoenc = editText3.getText().toString();

        Bundle extras = getIntent().getExtras();
        String selected_contact_by_user = extras.getString("selected_contact_by_user");

        //Toast.makeText(this, "account:"+selected_contact_by_user+"msg:"+texttoenc, Toast.LENGTH_LONG).show();
        //System.out.println("thank" + randomstring);





        // First create a rand string 20ch long to use as passwd to enc user data
        String randomstring = babak.randomAlphaNumeric(20);
        //get pub key of selected contact to enc the randomstring too
        Context context_lees_pubic_key = getApplicationContext();
        String publicKeyBytesBase64_gelezen = babak.read_file(context_lees_pubic_key, selected_contact_by_user);


        //enc data using AES + just created randstring
        String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
        //enc the randstring using RSA key
        String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(publicKeyBytesBase64_gelezen, randomstring);


        //write both to 2 seperate files.
       // Context context_to_write_userdata = getApplicationContext();
       // babak.write(context_to_write_userdata, "tmpuserdata",encrypted_data_by_AES_and_randstring);
       // Context context_to_write_key_to_dec_user_data = getApplicationContext();
       // babak.write(context_to_write_key_to_dec_user_data, "tmpdatakey",encrypted_randstring_by_user_pubkey);


       // String combinedmsg = "DSGMASSAGE:"+encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;
        String combinedmsg = "DSGMSGDSGSEPERATOR"+encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;

       // Context context_write_external = getApplicationContext();
      //  babak.write_external(context_write_external, "encmsgtosend.txt",combinedmsg);



        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_current_data_msg_for_qr(combinedmsg);
        globalVariable.set_current_data_array_part(0);

      //  Intent myIntent2 = new Intent(getBaseContext(),   qrshow.class);
    //    startActivity(myIntent2);
     //   finish();





    }





    public void encusertextmethode(View View) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.loading,
                null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(formElementsView);
        builder.setCancelable(false);
        builder.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {


                encusertextmethodereal();
                Intent myIntent2 = new Intent(getBaseContext(),   qrshow.class);
                startActivity(myIntent2);
                finish();

            }
        }, 1000);

    }



    public void gotovaultpage(View View){
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   login_succes.class);
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