package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import io.securypto.DSGV1.R;

public class qrscanner extends AppCompatActivity {


    public int msgpartnumber = 0;
    public int msgparttotalamount = 0;
    public static String msghash = "";
    public static String msgitself = "";
    public static String md5hash = "";
    public static String totalmsgtogether="";
    int arrayfull = 0;
    //max 1000 qr codes
    String[] datalist = new String[1000];

 //   final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
 //   final String vault_name  = globalVariable.get_vault_name();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
        builder.setTitle("Contact Import");
        builder.setMessage("Contact Name");
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setNeutralButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                final String vault_name_short  = globalVariable.get_vault_name_short();
                final String vault_passwd  = globalVariable.get_vault_passwd();

                String filenameingevoerddooruser = edtText.getText().toString();

                //enc desc using AES
                //String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, filenameingevoerddooruser);

                //enc desc using AES
                String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, filenameingevoerddooruser);

                //gen false name
                encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);



                String filenametowrite = "DSG_CONTACTS_"+vault_name_short+"_"+encrypted_desc_to_use_as_file_name+"_publicKey";


                Context context_scanner_page = getApplicationContext();
                babak.write(context_scanner_page, filenametowrite,scanresult);

                TextView textViewmsg = findViewById(R.id.editTextmsg);
                textViewmsg.setText("New contact has been saved!");


            }
        });



        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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





                        if(arrayfull == msgparttotalamount)
                        {

                            for(int i = 1; i< (msgparttotalamount+1); i++){
                               totalmsgtogether= totalmsgtogether+ datalist[i];
                            }


                            //totalmsgtogether = totalmsgtogether.replaceAll("null", "");


                            String[] msgstukken = totalmsgtogether.split("\\DSGSEPERATOR", -1);





                            String msgtype =msgstukken[0];
                    String keyfordecryption =msgstukken[1];
                    String msgtodecrypte =msgstukken[2];
                    //first dec the key using our own pub
                    String decryptedaeskey = encclass.decryptRSAToString(current_valt_Priv_key, keyfordecryption);
                    //now decrypte the sended data
                    String final_msg = AESCrypt.decrypt(decryptedaeskey, msgtodecrypte);



                            //String  combinedmsg = " ALLES: "+totalmsgtogether+" unenc key: "+keyfordecryption+" unenc msg: "+msgtodecrypte+" dec key: "+decryptedaeskey;
                           // Context context_write_external = getApplicationContext();
                          //  babak.write_external(context_write_external, "encmsgreceived.txt",combinedmsg);

    if (msgtype.equals("DSGMSG") && final_msg != null) {
    TextView textViewmsg = findViewById(R.id.editTextmsg);
    textViewmsg.setText(final_msg);
    // Toast.makeText(this, "Complete, Decrypting the Message!"+final_msg, Toast.LENGTH_LONG).show();
}
else
{
    TextView textViewmsg = findViewById(R.id.editTextmsg);
    textViewmsg.setText("Not a valid message!");
}



                        }




                    }
                    else
                    {
                        Toast.makeText(this, "wrong hash", Toast.LENGTH_LONG).show();
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
        integrator.setPrompt("Scanning...");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
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

