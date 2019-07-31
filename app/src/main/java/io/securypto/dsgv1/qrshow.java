package io.securypto.dsgv1;



import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import io.securypto.DSGV1.R;


public class qrshow extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrshow);

        //Button Button_Return1 = (Button) findViewById(R.id.Button_Return1);
        Button button_share1 = (Button) findViewById(R.id.button_share1);
        Button button_share2 = (Button) findViewById(R.id.button_share2);
        VideoView v = (VideoView) findViewById(R.id.videoViewqr);
        ImageView qrholder = (ImageView) findViewById(R.id.qrholder);
        //Button_Return1.setVisibility(View.GONE);
        //button_share1.setVisibility(View.GONE);
        //button_share2.setVisibility(View.GONE);
       // v.setVisibility(View.GONE);
       // qrholder.setVisibility(View.GONE);


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();


        String[] chunkarray = babak.splitToNChar(current_data_msg_for_qr, 500);
        String userdatatoshowonqrfinal ="";
        String md5hash="";
        String datachunckreadytoshow="";
        int current_data_array_part_plus_one=0;
        String destinationFilename ="";


        ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();


        for(int i = 0; i< chunkarray.length; i++) {
            userdatatoshowonqrfinal = chunkarray[i];
            md5hash = babak.md5(userdatatoshowonqrfinal);
            current_data_array_part_plus_one=i+1;

            //include md5 hash
            // example  DSGMSGPART:2:5:hash:msg
            datachunckreadytoshow = "DSGMSGPART:" + current_data_array_part_plus_one + ":" + chunkarray.length + ":" + md5hash + ":" + userdatatoshowonqrfinal;



            Bitmap bitmap;
            try {

                Context contextqr = getApplicationContext();
                bitmap = babak.texttoqr(contextqr, datachunckreadytoshow, 500);
                bitmapArray.add(bitmap); // Add a bitmap


if(chunkarray.length == 1) {
    //ImageView qrholder;
    qrholder = (ImageView) findViewById(R.id.qrholder);
    qrholder.setImageBitmap(bitmap);


    try {
        //destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/qrmsgtosend"+current_data_array_part_plus_one+".jpg";
        destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/qrmsgtosend1.jpg";
        File newfile = babak.savebitmap(bitmap, destinationFilename);
    }
    catch(IOException e) {
        e.printStackTrace();
    }


    }


            } catch (WriterException e) {
                e.printStackTrace();
            }



        }







        if(chunkarray.length > 1) {
            button_share1.setVisibility(View.GONE);
            qrholder.setVisibility(View.GONE);

            try {

                FileChannelWrapper out = null;
                File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/qrmsgtosend.mp4");

                try {
                    out = NIOUtils.writableFileChannel(file.getAbsolutePath());


                    AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(1, 1));
                    for (Bitmap bitmap : bitmapArray) {
                        encoder.encodeImage(bitmap);

                    }


                    encoder.finish();

                } finally {
                    NIOUtils.closeQuietly(out);
                }




                new Thread(new Runnable() {
                    public void run() {
                        startvideoqr();
                    }
                }).start();



            } catch (IOException ex) {
                //Do something with the exception
            }

        }else{
            button_share2.setVisibility(View.GONE);
            v.setVisibility(View.GONE);

        }









              new Thread(new Runnable() {
                    public void run() {
                        startvideo();
                    }
                }).start();



    }








    public void startvideoqr() {

        VideoView v = (VideoView) findViewById(R.id.videoViewqr);

        Uri uri= Uri.parse(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/qrmsgtosend.mp4");
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




    public void shareimage(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();
        Context context_share_txt= qrshow.this;

        //share text
        //babak.sharetxt(context_share_txt, "DSG Encrypted Message", current_data_msg_for_qr, "Share DSG");

        //share file jpg
        String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/qrmsgtosend1.jpg";
        babak.sharefile(context_share_txt,  destinationFilename, "DSG Encrypted Message", "image/jpg");
    }


    public void sharevideo(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        String current_data_msg_for_qr = globalVariable.get_current_data_msg_for_qr();
        Context context_share_txt= qrshow.this;

        //share text
        //babak.sharetxt(context_share_txt, "DSG Encrypted Message", current_data_msg_for_qr, "Share DSG");

        //share file jpg
        String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/qrmsgtosend.mp4";
        babak.sharefile(context_share_txt,  destinationFilename, "DSG Encrypted Message", "video/mp4");
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