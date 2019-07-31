package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.VideoView;

import io.securypto.DSGV1.R;

public class login_succes extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static String EXTRA_MESSAGE1 = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_succes);


        startvideo();


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();
        if(current_valt_Priv_key == null)
        {
         //   Toast.makeText(getBaseContext(), "Please open the vault.", Toast.LENGTH_LONG).show();
            Intent myIntent33331a = new Intent(getBaseContext(),   firstpage.class);
            startActivity(myIntent33331a);
        }





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













    public void gotopageqrscanner(View View){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setItems(new CharSequence[]
//                            {"Read by QR Code", "Read from Clipboard", "Manual Input", "Cancel"},
                            {"Scan QR Code", "Read from Clipboard", "Cancel"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    Intent myIntent333 = new Intent(getBaseContext(),   qrscanner.class);
                                    startActivity(myIntent333);
                                    break;
                                case 1:




                                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                    final String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();


                                    try {


//read clip
                                        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                                        ClipData abc = myClipboard.getPrimaryClip();
                                        ClipData.Item item = abc.getItemAt(0);
                                        String textfromclipboard = item.getText().toString();
//reset clip
//                                    ClipData myClip;
//                                    String text = "";
//                                    myClip = ClipData.newPlainText("text", text);
//                                    myClipboard.setPrimaryClip(myClip);



                                        String[] msgstukken = textfromclipboard.split("\\DSGSEPERATOR", -1);
                                        String msgtype = msgstukken[0];
                                        String keyfordecryption = msgstukken[1];
                                        String msgtodecrypte = msgstukken[2];

                                        //first dec the key using our own pub
                                        String decryptedaeskey = encclass.decryptRSAToString(current_valt_Priv_key, keyfordecryption);
                                        //now decrypte the sended data
                                        String final_msg = AESCrypt.decrypt(decryptedaeskey, msgtodecrypte);


                                        if (msgtype.equals("DSGMSG") && final_msg != null) {


                                            // Toast.makeText(this, "1 "+final_msg, Toast.LENGTH_LONG).show();
                                            Intent intentq = new Intent(getBaseContext(), showfromclip.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("EXTRA_DEC_MSG_FROM_CLIP",final_msg);
                                            intentq.putExtras(extras);
                                            startActivity(intentq);


                                        }
                                        else
                                        {
                                            Toast.makeText(getBaseContext(), "Not a valid message!", Toast.LENGTH_LONG).show();
                                        }


                                    }catch (Exception e) {
                                        // Handle the error/exception
                                        Toast.makeText(getBaseContext(), "Not a valid message!", Toast.LENGTH_LONG).show();
                                    }








                                    break;
                          //      case 2:
                           //         Intent myIntent333316w = new Intent(getBaseContext(),   decmanual.class);
                           //         startActivity(myIntent333316w);
                           //         break;


                                case 2:
                                    finish();
                                    startActivity(getIntent());
                                    break;

                            }
                        }
                    });
            builder.create().show();



    }










    public void loading(View View){
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
                Intent myIntentloading = new Intent(getBaseContext(),   qrshowpub.class);
                startActivity(myIntentloading);
            }
        }, 500);
    }












    public void gotopasswdmanager(View View){
        Intent myIntent33p = new Intent(getBaseContext(),   passwdmanager.class);
        startActivity(myIntent33p);
    }


    public void gotopageqrshow(View View){
        Intent myIntent33 = new Intent(getBaseContext(),   qrshowpub.class);
        startActivity(myIntent33);
    }

    public void gotopagecontactmanager(View View){
        Intent myIntent3333 = new Intent(getBaseContext(),   contacts_manager.class);
        startActivity(myIntent3333);
    }


    public void gotofirstpageagain(View View){

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_vault_name("");
        globalVariable.set_vault_name_short("");
        globalVariable.set_vault_passwd("");
        globalVariable.set_current_valt_Pub_key("");
        globalVariable.set_current_valt_Priv_key("");

        Intent myIntent33331 = new Intent(getBaseContext(),   firstpage.class);
        startActivity(myIntent33331);
    }




    public void gotohelppage44(View View){
        Intent myIntent333316 = new Intent(getBaseContext(),   wallet.class);
        startActivity(myIntent333316);
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


/*
    @Override
    public void onResume(){
        super.onResume();
        Intent myIntent33331ar = new Intent(getBaseContext(),   firstpage.class);
        startActivity(myIntent33331ar);
    }
*/


}
