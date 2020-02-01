package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.widget.VideoView;

import java.util.Locale;

import io.securypto.DSGV1.R;

public class login_succes extends AppCompatActivity {

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static String EXTRA_MESSAGE1 = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences("lang", 0);
        String languageToLoad  = settings.getString("lang", ""); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());



        setContentView(R.layout.activity_login_succes);


        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);




        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

     //   SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String mode = settings.getString("mode", "").toString();

        //Toast.makeText(getApplicationContext(), "mode:"+mode, Toast.LENGTH_SHORT).show();

        Button button_read_a_message = (Button) findViewById(R.id.button_read_a_message);
        Button button_contactmanager = (Button) findViewById(R.id.button_contactmanager);
        Button button_send_a_message = (Button) findViewById(R.id.button_send_a_message);
        //ImageView imgbuttoncamera = (ImageView) findViewById(R.id.imgbuttoncamera);
        //ImageView imgbuttoncontact = (ImageView) findViewById(R.id.imgbuttoncontact);

        ImageButton imgbuttoncamera = (ImageButton) findViewById(R.id.imgbuttoncamera);
        ImageButton imgbuttoncontact = (ImageButton) findViewById(R.id.imgbuttoncontact);

        if(settings.getString("mode", "").equals("On"))
        {
//            imgbuttoncamera.setVisibility(View.GONE);
//            imgbuttoncontact.setVisibility(View.GONE);

            imgbuttoncamera.setVisibility(View.GONE);
            imgbuttoncontact.setVisibility(View.GONE);


        }

        else{
            button_read_a_message.setVisibility(View.GONE);
            button_contactmanager.setVisibility(View.GONE);
            button_send_a_message.setVisibility(View.GONE);


        }












    }



    @Override
    public void onResume(){
        super.onResume();


        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));


    }
















    public void gotopageqrscanner(View View){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setItems(new CharSequence[]
                            {getResources().getString(R.string.Read_by_QR_Code), getResources().getString(R.string.Read_from_Clipboard), getResources().getString(R.string.Manual_Input), getResources().getString(R.string.Cancel)},
//                            {"Scan QR Code", "Read from Clipboard", "Cancel"},
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
                                            Toast.makeText(getBaseContext(), R.string.Not_a_valid_message, Toast.LENGTH_LONG).show();
                                        }


                                    }catch (Exception e) {
                                        // Handle the error/exception
                                        Toast.makeText(getBaseContext(), R.string.Not_a_valid_message, Toast.LENGTH_LONG).show();
                                    }








                                    break;
                                case 2:
                                    Intent myIntent333316w = new Intent(getBaseContext(),   decmanual.class);
                                    startActivity(myIntent333316w);
                                    break;


                                case 3:
                                    finish();
                                    startActivity(getIntent());
                                    break;

                            }
                        }
                    });
            builder.create().show();



    }










    public void loading(View View){
                Intent myIntentloading = new Intent(getBaseContext(),   qrshowpub.class);
                startActivity(myIntentloading);

    }








    public void godirectlytoscanner(View View){
        Intent myIntent33ps = new Intent(getBaseContext(),   qrscanner.class);
        startActivity(myIntent33ps);
    }



    public void gotopasswdmanager(View View){
        Intent myIntent33p = new Intent(getBaseContext(),   passwdmanager.class);
        startActivity(myIntent33p);
    }



    /*
    public void gotopageqrshow(View View){
        Intent myIntent33 = new Intent(getBaseContext(),   qrshowpub.class);
        startActivity(myIntent33);
    }
*/


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


      //  Intent myIntent33331 = new Intent(getBaseContext(),   firstpage.class);
      //  startActivity(myIntent33331);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);

    }







    public void gotohelppage44(View View){
        Intent myIntent333316 = new Intent(getBaseContext(),   walletspage.class);
      //  Intent myIntent333316 = new Intent(getBaseContext(),   scu_wallet.class);
        startActivity(myIntent333316);


/*
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Crypto_Wallet);
        builder.setMessage(R.string.SCU_DSG_under_construction);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                //finish();
                //startActivity(getIntent());
            }
        });
        builder.show();
*/


    }






    public void gotoarchive(View View){
        Intent myIntent333316c = new Intent(getBaseContext(),   archive.class);
        startActivity(myIntent333316c);


    }

    public void goto_settings(View View){
        Intent myIntent2s = new Intent(getBaseContext(),   settings.class);
        startActivity(myIntent2s);
    }

    public void gotohelppage(View View){
        Intent myIntent2sh = new Intent(getBaseContext(),   help.class);
        startActivity(myIntent2sh);
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
