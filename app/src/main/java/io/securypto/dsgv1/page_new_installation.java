package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.securypto.DSGV1.R;


public class page_new_installation extends AppCompatActivity {



        public static String passwdvalue="a";
        public static String account_name_value="b";
       // public static String bodyData="f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_new_installation);


        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());

/*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(bodyData,Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(bodyData));
        }
*/

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






        /** Called when the user taps the Send button */
        public void sendpasswd(View view) {
            // Do something in response to button

            Intent myIntent_page_gen_key = new Intent(getBaseContext(), page_new_installation.class);

            EditText account_name_field = (EditText) findViewById(R.id.account_name_field);
            final String vault_name = account_name_field.getText().toString();
           // myIntent_page_gen_key.putExtra(account_name_value, vault_name);

            EditText passwd_field = (EditText) findViewById(R.id.passwd_field);
            final String vault_passwd = passwd_field.getText().toString();
           // myIntent_page_gen_key.putExtra(passwdvalue, vault_passwd);


            if(!babak.validate_vault_passwd(vault_passwd) || !babak.validate_vault_name(vault_name) ){
                Toast.makeText(page_new_installation.this, "Vault Name or Password can't be empty!" , Toast.LENGTH_LONG).show();
            }
            else
            {


                //Gen & safe RSA key
                //Context context_key_gen = getApplicationContext();
              //  encclass.gen_pub_priv_key_and_safe(vault_passwd, context_key_gen, vault_name);

               // alertDialogDemo();


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




                        gen_keys_real(vault_passwd, vault_name);


                    }
                }, 1000);





            }




















        }




    public void gen_keys_real(String vault_passwd, String vault_name) {





                //Gen & safe RSA key
                Context context_key_gen = getApplicationContext();
                encclass.gen_pub_priv_key_and_safe(vault_passwd, context_key_gen, vault_name);

                alertDialogDemo();


    }




    private void alertDialogDemo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Vault Creation");
        builder.setMessage("Your new vault has been created!");
        builder.setCancelable(true);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent_gotologinpage = new Intent(getBaseContext(), firstpage.class);
                startActivity(myIntent_gotologinpage);
            }
        });
        builder.show();
    }



    public void cancelandgoback(View View){
        Intent myIntent2 = new Intent(getBaseContext(),   firstpage.class);
        startActivity(myIntent2);
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
