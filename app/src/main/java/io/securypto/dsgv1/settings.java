package io.securypto.dsgv1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import io.securypto.DSGV1.R;

public class settings extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


    }














    public void gotofirstpage(View View){
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   firstpage.class);
        startActivity(myIntentgotofirstpage);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            // Intent myIntentgotofirstpagea = new Intent(getBaseContext(),   login_succes.class);
            //  startActivity(myIntentgotofirstpagea);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }









}
