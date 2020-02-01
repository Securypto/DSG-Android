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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import io.securypto.DSGV1.R;

public class passwdmanager extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_passwdmanager);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);


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




        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, listValue);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.babaklistlayout, R.id.text1, listValue);
        ListView listView = (ListView)findViewById(R.id.listView_passwd_manager);

        listView.setAdapter(adapter);

        listView.setBackgroundResource(R.drawable.customshape);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);



                //Toast.makeText(passwdmanager.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                confirmDialogDemomultibutton(clickItemObj.toString());









            }
        });









        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));






    }




    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);
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



        int[] imageIdArr = {R.mipmap.sendfile, R.mipmap.delete, R.mipmap.cancel};
        final String[] listItemArr = {getResources().getString(R.string.Show_Record), getResources().getString(R.string.Delete), getResources().getString(R.string.Cancel)};
        final String CUSTOM_ADAPTER_IMAGE = "image";
        final String CUSTOM_ADAPTER_TEXT = "text";

        AlertDialog.Builder builder = new AlertDialog.Builder(passwdmanager.this);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(passwdmanager.this, dialogItemList,
                R.layout.layout_dialog_select_action_by_contact,
                new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});


        // Set the data adapter.
        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


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



        builder.setTitle(R.string.Credentials);
        builder.setMessage(getResources().getString(R.string.Description)+":"+manager_desc+"\n"+getResources().getString(R.string.Username)+":"+manager_user+"\n"+getResources().getString(R.string.lang_passwd)+":"+manager_passwd);
        builder.setCancelable(false);
        builder.setNeutralButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), selected_file_by_userNOW, Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }







    private void confirmDialogDemo(final String selected_file_by_user) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.WARNING);
        builder.setMessage(R.string.data_wipe_confim);
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
        builder.setTitle(R.string.Add_New_Credentials);
        builder.setMessage("");
        builder.setCancelable(false);


        builder.setNeutralButton(R.string.Save, new DialogInterface.OnClickListener() {
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

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.data_saved), Toast.LENGTH_SHORT).show();


                finish();
                startActivity(getIntent());



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
