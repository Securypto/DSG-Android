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
import android.widget.ImageView;
import android.widget.VideoView;


import com.google.zxing.WriterException;

import io.securypto.DSGV1.R;


public class qrshowpub extends AppCompatActivity {

    public static String EXTRA_MESSAGE1 = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrshowpub);


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
        String current_valt_Pub_key_final = "DigiSafeGuard-PUBLIC-KEY:" + current_valt_Pub_key;









        ImageView qrholder;
        Bitmap bitmap ;
        qrholder = (ImageView)findViewById(R.id.qrholder);
        try {

            Context contextqr= getApplicationContext();
            bitmap = babak.texttoqr(contextqr, current_valt_Pub_key_final, 500);
            qrholder.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
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
        final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
        String current_valt_Pub_key_final = "DigiSafeGuard-PUBLIC-KEY:" + current_valt_Pub_key;
        Context context_share_txt= qrshowpub.this;
        babak.sharetxt(context_share_txt, "DigiSafeGuard Pub Key", current_valt_Pub_key_final, "Share DSG");
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