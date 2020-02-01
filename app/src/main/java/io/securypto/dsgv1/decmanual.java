package io.securypto.dsgv1;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.VideoView;

import io.securypto.DSGV1.R;

public class decmanual extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_decmanual);

        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
    }


    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
    }









    public void decmsg(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_valt_Priv_key = globalVariable.get_current_valt_Priv_key();

        EditText editText3 = (EditText) findViewById(R.id.usertexttodecfield);
        String msgtodec = editText3.getText().toString().trim();

        try {


                    String[] msgstukken = msgtodec.split("\\DSGSEPERATOR", -1);
                    String msgtype = msgstukken[0];
                    String keyfordecryption = msgstukken[1];
                    String msgtodecrypte = msgstukken[2];

                    //first dec the key using our own pub
                    String decryptedaeskey = encclass.decryptRSAToString(current_valt_Priv_key, keyfordecryption);
                    //now decrypte the sended data
                    String final_msg = AESCrypt.decrypt(decryptedaeskey, msgtodecrypte);


//dont remove yet... ?  unknow
           // TextView textViewmsg = findViewById(R.id.usertexttodecfield);
           // textViewmsg.setText("Not a valid message!"+current_valt_Priv_key);



            if (msgtype.equals("DSGMSG") && final_msg != null) {
                TextView textViewmsg1 = findViewById(R.id.usertexttodecfield);
                textViewmsg1.setText(final_msg);
                // Toast.makeText(this, "Complete, Decrypting the Message!"+final_msg, Toast.LENGTH_LONG).show();
            }
            else
            {
                TextView textViewmsg2 = findViewById(R.id.usertexttodecfield);
                textViewmsg2.setText(R.string.Not_a_valid_message);
            }


        }catch (Exception e) {
            // Handle the error/exception
            TextView textViewmsg3 = findViewById(R.id.usertexttodecfield);
            textViewmsg3.setText(R.string.Not_a_valid_message);
        }



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
