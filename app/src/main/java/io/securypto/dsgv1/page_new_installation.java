package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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

    private ProgressDialog dialog;

        public static String passwdvalue="a";
        public static String account_name_value="b";
       // public static String bodyData="f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.page_new_installation);




        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());



        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

    }


    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));
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
                Toast.makeText(page_new_installation.this, R.string.Vault_Name_or_Password_cant_be_empty , Toast.LENGTH_LONG).show();
            }
            else
            {



                dialog = new ProgressDialog(this);
                dialog.setMessage(getResources().getString(R.string.Creating_new_encryption_keys));
                dialog.setCancelable(false);
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.show();




                new Thread() {
                    public void run() {



                        //Gen & safe RSA key
                        Context context_key_gen = getApplicationContext();
                        encclass.gen_pub_priv_key_and_safe(vault_passwd, context_key_gen, vault_name);



                        runOnUiThread(new Runnable() {
                            public void run() {
                                // Update UI elements
                                dialog.dismiss();

                                alertDialogDemo();
                            }
                        });




                    }
                }.start();





            }




















        }









    private void alertDialogDemo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.New_Vault_Creation);
        builder.setMessage(R.string.Your_new_vault_has_been_created);
        builder.setCancelable(true);
        builder.setNeutralButton(R.string.Ok, new DialogInterface.OnClickListener() {
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
