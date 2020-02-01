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

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_write_a_msg);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);

        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));
    }


    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
    }





    public void encusertextmethode(View View) {


        Intent intent = new Intent(this, write_a_msg.class);

        EditText editText3 = (EditText) findViewById(R.id.usertexttoencfield);
        String texttoenc = editText3.getText().toString();

        Bundle extras = getIntent().getExtras();
        String user_pub_to_use = extras.getString("user_pub_to_use");


        // First create a rand string 20ch long to use as passwd to enc user data
        String randomstring = babak.randomAlphaNumeric(20);
        // texttoenc= "TYPEISTEXTBABAKSEPERATOR" + texttoenc;

        //enc data using AES + just created randstring
        String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
        //enc the randstring using RSA key
        String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(user_pub_to_use, randomstring);
        String combinedmsg = "DSGMSGDSGSEPERATOR"+encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_current_data_msg_for_qr(combinedmsg);
        globalVariable.set_current_data_array_part(0);



        Intent myIntent2 = new Intent(getBaseContext(),   qrshow.class);
        startActivity(myIntent2);
        finish();





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