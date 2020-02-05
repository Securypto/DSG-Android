package io.securypto.dsgv1;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import io.securypto.DSGV1.R;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;

public class qrshowpub extends AppCompatActivity {


    private ProgressDialog dialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_qrshowpub);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        final TextView infofieldmsg = (TextView) findViewById(R.id.infofield);
        infofieldmsg.setText(getString(R.string.vault_pubkey_info));


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();


        String datatoshowforqr ="";



        try{
        Bundle extras = getIntent().getExtras();
            datatoshowforqr = extras.getString("qrdatatoshowonqrpage");
            //Toast.makeText(getApplicationContext(), "Contact: "+current_valt_Pub_key, Toast.LENGTH_SHORT).show();
        }catch (Exception e) {
            datatoshowforqr = "DigiSafeGuard-PUBLIC-KEY:" + globalVariable.get_current_valt_Pub_key();
           // Toast.makeText(getApplicationContext(), "Contact: "+current_valt_Pub_key, Toast.LENGTH_SHORT).show();
        }



        final String current_valt_Pub_key_final = datatoshowforqr;



        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Data_Encryption));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        new Thread() {
            public void run() {




                    final ImageView qrholder;
                    final Bitmap bitmap ;
                    qrholder = (ImageView)findViewById(R.id.qrholder);
        try {

                        Context contextqr= getApplicationContext();
                        bitmap = babak.texttoqr(contextqr, current_valt_Pub_key_final, 500);




            runOnUiThread(new Runnable() {
                public void run() {
                    // Update UI elements
                    qrholder.setImageBitmap(bitmap);
                }
            });


                        try {
                            String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/DigiSafeGuard_Public_Key.jpg";
                            File newfile = babak.savebitmap(bitmap, destinationFilename);
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }




                    } catch (WriterException e) {
                        e.printStackTrace();
                    }




                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        dialog.dismiss();
                    }
                });




            }
        }.start();










        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));




    }



    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
    }








    public void share(View View){

        //final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        //final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
        //String current_valt_Pub_key_final = "DigiSafeGuard-PUBLIC-KEY:" + current_valt_Pub_key;
        Context context_share_txt= qrshowpub.this;

        //share text
        //babak.sharetxt(context_share_txt, "DigiSafeGuard Pub Key", current_valt_Pub_key_final, "Share DSG");

        //share file jpg
        String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/DigiSafeGuard_Public_Key.jpg";
        babak.sharefile(context_share_txt,  destinationFilename, "DigiSafeGuard Pub Key", "image/jpg");


    }




    public void share_by_scu(View View){

        dialogscunotreadyyet();

    }



    private void dialogscunotreadyyet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Anonymous_Transfer);
        builder.setMessage(R.string.MSG_SCU_send);
        builder.setCancelable(false);
        builder.setNeutralButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                //finish();
                //startActivity(getIntent());
            }
        });
        builder.show();
    }



    public void gotologinsuccespage(View View){
        Intent myIntentgotologinsucces = new Intent(getBaseContext(),   login_succes.class);
        startActivity(myIntentgotologinsucces);
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