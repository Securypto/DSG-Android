package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import io.securypto.DSGV1.R;

public class passwdmanager extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwdmanager);

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();




        String []allefiles = getApplicationContext().fileList();
        //filterout array contain only what yo want
        List<String> listValue = new ArrayList<>(); //a LinkedList may be more appropriate

       // listValue.add(manager_desc );

        for (String each_file_name : allefiles) {
            if(babak.is_it_a_passwd_manager_file(each_file_name, vault_name_short))
            {


                each_file_name=each_file_name.replace("DSG_PASSWDM_","");
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




        TextView textViewnopasswd = findViewById(R.id.textViewnopasswd);
        textViewnopasswd.setVisibility(View.GONE);

        if (listValue.isEmpty()) {
            textViewnopasswd.setVisibility(View.VISIBLE);
        }





        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, listValue);
        ListView listView = (ListView)findViewById(R.id.listView_passwd_manager);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);



                //Toast.makeText(passwdmanager.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                confirmDialogDemomultibutton(clickItemObj.toString());









            }
        });













startvideo();


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









    //singel confirm dialog with multi buttons
    private void confirmDialogDemomultibutton(final String selected_file_by_user) {

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();

        //enc desc using AES
        String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, selected_file_by_user);

        //gen false name
        encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);



        final String selected_file_by_userNOW="DSG_PASSWDM_"+vault_name_short+"_"+encrypted_desc_to_use_as_file_name;




        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Contact Manager");
        builder.setCancelable(false);
        builder.setItems(new CharSequence[]
                        {"Show Credentials", "Delete Entry", "Cancel"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                confirmDialogDemoshowdata(selected_file_by_userNOW);
                                break;
                            case 1:
                                confirmDialogDemo(selected_file_by_userNOW);
                                // getApplicationContext().deleteFile(selected_file_by_user);
                                // finish();
                                // startActivity(getIntent());
                                break;
                            case 2:
                                //finish();
                                //startActivity(getIntent());
                                break;

                        }
                    }
                });
        builder.create().show();
    }







    private void confirmDialogDemoshowdata(final String selected_file_by_userNOW) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_passwd  = globalVariable.get_vault_passwd();
        final String current_valt_Priv_key  = globalVariable.get_current_valt_Priv_key();

        Context context1 = getApplicationContext();
        String inhoudtext = babak.read_file(context1, selected_file_by_userNOW);

        //dec using AES
        //inhoudtext = AESCrypt.decrypt(vault_passwd, inhoudtext);

        //dec using RSA
        inhoudtext = encclass.decryptRSAToString(current_valt_Priv_key, inhoudtext);


        String[] passwdstukken = inhoudtext.split("\\============DSG============", -1);
        String manager_desc =passwdstukken[0];
        String manager_user =passwdstukken[1];
        String manager_passwd =passwdstukken[2];



        builder.setTitle("Credentials");
        builder.setMessage("Description:"+manager_desc+"\nUsername:"+manager_user+"\nPassword:"+manager_passwd);
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), selected_file_by_userNOW, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }







    private void confirmDialogDemo(final String selected_file_by_user) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING");
        builder.setMessage("You are about to delete this record. Do you really want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

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



        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }










    public void addnewpasswd(View View){



        final EditText edtText = new EditText(passwdmanager.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.form_elements,
                null, false);

        // You have to list down your form elements
        final EditText Desc = (EditText) formElementsView.findViewById(R.id.popupdesc);
        final EditText Username = (EditText) formElementsView.findViewById(R.id.popupuser);
        final EditText Password = (EditText) formElementsView.findViewById(R.id.popuppasswd);


        AlertDialog.Builder builder = new AlertDialog.Builder(passwdmanager.this);
        builder.setView(formElementsView);
        builder.setTitle("Add New Credentials");
        builder.setMessage("");
        builder.setCancelable(false);


        builder.setNeutralButton("Save Credentials", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String desc = Desc.getText().toString();
                String user = Username.getText().toString();
                String passwd  = Password.getText().toString();


                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                final String vault_name_short  = globalVariable.get_vault_name_short();
                final String vault_passwd  = globalVariable.get_vault_passwd();


                Context context_pm = getApplicationContext();
                babak.create_passwd_manager_file(context_pm, vault_name_short, vault_passwd, desc, user, passwd);

                Toast.makeText(getApplicationContext(), "New Credentials has been saved!", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(getIntent());



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
