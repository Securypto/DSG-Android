package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

import io.securypto.DSGV1.R;

public class qrscanner extends AppCompatActivity {


    public int msgpartnumber = 0;
    public int msgparttotalamount = 0;
    public static String msghash = "";
    public static String msgitself = "";
    public static String md5hash = "";
    public static String totalmsgtogether="";
    int arrayfull = 0;

    private MediaPlayer   player = null;


    //max 1000 qr codes
    String[] datalist = new String[1000];

 //   final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
 //   final String vault_name  = globalVariable.get_vault_name();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());



        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


        setContentView(R.layout.qrscanner);




        msgpartnumber = 0;
        msgparttotalamount = 0;
        msghash = "";
        msgitself = "";
        md5hash = "";
        totalmsgtogether="";
        arrayfull = 0;
        String[] datalist = new String[1000];



        rescan();






    }



    /**
     * Prompt dialog demo
     * it is used when you want to capture user input
     */
    private void promptDialogDemo(final String scanresult) {
        final EditText edtText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Contact_Import);
        builder.setMessage(getResources().getString(R.string.Name));
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setNeutralButton(R.string.Save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                final String vault_name_short  = globalVariable.get_vault_name_short();
                final String vault_passwd  = globalVariable.get_vault_passwd();
                final String current_valt_Pub_key  = globalVariable.get_current_valt_Pub_key();

                String filenameingevoerddooruser = edtText.getText().toString();


                //enc desc using AES
                String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, filenameingevoerddooruser);
                //gen false name
                encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
                //final name to use as file name
                String filenametowrite = "DSG_CONTACTS_"+vault_name_short+"_"+encrypted_desc_to_use_as_file_name+"_publicKey";



                //enc text using vault pub rsa key +  AES
                String enc_text_to_write = babak.enc_a_text_using_RSA_AND_AES(getApplicationContext(), scanresult);
                //save the file
                babak.write(getApplicationContext(), filenametowrite, enc_text_to_write);


                TextView textViewmsg = findViewById(R.id.editTextmsg);
                textViewmsg.setText(R.string.New_contact_has_been_saved);


            }
        });



        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });



        builder.show();
    }












    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {



            if(result.getContents() == null) {
               // Log.e("Scan*******", "Cancelled scan");

            } else {
                //Log.e("Scan", "Scanned");
              //  Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();



                String resfromscan =result.getContents();

                String[] resfromscan_now = {"foo", "bar"};
                Arrays.fill(resfromscan_now, null);
                resfromscan_now = resfromscan.split("\\:", -1);

                if (resfromscan_now[0].equals("DigiSafeGuard-PUBLIC-KEY")) {

                    //iets a contact pub key
                    promptDialogDemo(resfromscan_now[1]);


                }

                else if (resfromscan_now[0].equals("DSGMSGPART") ) {












                    //its a message
                    // example  DSGMSGPART:2:5:hash:msg
                    msgpartnumber = Integer.parseInt(resfromscan_now[1]);
                    msgparttotalamount = Integer.parseInt(resfromscan_now[2]);
                    msghash = resfromscan_now[3];
                    msgitself = resfromscan_now[4];

                    //first calculate hash to check data transfer validation
                    md5hash= babak.md5(msgitself);
                    //md5hash="1";
                    if (msghash.equals(md5hash) && msghash.length() > 0) {
                        //hash is correct, lets go!

                        if(datalist[msgpartnumber] == null) {

                           // Toast.makeText(this, "Add to array:"+arrayfull+":"+msgparttotalamount, Toast.LENGTH_LONG).show();
                            datalist[msgpartnumber] = msgitself;

                            Toast.makeText(this, "Received: "+(arrayfull+1)+"/"+msgparttotalamount, Toast.LENGTH_LONG).show();

                        }




                        arrayfull = 1;
                        for(int i = 1; i< (msgparttotalamount); i++){
                            //if (datalist[i] == null || datalist[i] == "null" ) {
                            if (datalist[i] == null) {
                              //  Toast.makeText(this, "Opzoek naar:"+i, Toast.LENGTH_LONG).show();
                                rescan();
                            }
                            else
                            {
                                arrayfull++;
                            }
                        }





                        if(arrayfull == msgparttotalamount) {

                            for (int i = 1; i < (msgparttotalamount + 1); i++) {
                                totalmsgtogether = totalmsgtogether + datalist[i];
                            }


                            //totalmsgtogether = totalmsgtogether.replaceAll("null", "");


                            String[] msgstukken = totalmsgtogether.split("DSGSEPERATOR", -1);
                            String msgtype = msgstukken[0];
                            String keyfordecryption = msgstukken[1];
                            String msgtodecrypte = msgstukken[2];

                            //first dec the key using our own pub
                            String decryptedaeskey = encclass.decryptRSAToString(current_valt_Priv_key, keyfordecryption);
                            //now decrypte the sended data
                            String final_msg = AESCrypt.decrypt(decryptedaeskey, msgtodecrypte);


                            //need for files
                            String[] msgstukkenreadready = final_msg.split("BABAKSEPERATOR", -1);



                            if (msgtype.equals("DSGMSG") && final_msg != null) {
                            // we could decrypted, determine what we have now...


                                if ("audio_to_enc".equals(msgstukkenreadready[0]) && "3gp".equals(msgstukkenreadready[1])) {
                                    //its a voice message, save it to test

                                    globalVariable.set_tmp_data1(msgstukkenreadready[2]);
                                    asktoplayaudio();






                                }else{

                                    //its a text
                                    //its a message
                                    //TextView textViewmsg = findViewById(R.id.editTextmsg);
                                    //textViewmsg.setText(final_msg);

                                    Intent intentq = new Intent(getBaseContext(), showfromclip.class);
                                    Bundle extras = new Bundle();
                                    extras.putString("EXTRA_DEC_MSG_FROM_CLIP", final_msg);
                                    intentq.putExtras(extras);
                                    startActivity(intentq);
                                }










}
else
{
    TextView textViewmsg = findViewById(R.id.editTextmsg);
    textViewmsg.setText(R.string.Not_a_valid_message);
}



                        }




                    }
                    else
                    {
                        Toast.makeText(this, "Wrong hash!", Toast.LENGTH_LONG).show();
rescan();
                    }


















                }


              //  else
                {
                    if(arrayfull != msgparttotalamount) {
                        rescan();
                     //  Toast.makeText(this, "Here we go again!", Toast.LENGTH_LONG).show();
                    }
                }












            }



        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
           // System.out.println("thank 2" + result.getContents() );

        }


    }




    public void rescan(){

        IntentIntegrator integrator = new IntentIntegrator(qrscanner.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(getResources().getString(R.string.Scanning___));
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
          }



    public void asktoplayaudio() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.You_have_a_Voice_message);
        builder.setMessage(R.string.Do_you_want_me_to_play_it);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {



                Intent myIntent56at = new Intent(getBaseContext(), showaudio.class);
                startActivity(myIntent56at);


            }

        });



        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
                //clean internal
                //babak.cleaninternal(getApplicationContext());
                Intent myIntent56at = new Intent(getBaseContext(), login_succes.class);
                startActivity(myIntent56at);

            }
        });

        builder.show();
    }




    public void gotologinsuccespage23(View View){
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

