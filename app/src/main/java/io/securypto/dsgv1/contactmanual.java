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
import android.widget.Toast;
import android.widget.VideoView;

import java.nio.charset.StandardCharsets;

import io.securypto.DSGV1.R;

public class contactmanual extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_contactmanual);

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);

    }


    @Override
    public void onResume(){
        super.onResume();

        //check login otherwise go to firstpage
        babak.checkloginstatsu(getApplicationContext(), getBaseContext(), this);

    }



    public void importcontactdialog(View View){
        final EditText edtText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Contact Import");
        builder.setMessage("Contact Name");
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setNeutralButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                EditText editText3 = (EditText) findViewById(R.id.importmanualfield);
                String contactpubtoimport = editText3.getText().toString().trim();


                contactpubtoimport = contactpubtoimport.replaceAll("DigiSafeGuard-PUBLIC-KEY:", "");


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
                String enc_text_to_write = babak.enc_a_text_using_RSA_AND_AES(getApplicationContext(), contactpubtoimport);
                //save the file
                babak.write(getApplicationContext(), filenametowrite, enc_text_to_write);


                editText3.setText("New contact has been saved!");

                Toast.makeText(getApplicationContext(), "New contact has been saved!", Toast.LENGTH_SHORT).show();

                Intent myIntent5c = new Intent(getBaseContext(),   login_succes.class);
                startActivity(myIntent5c);

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
