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
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.zxing.WriterException;

import org.jcodec.api.SequenceEncoder;
import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.securypto.DSGV1.R;
import android.os.Bundle;
import android.os.Handler;


public class qrshow extends AppCompatActivity {


    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.qrshow);



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Data_Encryption));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();




        GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        int random = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80]
        globalVariable.set_current_data_array_part(random);

        new Thread() {
            public void run() {
                encdatathreath();
            }
        }.start();




                babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

    }





    @Override
    public void onResume(){
        super.onResume();

        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        /*
        dialog = new ProgressDialog(this);
        dialog.setMessage("Encryption in progress...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        int random = new Random().nextInt(61) + 20; // [0, 60] + 20 => [20, 80]
        globalVariable.set_current_data_array_part(random);

        new Thread() {
            public void run() {
                encdatathreath();
            }
        }.start();
*/

    }














    public void encdatathreath() {


        final TextView myAwesomeTextView = (TextView) findViewById(R.id.textqrcounter);

        final ImageButton button_share1 = (ImageButton) findViewById(R.id.button_share1);
        final ImageButton button_share2 = (ImageButton) findViewById(R.id.button_share2);

        final ImageView qrholder = (ImageView) findViewById(R.id.qrholder);
        final Uri uri = Uri.parse(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Encrypted_data.mp4");


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();
        // final int current_data_array_part  = globalVariable.get_current_data_array_part();

        //remove old one for the next time
        //globalVariable.set_current_data_msg_for_qr("");

        //save as .dsg file
        String destinationFilenamedsg = "Encrypted_data.dsg";
        babak.write_external_downloaddir(getApplicationContext(), destinationFilenamedsg, current_data_msg_for_qr);


        final String[] chunkarray = babak.splitToNChar(current_data_msg_for_qr, 1000);
        String userdatatoshowonqrfinal = "";
        String md5hash = "";
        String datachunckreadytoshow = "";
        int current_data_array_part_plus_one = 0;
        String destinationFilename = "";


        if (chunkarray.length == 1) {


            runOnUiThread(new Runnable() {
                public void run() {
                    // Update UI elements
                    button_share2.setVisibility(View.GONE);

                }
            });

        } else {

            runOnUiThread(new Runnable() {
                public void run() {
                    // Update UI elements
                    button_share1.setVisibility(View.GONE);

                }
            });

        }


        final int current_data_array_part_org = globalVariable.get_current_data_array_part();


        int i = 0;
        int ii = 0;
        final int currentthread = current_data_array_part_org;

        while (i < chunkarray.length) {

            int current_data_array_part_org_now = globalVariable.get_current_data_array_part();
            if(current_data_array_part_org_now == currentthread){





            userdatatoshowonqrfinal = chunkarray[i];
            md5hash = babak.md5(userdatatoshowonqrfinal);
            current_data_array_part_plus_one = i + 1;


            //include md5 hash
            // example  DSGMSGPART:2:5:hash:msg
            datachunckreadytoshow = "DSGMSGPART:" + current_data_array_part_plus_one + ":" + chunkarray.length + ":" + md5hash + ":" + userdatatoshowonqrfinal;


            final Bitmap bitmap;
            try {

                Context contextqr = getApplicationContext();
                bitmap = babak.texttoqr(contextqr, datachunckreadytoshow, 500);


                if (ii == 0) {

                    ii++;


                    try {
                        //destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Encrypted_data"+current_data_array_part_plus_one+".jpg";
                        destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Encrypted_data.jpg";
                        File newfile = babak.savebitmap(bitmap, destinationFilename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Update UI elements
                            dialog.dismiss();


                        }
                    });

                }


                final int itoshow = current_data_array_part_plus_one;
                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        qrholder.setImageBitmap(bitmap);

                        myAwesomeTextView.setText("QR " + itoshow + "/" + chunkarray.length);

                    }
                });


            } catch (WriterException e) {
                e.printStackTrace();
            }





            //reset
            if (current_data_array_part_plus_one == chunkarray.length && chunkarray.length > 1) {
                i = -1;


                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        //Toast.makeText(getApplicationContext(), "Restarting QR Dance.." + currentthread + "|" + current_data_array_part_org, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), R.string.Restarting_QR_Dance, Toast.LENGTH_SHORT).show();
                        //myAwesomeTextView.setText("Tap to restart QR Dance...");
                    }
                });


            }


        }
            i++;
    }










    }










    public void shareimage(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();
        globalVariable.set_current_data_array_part(0);
        Context context_share_txt= qrshow.this;

        //share text
        //babak.sharetxt(context_share_txt, "DSG Encrypted Message", current_data_msg_for_qr, "Share DSG");

        //share file jpg
        String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Encrypted_data.jpg";
        babak.sharefile(context_share_txt,  destinationFilename, "DSG Encrypted Message", "image/jpg");
    }


    public void sharevideo(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();
        Context context_share_txt= qrshow.this;

        //share text
        //babak.sharetxt(context_share_txt, "DSG Encrypted Message", current_data_msg_for_qr, "Share DSG");

        //share file jpg
        String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Encrypted_data.mp4";
        babak.sharefile(context_share_txt,  destinationFilename, "DSG Encrypted Message", "video/mp4");
    }


    public void sharedsgfile(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();
        globalVariable.set_current_data_array_part(0);
        Context context_share_txt= qrshow.this;


        //share text
        //babak.sharetxt(context_share_txt, "DSG Encrypted Message", current_data_msg_for_qr, "Share DSG");

        //share file jpg
        String destinationFilenamedsg = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Encrypted_data.dsg";
        babak.sharefile(context_share_txt,  destinationFilenamedsg, "DSG Encrypted Message", "application/dsg");
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
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_current_data_array_part(0);
        Intent myIntentgotologinsucces = new Intent(getBaseContext(),   login_succes.class);
        startActivity(myIntentgotologinsucces);
        finish();
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