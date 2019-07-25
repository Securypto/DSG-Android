package io.securypto.dsgv1;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.zxing.WriterException;
import java.util.Timer;
import java.util.TimerTask;

import io.securypto.DSGV1.R;


public class qrshow extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrshow);

        Button Button_Return1 = (Button) findViewById(R.id.Button_Return1);
        Button button_restart = (Button) findViewById(R.id.button_restart);
        Button button_share1 = (Button) findViewById(R.id.button_share1);
        Button_Return1.setVisibility(View.GONE);
        button_restart.setVisibility(View.GONE);
        button_share1.setVisibility(View.GONE);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        int current_data_array_part = globalVariable.get_current_data_array_part();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();


        int current_data_array_partnew=current_data_array_part+1;
        globalVariable.set_current_data_array_part(current_data_array_partnew);


        //current_data_msg_for_qr="babak1babak2babak3babak4babak5";


        String[] chunkarray = babak.splitToNChar(current_data_msg_for_qr, 500);

        String userdatatoshowonqrfinal=chunkarray[current_data_array_part];

        String md5hash= babak.md5(userdatatoshowonqrfinal);

        //include md5 hash
        // example  DSGMSGPART:2:5:hash:msg
        String datachunckreadytoshow="DSGMSGPART:"+current_data_array_partnew+":"+chunkarray.length+":"+md5hash+":"+userdatatoshowonqrfinal;




        ImageView qrholder;
        Bitmap bitmap ;
        qrholder = (ImageView)findViewById(R.id.qrholder);
        try {

            Context contextqr= getApplicationContext();
            bitmap = babak.texttoqr(contextqr, datachunckreadytoshow, 500);
            qrholder.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), "Data:"+current_data_array_partnew+"/"+chunkarray.length, Toast.LENGTH_SHORT).show();
       // Toast.makeText(getApplicationContext(), "data:"+current_data_array_partnew+"/"+chunkarray.length+":"+userdatatoshowonqrfinal, Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.textprogress);
        textView.setText("Data:"+current_data_array_partnew+"/"+chunkarray.length);



    //    if(current_data_array_partnew >= chunkarray.length && chunkarray.length > 1) {
     //       globalVariable.set_current_data_array_part(0);
    //    }


        if(current_data_array_partnew < chunkarray.length) {

            new Timer().schedule(new TimerTask(){
                public void run() {
                    startActivity(new Intent(qrshow.this, qrshow.class));
                    finish();
                }
            }, 200 );


        } else{
            globalVariable.set_current_data_array_part(0);

            Button_Return1.setVisibility(View.VISIBLE);
            button_restart.setVisibility(View.VISIBLE);
            button_share1.setVisibility(View.VISIBLE);
           // restart();

        }













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





    public void share(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();
        Context context_share_txt= qrshow.this;
        babak.sharetxt(context_share_txt, "DigiSafeGuard Message", current_data_msg_for_qr, "Share DSG");
    }


/*
    public void restart(){
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_current_data_array_part(0);
        startActivity(new Intent(qrshow.this, qrshow.class));
        finish();
    }
*/




    public void startrefreh(View View){
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_current_data_array_part(0);
        startActivity(new Intent(qrshow.this, qrshow.class));
        finish();
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