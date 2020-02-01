package io.securypto.dsgv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.SearchView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemSelectedListener;

import io.securypto.DSGV1.R;




public class archive extends AppCompatActivity implements SearchView.OnQueryTextListener {






    MediaRecorder recorder = null;

    private ProgressDialog dialog;

    SearchView searchView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_archive);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String archivemode ="text";

        ImageButton ImageButtonlinks = findViewById(R.id.button_select_archive_sort);

        if(settings.getString("archivemode", "").equals("Text & Notes"))
        {
            archivemode ="DSG_ARCHIVE_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendfile);
        }
        else if(settings.getString("archivemode", "").equals("Audio Files")){
            archivemode ="DSG_AUDIO_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendvoice);
        }
        else if(settings.getString("archivemode", "").equals("Pictures")){
            archivemode ="DSG_PICS_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendpic);
        }
        else {
            archivemode ="DSG_ARCHIVE_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendfile);
        }



final String archivemodefinal = archivemode;


        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Decrypting_in_progress));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();




        searchView = (SearchView) findViewById(R.id.searchView);


        new Thread() {
            public void run() {


        String []allefiles = getApplicationContext().fileList();
        //filterout array contain only what yo want
        final List<String> listValue = new ArrayList<>(); //a LinkedList may be more appropriate

        // listValue.add(manager_desc );

        for (String each_file_name : allefiles) {
            if(babak.is_it_a_archive_file(each_file_name, vault_name_short, archivemodefinal))
            {


                each_file_name=each_file_name.replace(archivemodefinal,"");
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






                runOnUiThread(new Runnable() {
                    public void run() {


        TextView textViewnoarchive = findViewById(R.id.textViewnoarchive);
        textViewnoarchive.setVisibility(View.GONE);

        if (listValue.isEmpty()) {
            textViewnoarchive.setVisibility(View.VISIBLE);
        }



                    }
                });


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, listValue);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.babaklistlayout, R.id.text1, listValue);
        final ListView listView = (ListView)findViewById(R.id.listView_archive);



                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements



        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(archive.this);

        listView.setBackgroundResource(R.drawable.customshape);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);



                //Toast.makeText(archive.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                confirmDialogDemomultibutton(clickItemObj.toString(), archivemodefinal);




            }
        });

                    }
                });




                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        dialog.dismiss();
                    }
                });



            }
        }.start();







        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));


        final String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();
        if(current_valt_Priv_key == null)
        {
            //   Toast.makeText(getBaseContext(), "Please open the vault.", Toast.LENGTH_LONG).show();
            Intent myIntent33331a = new Intent(getBaseContext(),   firstpage.class);
            startActivity(myIntent33331a);
        }








    }

















     @Override
public boolean onQueryTextSubmit(String query) {
         return false;
         }

         @Override
 public boolean onQueryTextChange(String newText) {
         String text = newText;
         adapter.getFilter().filter(text);
         return false;
         }





    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));
    }












    //singel confirm dialog with multi buttons
    private void confirmDialogDemomultibutton(final String selected_file_by_user, final String archivemodefinal) {



        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();

        //enc desc using AES
        String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, selected_file_by_user);

        //gen false name
        encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

        final String selected_file_by_userNOW=archivemodefinal+vault_name_short+"_"+encrypted_desc_to_use_as_file_name;



        int[] imageIdArr = {R.mipmap.sendfile, R.mipmap.delete, R.mipmap.cancel};
        final String[] listItemArr = {getResources().getString(R.string.Show_Record), getResources().getString(R.string.Delete), getResources().getString(R.string.Cancel)};
        final String CUSTOM_ADAPTER_IMAGE = "image";
        final String CUSTOM_ADAPTER_TEXT = "text";

        AlertDialog.Builder builder = new AlertDialog.Builder(archive.this);
        //   builder.setIcon(R.mipmap.lockloader);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(archive.this, dialogItemList,
                R.layout.layout_dialog_select_action_by_contact,
                new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});


        // Set the data adapter.
        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                switch (which) {
                    case 0:


                        if ("DSG_ARCHIVE_".equals(archivemodefinal)) {
                            Intent i23 = new Intent(getBaseContext(), show_edit_archive.class);
                            i23.putExtra("archivefiletoshow",selected_file_by_user);
                            startActivity(i23);
                            break;
                        }
                        else if ("DSG_AUDIO_".equals(archivemodefinal)) {
                            decandredirect(selected_file_by_userNOW, archivemodefinal, selected_file_by_user);
                            break;
                        }
                        else if ("DSG_PICS_".equals(archivemodefinal)) {
                            decandredirect(selected_file_by_userNOW, archivemodefinal, selected_file_by_user);
                            break;
                        }
                        else{
                            break;
                        }



                    case 1:
                        confirmDialogDemo(selected_file_by_userNOW);
                        break;


                    case 2:
                        break;


                }


            }
        });


        builder.setCancelable(false);

/*
              builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                  }
              });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }


        });
*/
        builder.create();
        builder.show();















    }















    private void decandredirect(final String selected_file_by_userNOW, final String archivemodefinal, final String selected_file_by_user) {






        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Decrypting_in_progress));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        new Thread() {
            public void run() {



                Context context1 = getApplicationContext();
                String inhoudtextunenc = babak.read_file(context1, selected_file_by_userNOW);

                String inhoudtextdecrypted = babak.dec_a_text_using_RSA_AND_AES(getApplicationContext(), inhoudtextunenc);

                GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                globalVariable.set_tmp_data1(inhoudtextdecrypted);



                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        dialog.dismiss();


                    }
                });



                if ("DSG_PICS_".equals(archivemodefinal)) {
                    Intent i23 = new Intent(getBaseContext(), showpic.class);
                    i23.putExtra("archivefiletoshow",selected_file_by_user);
                    startActivity(i23);
                }else if("DSG_AUDIO_".equals(archivemodefinal)) {
                    Intent i23 = new Intent(getBaseContext(), showaudio.class);
                    i23.putExtra("archivefiletoshow",selected_file_by_user);
                    startActivity(i23);
                }





            }
        }.start();






    }




















    private void confirmDialogDemo(final String selected_file_by_user) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.WARNING);
        builder.setMessage(R.string.data_wipe_confim);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

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












/*

    public void selectarchivesort(final View View){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Select_an_Archive);
        builder.setCancelable(false);
        builder.setItems(new CharSequence[]
                        {getResources().getString(R.string.Text_and_Notes), getResources().getString(R.string.Audio_Files), getResources().getString(R.string.Pictures)},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item

                        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                        SharedPreferences.Editor editor = settings.edit();

                        switch (which) {
                            case 0:
                                editor.putString("archivemode","Text & Notes");
                                editor.commit();
                                startActivity(new Intent(getBaseContext(),   archive.class));
                                break;

                            case 1:
                                editor.putString("archivemode","Audio Files");
                                editor.commit();
                                startActivity(new Intent(getBaseContext(),   archive.class));
                                break;

                            case 2:
                                editor.putString("archivemode","Pictures");
                                editor.commit();
                                startActivity(new Intent(getBaseContext(),   archive.class));
                                break;

                        }
                    }
                });


        builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }



*/






















        public void selectarchivesort(final View View){


        int[] imageIdArr = {R.mipmap.sendfile, R.mipmap.sendvoice, R.mipmap.sendpic};
        final String[] listItemArr = {getResources().getString(R.string.Text_and_Notes), getResources().getString(R.string.Audio_Files), getResources().getString(R.string.Pictures)};
        final String CUSTOM_ADAPTER_IMAGE = "image";
        final String CUSTOM_ADAPTER_TEXT = "text";

        AlertDialog.Builder builder = new AlertDialog.Builder(archive.this);
        //   builder.setIcon(R.mipmap.lockloader);
            builder.setTitle(R.string.Select_an_Archive);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(archive.this, dialogItemList,
                R.layout.layout_dialog_select_action_by_contact,
                new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});


        // Set the data adapter.
        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();

                switch (which) {

                                        case 0:
                        editor.putString("archivemode","Text & Notes");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(),   archive.class));
                        break;

                    case 1:
                        editor.putString("archivemode","Audio Files");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(),   archive.class));
                        break;

                    case 2:
                        editor.putString("archivemode","Pictures");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(),   archive.class));
                        break;


                }


            }
        });


        builder.setCancelable(false);

/*
              builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                  }
              });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }


        });
*/
        builder.create();
        builder.show();















    }















































    public void addnewarchive(View View){


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);


        if(settings.getString("archivemode", "").equals("Text & Notes"))
        {
            Intent gotoshow_edit_archive = new Intent(getBaseContext(),   show_edit_archive.class);
            startActivity(gotoshow_edit_archive);

        }
        else if(settings.getString("archivemode", "").equals("Audio Files")){
            recordaudio();

        }
        else if(settings.getString("archivemode", "").equals("Pictures")){

            //first ask for name and then take pic
            askfornameandthentakepic();

            //first take pic and then ask for name
            //takeapic();

        }
        else {
            Intent gotoshow_edit_archive = new Intent(getBaseContext(),   show_edit_archive.class);
            startActivity(gotoshow_edit_archive);

        }




    }



















    private void askfornameandthentakepic() {
        final EditText edtText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       // builder.setTitle(R.string.Save_as);
        builder.setMessage(getResources().getString(R.string.Save_as));
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setNeutralButton(R.string.Save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String filenameingevoerddooruser = edtText.getText().toString();
                if(filenameingevoerddooruser.length() > 0) {

                    final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                    globalVariable.set_tmp_data1(filenameingevoerddooruser);
                    wehaveanamenowtakethepic();


                }else{
                    Toast.makeText(archive.this, R.string.Name_cant_be_empty, Toast.LENGTH_LONG).show();
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







    //camera shot after we got the name

            static final int REQUEST_IMAGE_CAPTURE = 1;
            private void wehaveanamenowtakethepic() {

                //clean downloaddir
                babak.cleandownloaddir(getApplicationContext());

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    String imageFileName = "pic_from_camera_to_enc.jpg";



                    File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    File image = new File(storageDir, imageFileName);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);


                }
            }







            @Override
            protected void onActivityResult(int requestCode, int resultCode, Intent data) {
               // Toast.makeText(getApplicationContext(), requestCode+":"+resultCode, Toast.LENGTH_SHORT).show();
                if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {






//begin save

                                final ProgressDialog  dialog3 = new ProgressDialog(archive.this);
                                dialog3.setMessage(getResources().getString(R.string.Data_Encryption));
                                dialog3.setCancelable(false);
                                dialog3.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                dialog3.show();


                                new Thread() {
                                    public void run() {




                                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                                        final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
                                        final String vault_name_short  = globalVariable.get_vault_name_short();
                                        final String vault_passwd  = globalVariable.get_vault_passwd();

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
                                        texttoenc= base64ready;

                                        //enc data using AES + just created randstring
                                        String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
                                        //enc the randstring using RSA key
                                        String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(current_valt_Pub_key, randomstring);
                                        String combinedmsg = encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;


                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat mdformat = new SimpleDateFormat("y-M-d HH:mm:ss");
                                        String timetouse = mdformat.format(calendar.getTime());

                                       // String desc = filenameingevoerddooruser +" | "+timetouse;

                                        String desc  = globalVariable.get_tmp_data1() +" | "+timetouse;

                                        //enc desc using AES
                                        String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
                                        //gen false name
                                        encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

                                        String filenametowrite_archive = "DSG_PICS_" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;


                                        babak.write(getApplicationContext(), filenametowrite_archive, combinedmsg);


                                        //clean downloaddir
                                        babak.cleandownloaddir(getApplicationContext());


                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                // Update UI elements
                                                dialog3.dismiss();
                                                Intent myIntent333316c = new Intent(getBaseContext(), archive.class);
                                                startActivity(myIntent333316c);
                                                finish();
                                            }
                                        });


                                    }
                                }.start();







                                //end save











                }else{
                    Toast.makeText(getApplicationContext(), R.string.Coudnt_take_a_picture, Toast.LENGTH_SHORT).show();
                }
            }










/*


//camera shot

    static final int REQUEST_IMAGE_CAPTURE = 1;


    private void takeapic() {

        //clean downloaddir
        babak.cleandownloaddir(getApplicationContext());

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            String imageFileName = "pic_from_camera_to_enc.jpg";



            File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
            File image = new File(storageDir, imageFileName);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);


        }
    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // Toast.makeText(getApplicationContext(), requestCode+":"+resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {










            final EditText edtText = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Save as...");
            builder.setMessage(getResources().getString(R.string.Save_as));
            builder.setCancelable(false);
            builder.setView(edtText);
            builder.setNeutralButton(R.string.Save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    final String filenameingevoerddooruser = edtText.getText().toString();


                    if(filenameingevoerddooruser.length() > 0) {

//begin save

                        final ProgressDialog  dialog3 = new ProgressDialog(archive.this);
                        dialog3.setMessage(getResources().getString(R.string.Data_Encryption));
                        dialog3.setCancelable(false);
                        dialog3.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog3.show();


                        new Thread() {
                            public void run() {




            final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
            final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
            final String vault_name_short  = globalVariable.get_vault_name_short();
            final String vault_passwd  = globalVariable.get_vault_passwd();

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
            texttoenc= base64ready;

            //enc data using AES + just created randstring
            String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
            //enc the randstring using RSA key
            String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(current_valt_Pub_key, randomstring);
            String combinedmsg = encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;


            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("y-M-d HH:mm:ss");
            String timetouse = mdformat.format(calendar.getTime());

            String desc = filenameingevoerddooruser +" | "+timetouse;

            //enc desc using AES
            String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
            //gen false name
            encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

            String filenametowrite_archive = "DSG_PICS_" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;


            babak.write(getApplicationContext(), filenametowrite_archive, combinedmsg);




                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // Update UI elements
                                        dialog3.dismiss();
                                        Intent myIntent333316c = new Intent(getBaseContext(), archive.class);
                                        startActivity(myIntent333316c);
                                        finish();
                                    }
                                });


                            }
                        }.start();







                        //end save



                    }else{
                        Toast.makeText(archive.this, R.string.Name_cant_be_empty, Toast.LENGTH_LONG).show();
                        //clean downloaddir
                        babak.cleandownloaddir(getApplicationContext());

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











        }else{
            Toast.makeText(getApplicationContext(), R.string.Coudnt_take_a_picture, Toast.LENGTH_SHORT).show();
        }
    }





*/



// end camera shot





































    private void recordaudio() {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        globalVariable.set_tmp_data2("no");

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Recording);
        builder.setMessage("");
        builder.setCancelable(false);

        //builder.setMessage("00:10");

        builder.setNeutralButton(R.string.Stop_Recording, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                globalVariable.set_tmp_data2("yes");
                recorderstop();

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
                    recorderstop();
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







    public void recorderstop() {

        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;



            final EditText edtText = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Save as...");
            builder.setMessage(R.string.Save_as);
            builder.setCancelable(false);
            builder.setView(edtText);
            builder.setNeutralButton(R.string.Save, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    final String filenameingevoerddooruser = edtText.getText().toString();


                    if(filenameingevoerddooruser.length() > 0) {

//begin save

                       final ProgressDialog  dialog3 = new ProgressDialog(archive.this);
                        dialog3.setMessage(getResources().getString(R.string.Data_Encryption));
                        dialog3.setCancelable(false);
                        dialog3.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog3.show();

                        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                        final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
                        final String vault_name_short  = globalVariable.get_vault_name_short();
                        final String vault_passwd  = globalVariable.get_vault_passwd();

                        new Thread() {
                            public void run() {




                                File file = new File(getFilesDir() + "/audio_to_enc.3gp");
                                String base64ready =  babak.getStringFile(file);



                                //clean internal
                                babak.cleaninternal(getApplicationContext());

                                String texttoenc="";

                                // First create a rand string 20ch long to use as passwd to enc user data
                                String randomstring = babak.randomAlphaNumeric(20);
                                texttoenc= base64ready;

                                //enc data using AES + just created randstring
                                String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
                                //enc the randstring using RSA key
                                String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(current_valt_Pub_key, randomstring);
                                String combinedmsg = encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;


                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat mdformat = new SimpleDateFormat("y-M-d HH:mm:ss");
                                String timetouse = mdformat.format(calendar.getTime());

                                String desc = filenameingevoerddooruser +" | "+timetouse;

                                //enc desc using AES
                                String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
                                //gen false name
                                encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

                                String filenametowrite_archive = "DSG_AUDIO_" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;


                                babak.write(getApplicationContext(), filenametowrite_archive, combinedmsg);




                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        // Update UI elements
                                        dialog3.dismiss();
                                        Intent myIntent333316c = new Intent(getBaseContext(), archive.class);
                                        startActivity(myIntent333316c);
                                        finish();
                                    }
                                });


                            }
                        }.start();





//end save



                    }else{
                        Toast.makeText(archive.this, R.string.Name_cant_be_empty, Toast.LENGTH_LONG).show();
                        //clean internal
                        babak.cleaninternal(getApplicationContext());
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
