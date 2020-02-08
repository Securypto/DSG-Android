package io.securypto.dsgv1;


import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.securypto.DSGV1.R;

public class filemanagerdsg extends AppCompatActivity {

    Button button ;
    Intent intent ;
    private static final int buffer = 1024;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.filemanagerdsg);

        button = (Button)findViewById(R.id.button) ;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 7);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode==RESULT_OK){

                    String PathHolder = data.getData().getPath();

                    Toast.makeText(filemanagerdsg.this, PathHolder , Toast.LENGTH_LONG).show();


                    // Toast.makeText(backup.this, zipfiledest, Toast.LENGTH_SHORT).show();

                    //convert array
                    //List<String> listValue = new ArrayList<>();
                    //listValue.add(getFilesDir()+"/"+PathHolder );
                    //String[] listValue2 = listValue.toArray(new String[listValue.size()]);
                    //final String zipfiledest = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/DigisafeGuard_Backup_test.zip";

                    //babak.zip(listValue2, zipfiledest, buffer);


                }
                break;

        }
    }

}