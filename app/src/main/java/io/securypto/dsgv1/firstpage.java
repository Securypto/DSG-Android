package io.securypto.dsgv1;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import android.content.ClipboardManager;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import io.securypto.DSGV1.R;


public class firstpage extends AppCompatActivity {

    private ProgressDialog dialog2;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    public static String passwd_to_login_value = "a";
    public static String account_to_login_value = "b";
    public static String textfromclipboard="";

    String[] msgstukken ;
    String msgtype;

    private MediaPlayer   player = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {





//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);










        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        super.onCreate(savedInstanceState);





        SharedPreferences settings = getSharedPreferences("lang", 0);

        if(settings.getString("lang", "").equals(""))
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("lang","en");
            editor.commit();
        }



        String languageToLoad  = settings.getString("lang", ""); // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());








        setContentView(R.layout.activity_firstpage);




        //ask for permissions
        permissions_note(getApplicationContext());

        babak.startvideointro(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //clean everything
        babak.cleandownloaddir(getApplicationContext());
        babak.cleaninternal(getApplicationContext());

        onSharedIntent();



        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        if (Intent.ACTION_MAIN.equals(receivedAction)) {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();
            String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
            if (current_valt_Priv_key != null && current_valt_Pub_key != null && current_valt_Priv_key.length() > 10 && current_valt_Pub_key.length() > 10) {
                Intent myIntent56a = new Intent(getBaseContext(), login_succes.class);
                startActivity(myIntent56a);
            }
        }





        helping_hand_knop_page_new_installation();






    }


    @Override
    public void onResume(){
        super.onResume();


        //ask for permissions
        permissions_note(getApplicationContext());

        //play bg
        babak.startvideointro(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //clean everything
        //babak.cleandownloaddir(getApplicationContext());
        //babak.cleaninternal(getApplicationContext());



        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
//        Toast.makeText(getApplicationContext(), "Action"+receivedAction, Toast.LENGTH_SHORT).show();
//        if (Intent.ACTION_MAIN.equals(receivedAction)) {
        if (Intent.ACTION_MAIN.equals(receivedAction) || (receivedAction == null)) {
            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();
            String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
            if (current_valt_Priv_key != null && current_valt_Pub_key != null && current_valt_Priv_key.length() > 10 && current_valt_Pub_key.length() > 10) {
                Intent myIntent56a = new Intent(getBaseContext(), login_succes.class);
                startActivity(myIntent56a);
            }
        }



        helping_hand_knop_page_new_installation();



    }





    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        //ask for permissions
        permissions_note(getApplicationContext());

        //play bg
        babak.startvideointro(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //clean everything
        babak.cleandownloaddir(getApplicationContext());
        babak.cleaninternal(getApplicationContext());

        onSharedIntent();



        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        if (Intent.ACTION_MAIN.equals(receivedAction)) {
                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();
                String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
                if (current_valt_Priv_key != null && current_valt_Pub_key != null && current_valt_Priv_key.length() > 10 && current_valt_Pub_key.length() > 10) {
                    Intent myIntent56a = new Intent(getBaseContext(), login_succes.class);
                    startActivity(myIntent56a);
            }
        }






    }








    public void onSharedIntent() {
        final Intent receiverdIntent = getIntent();
        final String receivedAction = receiverdIntent.getAction();
        final String receivedType = receiverdIntent.getType();

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();
        final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();





                if (Intent.ACTION_SEND.equals(receivedAction)) {


                final Uri receiveUri = (Uri) receiverdIntent.getParcelableExtra(Intent.EXTRA_STREAM);
                final String nameoffile = getFileName(receiveUri);
                final String destinationFilename = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + nameoffile;

                if (receiveUri != null) {


                    // save file
                    babak.saveFile(getApplicationContext(), nameoffile, receiveUri, destinationFilename);
                    // read file
                    final String inhoudfile = babak.read_file_external_no_line_break(destinationFilename);


                if (current_valt_Priv_key != null && current_valt_Pub_key != null && current_valt_Priv_key.length() > 10 && current_valt_Pub_key.length() > 10 && !"".equals(inhoudfile) ) {



                    dialog2 = new ProgressDialog(this);
                    dialog2.setMessage(getResources().getString(R.string.Decrypting_in_progress));
                    dialog2.setCancelable(false);
                    dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog2.show();


                    new Thread() {
                        public void run() {






                            final String decoded = babak.qr_file_to_text_and_dec_to_get_msg(getApplicationContext(), destinationFilename);
                            final String decoded2 = babak.qr_file_to_text_and_dec_to_get_vault_address(destinationFilename);
                            final String[] msgstukken2 = inhoudfile.split("DSGSEPERATOR", -1);
                            final String final_msg_from_file = babak.dec_a_text_using_RSA_AND_AES_full_text(getApplicationContext(), inhoudfile);




                            if (decoded != null) {
                                //bij msg qr
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // Update UI elements

                                        dialog2.dismiss();
                                        Intent intentq = new Intent(getBaseContext(), showfromclip.class);
                                        Bundle extras = new Bundle();
                                        extras.putString("EXTRA_DEC_MSG_FROM_CLIP", decoded);
                                        intentq.putExtras(extras);
                                        startActivity(intentq);
                                        //Toast.makeText(getApplicationContext(), decoded, Toast.LENGTH_SHORT).show();


                                    }
                                });

                            } else if (decoded2 != null) {
                                //bij pubkey qr
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                        dialog2.dismiss();
                                        addcontactdialog(decoded2);
                                        //Toast.makeText(getApplicationContext(), decoded2, Toast.LENGTH_SHORT).show();


                                    }
                                });

                            } else if (msgstukken2[0].equals("DSGMSG") && final_msg_from_file != null) {
                                //bij file
                                runOnUiThread(new Runnable() {
                                    public void run() {


                                        //we could decrypted, determine what we have now...


                                            String[] msgstukkenreadready = final_msg_from_file.split("BABAKSEPERATOR", -1);


                                        if ("audio_to_enc".equals(msgstukkenreadready[0]) && "3gp".equals(msgstukkenreadready[1])) {
                                            //its a voice message, save it to test


                                            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                            globalVariable.set_tmp_data1(msgstukkenreadready[2]);



                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    dialog2.dismiss();
                                                }
                                            });

                                            asktoplayaudio();





                                        }else if ("pic_from_camera_to_enc".equals(msgstukkenreadready[0]) && "jpg".equals(msgstukkenreadready[1])) {

                                            //its a pic message

                                            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                            globalVariable.set_tmp_data1(msgstukkenreadready[2]);

                                            runOnUiThread(new Runnable() {
                                                public void run() {
                                                    dialog2.dismiss();
                                                }
                                            });


                                            Intent myIntent56atp = new Intent(getBaseContext(), showpic.class);
                                            startActivity(myIntent56atp);



                                        }else {

                                            dialog2.dismiss();
                                            Intent intentq = new Intent(getBaseContext(), showfromclip.class);
                                            Bundle extras = new Bundle();
                                            extras.putString("EXTRA_DEC_MSG_FROM_CLIP", final_msg_from_file);
                                           // extras.putString("EXTRA_DEC_MSG_FROM_CLIP", msgstukkenreadready[0]+"."+msgstukkenreadready[1]);

                                            intentq.putExtras(extras);
                                            startActivity(intentq);

                                        }




                                    }
                                });


                            } else if (babak.is_it_a_backup_file(nameoffile)) {
                                //bij backup
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog2.dismiss();
                                        Intent myIntent33331asbn = new Intent(getBaseContext(),   backup.class);
                                        startActivity(myIntent33331asbn);
                                        //Toast.makeText(getApplicationContext(), "Backup file has been imported. Go to Backup/Restore page to restore.", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } else if (msgstukken2[0].equals("DSGMSG") && final_msg_from_file == null ) {
                                //bij enc data thgatcant be decrypted

                                // final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                globalVariable.set_current_data_msg_for_qr(inhoudfile);
                                globalVariable.set_current_data_array_part(0);


                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dialog2.dismiss();
                                        Intent myIntent2 = new Intent(getBaseContext(),   qrshow.class);
                                        startActivity(myIntent2);

                                    }
                                });


                            }




                            else {
                                //dont know what to do



if(!"".equals(inhoudfile)) {


                                    runOnUiThread(new Runnable() {
                                        public void run() {

                                            //Toast.makeText(getApplicationContext(), "Need to encrypte...", Toast.LENGTH_SHORT).show();
                                            dialog2.dismiss();
                                            alertwhattodo();

                                        }
                                    });




                                }











                            }


                        }


                    }.start();


                } else {

                    //shared file but not logged in

                    if (babak.is_it_a_backup_file(nameoffile)) {
                        // save file
                        babak.saveFile(getApplicationContext(), nameoffile, receiveUri, destinationFilename);
                        Intent myIntent33331asb = new Intent(getBaseContext(),   backup.class);
                        startActivity(myIntent33331asb);
                        //Toast.makeText(getApplicationContext(), "Backup file has been imported. Go to Backup/Restore page to restore.", Toast.LENGTH_SHORT).show();
                        //dialog2.dismiss();
                    } else {
                        //dialog2.dismiss();
                        loginonthefly();
                    }


                }


            }


            } else if (Intent.ACTION_MAIN.equals(receivedAction)) {

                // Log.e(TAG, "onSharedIntent: nothing shared" );
                // nothing shared, but if logged in, redirect

                if(current_valt_Priv_key != null && current_valt_Pub_key != null && current_valt_Priv_key.length() > 10 && current_valt_Pub_key.length() > 10)
                {
                    //   Toast.makeText(getBaseContext(), "Please open the vault.", Toast.LENGTH_LONG).show();
                    Intent myIntent33331as = new Intent(getBaseContext(),   login_succes.class);
                    startActivity(myIntent33331as);
                }



            }

          //  getIntent().removeExtra("key");

    }


    private void alertwhattodo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File received!");
        builder.setMessage("I got an file that I coudn't decrypte...");
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }








    public void startvideo() {

        // correct convert
        //ffmpeg -i introorg1.mp4 -an -vcodec libx264 -crf 26 -s 800x480 intro1.mp4

        VideoView v = (VideoView) findViewById(R.id.videoView);
        Uri uri= Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.intro3);
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






//singel confirm dialog

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












    /** Called when the user taps the Send button */
    public void sendpasswd(View view) {


        EditText account_to_login = (EditText) findViewById(R.id.account_to_login);
        String vault_name = account_to_login.getText().toString();

        EditText passwd_to_login = (EditText) findViewById(R.id.passwd_to_login);
        String vault_passwd = passwd_to_login.getText().toString();

        if (!babak.validate_vault_passwd(vault_passwd) || !babak.validate_vault_name(vault_name)) {
            Toast.makeText(firstpage.this, R.string.Vault_Name_or_Password_cant_be_empty, Toast.LENGTH_LONG).show();
        } else {


            Context context_checkifusercanlogin = getApplicationContext();
            String checkvalidlogin=babak.checkifusercanlogin(context_checkifusercanlogin, vault_passwd, vault_name);
            if (checkvalidlogin.equals("yes")) {

                Intent myIntent56a = new Intent(getBaseContext(),   login_succes.class);
                startActivity(myIntent56a);


            } else {
                // Be really, really sad
                Toast.makeText(this, R.string.Wrong_credentials, Toast.LENGTH_LONG).show();

            }





        }

    }




    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }





    private void addcontactdialog(final String contactpubkeytosave) {
        final EditText edtText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Contact_Import);
        builder.setMessage(R.string.Name);
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

                if(filenameingevoerddooruser.length() > 0) {
                    //enc desc using AES
                    String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, filenameingevoerddooruser);
                    //gen false name
                    encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
                    //final name to use as file name
                    String filenametowrite = "DSG_CONTACTS_" + vault_name_short + "_" + encrypted_desc_to_use_as_file_name + "_publicKey";


                    //enc text using vault pub rsa key +  AES
                    String enc_text_to_write = babak.enc_a_text_using_RSA_AND_AES(getApplicationContext(), contactpubkeytosave);
                    //save the file
                    babak.write(getApplicationContext(), filenametowrite, enc_text_to_write);


                    Toast.makeText(getApplicationContext(), R.string.New_contact_has_been_saved, Toast.LENGTH_SHORT).show();
                    Intent myIntent56ac = new Intent(getBaseContext(), contacts_manager.class);
                    startActivity(myIntent56ac);

                }else{
                    Toast.makeText(firstpage.this, R.string.Contact_name_cant_be_empty, Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(getIntent());
                }

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




//new methode login on fly



    public void loginonthefly(){



        final EditText edtText = new EditText(firstpage.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_new_contact,
                null, false);

        // You have to list down your form elements
        final EditText addcontactvaultname = (EditText) formElementsView.findViewById(R.id.popupvault);
        final EditText addcontactvaultpassword = (EditText) formElementsView.findViewById(R.id.popupvaultpasswd);


        AlertDialog.Builder builder = new AlertDialog.Builder(firstpage.this);
        builder.setView(formElementsView);
        builder.setTitle(R.string.Please_open_your_vault);
        builder.setMessage("");
        builder.setCancelable(false);


        builder.setNeutralButton(R.string.Open_The_Vault, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String addcontactvaultname1 = addcontactvaultname.getText().toString();
                String addcontactvaultpassword1  = addcontactvaultpassword.getText().toString();

                Context context_checkifusercanlogin = getApplicationContext();


                String checkvalidlogin=babak.checkifusercanlogin(context_checkifusercanlogin, addcontactvaultpassword1, addcontactvaultname1);
                if (checkvalidlogin.equals("yes")) {


                    finish();
                    startActivity(getIntent());

                }
                else
                {
                    // Be really, really sad
                    Toast.makeText(getApplicationContext(), R.string.Wrong_credentials, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                }

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

















    public void goto_page_new_installation(View View){
        Intent myIntent_page_new_installation = new Intent(getBaseContext(),   page_new_installation.class);
        startActivity(myIntent_page_new_installation);
    }





    public void goto_settings(View View){
        Intent myIntent2s = new Intent(getBaseContext(),   settings.class);
        startActivity(myIntent2s);
    }

    public void gotohelppage(View View){
        Intent myIntent2sh = new Intent(getBaseContext(),   help.class);
        startActivity(myIntent2sh);

  //      Intent myIntent2sh = new Intent(getBaseContext(),   filemanagerdsg.class);
 //       startActivity(myIntent2sh);


           //   Intent myIntent2sh = new Intent(getBaseContext(),   cryptowallet.class);
           //    startActivity(myIntent2sh);

    }


    public void gotobackup(View View){
        Intent myIntent24 = new Intent(getBaseContext(),   backup.class);
        startActivity(myIntent24);
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }









    public void permissions_note(final Context context) {

        final String[] PERMISSIONS = {
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.INTERNET,
                android.Manifest.permission.ACCESS_NETWORK_STATE
        };

        if (!hasPermissions(this, PERMISSIONS)) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.Permission_header);
            builder.setMessage(R.string.Permission_notes);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    int PERMISSION_ALL = 1;
                    ActivityCompat.requestPermissions(firstpage.this, PERMISSIONS, PERMISSION_ALL);
                    dialog.dismiss();

                }



            });

            builder.show();

        }
    }




    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }



























/*
    public void ask_for_permissions(Context context) {



        //persmissions
        boolean permission_Rexternal = babak.doesUserHavePermission_Rexternal(context);
        boolean permission_Wexternal = babak.doesUserHavePermission_Wexternal(context);
        boolean permission_audio = babak.doesUserHavePermission_audio(context);
        boolean permission_camera = babak.doesUserHavePermission_camera(context);
        boolean permission_internet = babak.doesUserHavePermission_internet(context);
        boolean permission_networkstate = babak.doesUserHavePermission_networkstate(context);
        // Toast.makeText(context, "Audio:" + permission_audio + "RExternal" + permission_Rexternal + "WExternal" + permission_Wexternal + "Camera" + permission_camera, Toast.LENGTH_SHORT).show();

        if (permission_Rexternal == false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }
        if (permission_Wexternal == false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }
        if (permission_audio == false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 200);
        }
        if (permission_camera == false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
        }
        if (permission_internet == false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 200);
        }
        if (permission_networkstate == false) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 200);
        }
    }

*/




    public void helping_hand_knop_page_new_installation() {

        TextView infofieldmsg = (TextView) findViewById(R.id.infofield);
        infofieldmsg.setVisibility(View.GONE);
        ImageView hand_knop_page_new_installation = findViewById(R.id.hand_knop_page_new_installation);
        hand_knop_page_new_installation.setVisibility(View.GONE);



        if (!babak.check_if_there_are_vaults_created(getApplicationContext())) {

            infofieldmsg.setText(getString(R.string.info_first_time_wallet));
            infofieldmsg.setVisibility(View.VISIBLE);
            hand_knop_page_new_installation.setVisibility(View.VISIBLE);

            Animation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setStartOffset(1000);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(50);
            hand_knop_page_new_installation.startAnimation(anim);


        }else{
            hand_knop_page_new_installation.clearAnimation();
        }

    }





}
