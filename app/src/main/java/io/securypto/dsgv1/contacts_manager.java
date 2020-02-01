package io.securypto.dsgv1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import io.securypto.DSGV1.BuildConfig;
import io.securypto.DSGV1.R;



public class contacts_manager extends Activity {

    Button btn;
    private ClipboardManager myClipboard;
    private ClipData myClip;


    MediaRecorder recorder = null;






    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_contacts_manager_page);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String mode = settings.getString("mode", "").toString();

        //Toast.makeText(getApplicationContext(), "mode:"+mode, Toast.LENGTH_SHORT).show();

        Button button_import_contact = (Button) findViewById(R.id.button_import_contact);


        if(settings.getString("mode", "").equals("On"))
        {


        }

        else{
//            button_import_contact.setVisibility(View.GONE);

        }






        String []allefiles = getApplicationContext().fileList();
        //filterout array contain only what yo want
        List<String> listValue = new ArrayList<>(); //a LinkedList may be more appropriate


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();


        for (String each_file_name : allefiles) {
           // listValue.add(each_file_name);


            if(babak.is_it_a_contact(each_file_name, vault_name_short))
            {

                //Toast.makeText(getApplicationContext(), each_file_name, Toast.LENGTH_SHORT).show();

                each_file_name=each_file_name.replace("DSG_CONTACTS_","");
                each_file_name=each_file_name.replace("_publicKey","");
                each_file_name=each_file_name.replace(vault_name_short+"_","");



                //get the real name
                try {
                    //recover false name to dec daarna
                    each_file_name = new String(Base64.decode(each_file_name, Base64.DEFAULT), StandardCharsets.UTF_8);

                    //dec naam using AES
                    each_file_name = AESCrypt.decrypt(vault_passwd, each_file_name);


                    if(each_file_name != null) {

                        listValue.add(each_file_name);
                    }

                }catch(Exception e){}






            }

        }




        TextView textViewnocontact = findViewById(R.id.textViewnocontact);
        textViewnocontact.setVisibility(View.GONE);

        if (listValue.isEmpty()) {
            // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            textViewnocontact.setVisibility(View.VISIBLE);
        }





        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listValue);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.babaklistlayout, R.id.text1, listValue);
        ListView listView = (ListView)findViewById(R.id.listView1);

        listView.setAdapter(adapter);

        listView.setBackgroundResource(R.drawable.customshape);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                //Toast.makeText(contacts_manager.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                confirmDialogDemomultibutton(clickItemObj.toString());
            }
        });



        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));


        TextView textView = (TextView) findViewById(R.id.textViewnocontact);
        textView.setMovementMethod(new ScrollingMovementMethod());





    }




    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
    }













    private void recordaudio(final String user_pub_to_use_for_sending_msg) {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_tmp_data2("no");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Recording);
        builder.setMessage("");
        builder.setCancelable(false);

        builder.setMessage("00:10");

        builder.setNeutralButton(R.string.Stop_Recording, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                globalVariable.set_tmp_data2("yes");
                recorderstop(user_pub_to_use_for_sending_msg);

            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

//        builder.show();


        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
               // builder.setMessage("00:"+ (millisUntilFinished/1000));
                TextView tv_message = (TextView) alertDialog.findViewById(android.R.id.message);
                //tv_message.setText("00:"+ (millisUntilFinished/1000));

                long sectogo= (millisUntilFinished/1000);
                long hours = sectogo / 3600;
                long minutes = (sectogo % 3600) / 60;
                long seconds = sectogo % 60;

                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                tv_message.setText(timeString);
            }

            @Override
            public void onFinish() {
                String tmp_data2  = globalVariable.get_tmp_data2();
                if ("no".equals(tmp_data2)) {
                    recorderstop(user_pub_to_use_for_sending_msg);
                }
            }
        }.start();



        String fileName = getFilesDir()+"/audio_to_enc.3gp";

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();

        } catch (IOException e) {

        }

        recorder.start();






    }






        public void recorderstop(final String user_pub_to_use_for_sending_msg) {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;

            File file = new File(getFilesDir() + "/audio_to_enc.3gp");
            String base64ready =  babak.getStringFile(file);

            //clean internal
            babak.cleaninternal(getApplicationContext());

            String texttoenc="";

            // First create a rand string 20ch long to use as passwd to enc user data
            String randomstring = babak.randomAlphaNumeric(20);
            texttoenc= "audio_to_encBABAKSEPERATOR3gpBABAKSEPERATOR" + base64ready;

            //enc data using AES + just created randstring
            String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
            //enc the randstring using RSA key
            String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(user_pub_to_use_for_sending_msg, randomstring);
            String combinedmsg = "DSGMSGDSGSEPERATOR"+encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;


            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.set_current_data_msg_for_qr(combinedmsg);
            globalVariable.set_current_data_array_part(0);





            Intent myIntent2 = new Intent(getBaseContext(),   qrshow.class);
            startActivity(myIntent2);
            finish();



        }
    }







    private void sendfile(final String user_pub_to_use_for_sending_msg) {
        Toast.makeText(getApplicationContext(), R.string.SCU_DSG_under_construction, Toast.LENGTH_SHORT).show();
    }






//camera shot

    static final int REQUEST_IMAGE_CAPTURE = 1;


    private void takeapic() {


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            String imageFileName = "pic_from_camera_to_enc.jpg";

/*
            try {

                 File storageDir = getFilesDir();
                 File image = File.createTempFile( imageFileName, ".jpg", storageDir);
                 cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                 startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

             } catch (IOException ex) {
             //Error occurred while creating the File
             }
*/

            //Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
           // Uri photoURI = FileProvider.getUriForFile(this,BuildConfig.APPLICATION_ID + ".provider", "pic_from_camera_to_enc.jpg");
            // Uri photoURI = Uri.fromFile(new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),  imageFileName+".jpg"));


           // Uri photoURI = FileProvider.getUriForFile(this,
           //          getApplicationContext().getPackageName() + ".provider",
           //         new File(getFilesDir()+"/"+imageFileName+".jpg"));

            //Uri photoURI = Uri.fromFile(imageFileName);

            //File storageDir2 = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            //Toast.makeText(getApplicationContext(), storageDir2.toString(), Toast.LENGTH_SHORT).show();

            //File storageDir = getFilesDir();
            //File image = new File(storageDir, imageFileName + ".jpg");
           // File image = new File(getFilesDir() + "/"+imageFileName+".jpg");



            File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File image = new File(storageDir, imageFileName);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(getApplicationContext(), requestCode+":"+resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

             final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String user_pub_to_use_for_sending_msg  = globalVariable.get_tmp_contact_Pub_key();
            globalVariable.set_tmp_contact_Pub_key("");


            String imageFileName = "pic_from_camera_to_enc.jpg";

            File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/"+imageFileName);
            //File file = new File(getFilesDir() + "/"+imageFileName);

            //Toast.makeText(getApplicationContext(), file.toString(), Toast.LENGTH_SHORT).show();
            String base64ready =  babak.getStringFile(file);

            //clean downloaddir
            babak.cleandownloaddir(getApplicationContext());

            String texttoenc="";

            // First create a rand string 20ch long to use as passwd to enc user data
            String randomstring = babak.randomAlphaNumeric(20);
            texttoenc= "pic_from_camera_to_encBABAKSEPERATORjpgBABAKSEPERATOR" + base64ready;

            //enc data using AES + just created randstring
            String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
            //enc the randstring using RSA key
            String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(user_pub_to_use_for_sending_msg, randomstring);
            String combinedmsg = "DSGMSGDSGSEPERATOR"+encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;


           // final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            globalVariable.set_current_data_msg_for_qr(combinedmsg);
            globalVariable.set_current_data_array_part(0);






            Intent myIntent2 = new Intent(getBaseContext(),   qrshow.class);
            startActivity(myIntent2);
            finish();




        }else{
            Toast.makeText(getApplicationContext(), R.string.Coudnt_take_a_picture, Toast.LENGTH_SHORT).show();
        }
    }



// end camera shot




/*

    private static final int REQUEST_CAPTURE_IMAGE = 100;

    private void takeapic() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE
        );
        if(pictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pictureIntent,
                    REQUEST_CAPTURE_IMAGE);
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE &&
                resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                //mImageView.setImageBitmap(imageBitmap);

                String imageFileName = "pic_from_camera_to_enc.jpg";

                try  {
// Assume block needs to be inside a Try/Catch block.
                    OutputStream fOut = null;
                    Integer counter = 0;
                    File file = new File(getFilesDir(), imageFileName); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                    fOut = new FileOutputStream(file);

                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

*/




/*


    ImageView imageView;
    Uri image;
    String mCameraFileName;

    private void takeapic() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        String newPicFile = "pic_from_camera_to_enc";
        String outPath = getFilesDir() + "/"+newPicFile+".jpg";
        File outFile = new File(outPath);

        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        startActivityForResult(intent, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if (data != null) {
                    image = data.getData();
                   // imageView.setImageURI(image);
                   // imageView.setVisibility(View.VISIBLE);
                }
                if (image == null && mCameraFileName != null) {
                    image = Uri.fromFile(new File(mCameraFileName));
                   // imageView.setImageURI(image);
                   // imageView.setVisibility(View.VISIBLE);
                }
                File file = new File(mCameraFileName);
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
    }


*/












//singel confirm dialog

    private void confirmDialogDemo(final String selected_file_by_user) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.WARNING);
        builder.setMessage(R.string.Wipe_all_date);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

            //Context context_file_exist= getApplicationContext();

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Toast.makeText(getApplicationContext(), "Delete:"+selected_file_by_user+"", Toast.LENGTH_SHORT).show();

                //remove file
                getApplicationContext().deleteFile(selected_file_by_user);
                finish();
                startActivity(getIntent());

            }



        });



        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }









          private void confirmDialogDemomultibutton(final String selected_file_by_user){

              final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
              final String vault_name_short  = globalVariable.get_vault_name_short();
              final String vault_passwd  = globalVariable.get_vault_passwd();

              //enc desc using AES
              //String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, selected_file_by_user);

              //enc desc using AES
              String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, selected_file_by_user);

              //gen false name
              encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

              final String selected_file_by_userNOW="DSG_CONTACTS_"+vault_name_short+"_"+encrypted_desc_to_use_as_file_name+"_publicKey";




            // Each image in array will be displayed at each item beginning.
             int[] imageIdArr = {R.mipmap.sendmsg, R.mipmap.sendvoice, R.mipmap.sendpic, R.mipmap.sendfile, R.mipmap.showaddress, R.mipmap.delete};
            // Each item text.
             final String[] listItemArr = {getResources().getString(R.string.Text_Message), getResources().getString(R.string.Voice_Message), getResources().getString(R.string.Picture), getResources().getString(R.string.File), getResources().getString(R.string.Show_contact_address), getResources().getString(R.string.Delete)};

            // Image and text item data's key.
             final String CUSTOM_ADAPTER_IMAGE = "image";
             final String CUSTOM_ADAPTER_TEXT = "text";

                // Create a alert dialog builder.
                AlertDialog.Builder builder = new AlertDialog.Builder(contacts_manager.this);
                // Set icon value.
             //   builder.setIcon(R.mipmap.lockloader);
                // Set title value.
              //  builder.setTitle("Encryption options");

                // Create SimpleAdapter list data.
                List<Map<String, Object>> dialogItemList = new ArrayList<Map<String, Object>>();
                int listItemLen = listItemArr.length;
                for (int i = 0; i < listItemLen; i++) {
                    Map<String, Object> itemMap = new HashMap<String, Object>();
                    itemMap.put(CUSTOM_ADAPTER_IMAGE, imageIdArr[i]);
                    itemMap.put(CUSTOM_ADAPTER_TEXT, listItemArr[i]);

                    dialogItemList.add(itemMap);
                }

                // Create SimpleAdapter object.
                SimpleAdapter simpleAdapter = new SimpleAdapter(contacts_manager.this, dialogItemList,
                        R.layout.layout_dialog_select_action_by_contact,
                        new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                        new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});


                /*
                // Set the data adapter.
                builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ) {
                       // textViewTmp.setText("You choose item " + listItemArr[itemIndex]);
                        Toast.makeText(getApplicationContext(), listItemArr[itemIndex]+":"+selected_file_by_user, Toast.LENGTH_SHORT).show();
                    }
                });
                */



              // Set the data adapter.
              builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialogInterface, int which) {

                      // The 'which' argument contains the index position
                      // of the selected item

                      // dec a text that was enc by RSA this vault + AES
                      String inhoudtextencmode = babak.read_file(getApplicationContext(), selected_file_by_userNOW);
                      String final_msg = babak.dec_a_text_using_RSA_AND_AES(getApplicationContext(), inhoudtextencmode);
                      String final_msg_incl_type= "DigiSafeGuard-PUBLIC-KEY:" +final_msg;



                      switch (which) {
                          case 0:
                              Intent i = new Intent(getBaseContext(), write_a_msg.class);
                              i.putExtra("user_pub_to_use",final_msg);
                              startActivity(i);
                              break;
                          case 1:
                              recordaudio(final_msg);
                              break;
                          case 2:
                              final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                              globalVariable.set_tmp_contact_Pub_key(final_msg);
                              takeapic();
                              break;
                          case 3:
                              sendfile(final_msg);
                              break;
                          case 4:
                              Intent i3 = new Intent(getBaseContext(), qrshowpub.class);
                              i3.putExtra("qrdatatoshowonqrpage",final_msg_incl_type);
                              startActivity(i3);
                              break;
                          case 5:
                              confirmDialogDemo(selected_file_by_userNOW);
                              break;


                      }


                  }
              });


                builder.setCancelable(false);

/*
              builder.setNegativeButton("Delete Contact", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      confirmDialogDemo(selected_file_by_userNOW);
                  }
              });
*/



              builder.setPositiveButton(R.string.Cancel, new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                  }



              });






                builder.create();
                builder.show();


    }



















    public void importcontactdialogclip(final String contactpubtoimport){
        final EditText edtText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Contact_Import);
        builder.setMessage(R.string.Name);
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setNeutralButton(R.string.Save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                String contactpubtoimport2 = contactpubtoimport.replaceAll("DigiSafeGuard-PUBLIC-KEY:", "");

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

                //enc text using vault pub rsa key +  AES
                String enc_text_to_write = babak.enc_a_text_using_RSA_AND_AES(getApplicationContext(), contactpubtoimport2);
                //save the file
                babak.write(getApplicationContext(), filenametowrite, enc_text_to_write);



                Toast.makeText(getApplicationContext(), R.string.New_contact_has_been_saved, Toast.LENGTH_SHORT).show();

                finish();
                startActivity(getIntent());



            }
        });
        builder.show();
    }










    public void godirectlytoscannerpage(View View){
        Intent myIntent33ps = new Intent(getBaseContext(),   qrscanner.class);
        startActivity(myIntent33ps);
    }






    public void gotovaultpage(View View){
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   login_succes.class);
        startActivity(myIntentgotofirstpage);
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