package io.securypto.dsgv1;



import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.WriterException;

import io.securypto.DSGV1.R;


public class qrshowdata extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrshowdata);


        //final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        Button btn = (Button) findViewById(R.id.btn);


//startqr();



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startqr();
            }
        });








        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }finally{
                    startqr();
                }
            }
        };
        timer.start();


















    }











    public void startqr() {

        String current_data_msg_for_qr="babak1babak2babak3babak4babak5";
        String[] chunkarray = babak.splitToNChar(current_data_msg_for_qr, 6);

        int i =0;
        for (String strTemp : chunkarray) {


            i++;
            String md5hash= babak.md5(strTemp);
            final String datachunckreadytoshow="DSGMSGPART:"+i+":"+chunkarray.length+":"+md5hash+":"+strTemp;


            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);


                    System.out.println(datachunckreadytoshow);


                    ImageView iv = (ImageView) findViewById(R.id.iv);
                    Bitmap bitmap ;
                    try {
                        Context contextqr= getApplicationContext();
                        bitmap = babak.texttoqr(contextqr, datachunckreadytoshow, 500);
                        iv.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }





        }

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