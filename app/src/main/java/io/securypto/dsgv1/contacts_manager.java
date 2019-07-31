package io.securypto.dsgv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.securypto.DSGV1.R;

public class contacts_manager extends Activity {

    Button btn;
    private ClipboardManager myClipboard;
    private ClipData myClip;




    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager_page);



        String []allefiles = getApplicationContext().fileList();
        //filterout array contain only what yo want
        List<String> listValue = new ArrayList<>(); //a LinkedList may be more appropriate


        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();


        for (String each_file_name : allefiles) {
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





        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2, android.R.id.text1, listValue);
        ListView listView = (ListView)findViewById(R.id.listView1);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
               //Toast.makeText(contacts_manager.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                confirmDialogDemomultibutton(clickItemObj.toString());
            }
        });



        startvideo();


        TextView textView = (TextView) findViewById(R.id.textViewnocontact);
        textView.setMovementMethod(new ScrollingMovementMethod());

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




    /**
     * Alert dialog demo
     */
    private void alertDialogDemo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert dialog demo !");
        builder.setMessage("This is an alert dialog message");
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }





//singel confirm dialog

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








    //singel confirm dialog with multi buttons
    private void confirmDialogDemomultibutton(final String selected_file_by_user) {

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Contact Manager");
        builder.setCancelable(false);
        builder.setItems(new CharSequence[]
                        {"Send a message", "Delete Contact", "Cancel"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Intent i = new Intent(getBaseContext(), write_a_msg.class);
                                i.putExtra("selected_contact_by_user",selected_file_by_userNOW);
                                startActivity(i);
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
















    public void gotopagecontactimport(View View){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setItems(new CharSequence[]
//                        {"Import by QR Code", "Read from Clipboard", "Manual", "Cancel"},
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


                                importcontactdialogclip(textfromclipboard);




                                break;

                          //  case 2:
                          //      Intent myIntent333d = new Intent(getBaseContext(),   contactmanual.class);
                          //      startActivity(myIntent333d);
                          //      break;

                            case 2:
                                finish();
                                startActivity(getIntent());
                                break;

                        }
                    }
                });
        builder.create().show();






    }













    public void importcontactdialogclip(final String contactpubtoimport){
        final EditText edtText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact Import");
        builder.setMessage("Contact Name");
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setNeutralButton("Save", new DialogInterface.OnClickListener() {
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
                Context context_mci = getApplicationContext();
                babak.write(context_mci, filenametowrite,contactpubtoimport2);


                Toast.makeText(getApplicationContext(), "New contact has been saved!", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(getIntent());



            }
        });
        builder.show();
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