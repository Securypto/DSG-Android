package io.securypto.dsgv1;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import android.app.AlertDialog;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;




import io.securypto.DSGV1.R;

public class backup extends AppCompatActivity {


    private static final int buffer = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_backup);





/*
        File filesInDirectory[] = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).listFiles();
        List<String> listValue_backups2 = new ArrayList<>();
        for (int i = 0; i<filesInDirectory.length;i++) {
            listValue_backups2.add(filesInDirectory[i].getName());
        }
*/



        //werkt internal
       // String[] allefiles = getApplicationContext().fileList();

        //werkt internal accecabale
        String[] allefiles = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).list();


        List<String> listValue_backups = new ArrayList<>();
        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();

        for (String each_file_name : allefiles) {
            if (babak.is_it_a_backup_file(each_file_name)) {

                listValue_backups.add(each_file_name);

            }
        }



        TextView textViewnobackup = findViewById(R.id.textViewnobackup);
        textViewnobackup.setVisibility(View.GONE);

        if (listValue_backups.isEmpty()) {
            // Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            textViewnobackup.setVisibility(View.VISIBLE);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listValue_backups);
        ListView listView = (ListView) findViewById(R.id.listView_backup);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                // Toast.makeText(backup.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                askwhattododialog(clickItemObj.toString());


            }
        });








startvideo();


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



    private void askwhattododialog(final String selected_file_by_user) {
AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(false);
        builder.setItems(new CharSequence[]
    {"Transfer the Backup", "Restore the Backup", "Delete the Backup", "Cancel"},
            new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            // The 'which' argument contains the index position
            // of the selected item
            switch (which) {
                case 0:
                    //werkt internal acce
                    File filetoshare = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + selected_file_by_user);
                    shareFile(filetoshare);
                    //Toast.makeText(backup.this, "Under construction", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    confirmrestoredialog(selected_file_by_user);
                    break;
                case 2:
                    confirmdeletedialog(selected_file_by_user);
                    break;
                case 3:
                    //finish();
                    //startActivity(getIntent());
                    break;

            }
        }
    });
        builder.create().show();
}






    private void shareFile(File file) {


        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("application/zip");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"DSG Encrypted Data");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "DSG Encrypted Data");
        startActivity(Intent.createChooser(intentShareFile, "DSG Encrypted Data"));

    }






    private void confirmrestoredialog(final String selected_file_by_user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING");
        builder.setMessage("You are about to restore a backup...\n\n"+selected_file_by_user+"\n\nAll current data will be permanently deleted. Do you really want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            //Context context_file_exist= getApplicationContext();

            @Override
            public void onClick(DialogInterface dialog, int which) {
                restore_a_backup(selected_file_by_user);
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



    //test new


    private void confirmdeletedialog(final String selected_file_by_user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("WARNING");
        builder.setMessage("You are about to delete a backup file...\n\n"+selected_file_by_user+"\n\nDo you really want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            //Context context_file_exist= getApplicationContext();

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //werkt internal
                //getApplicationContext().deleteFile(selected_file_by_user);

                //werkt internal accebale
                File f = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+selected_file_by_user);
                boolean d = f.delete();

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






    public void make_a_backup(View View) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.loading,
                null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(formElementsView);
        builder.setCancelable(false);
        builder.show();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("y-M-d-HHmmss");
                String bcname = mdformat.format(calendar.getTime());



 //String bcname = "blabla4";
 make_a_backup_reall(bcname);


            }
        }, 1000);

    }






    public void make_a_backup_reall(String nametouseforbackup) {

        String []allefiles = getApplicationContext().fileList();
        //filterout array contain only what yo want
        List<String> listValue = new ArrayList<>(); //a LinkedList may be more appropriate
        for (String each_file_name : allefiles) {
            if(babak.is_it_a_DSG_file(each_file_name))
            {
                listValue.add(getFilesDir()+"/"+each_file_name );
            }
        }

        //convert array
        String[] listValue2 = listValue.toArray(new String[listValue.size()]);


        //werkt, echt internal opslaan
        //String zipfiledest = getFilesDir()+"/DigisafeGuard_Backup_"+nametouseforbackup+".zip";


        //werkt, internal but accecebale
        String zipfiledest = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/DigisafeGuard_Backup_"+nametouseforbackup+".zip";



       // Toast.makeText(backup.this, zipfiledest, Toast.LENGTH_SHORT).show();
        babak.zip(listValue2, zipfiledest, buffer);

        finish();
        startActivity(getIntent());
    }








    public void restore_a_backup(String filetorestore) {

        //delete everything before restore
        String []allefiles = getApplicationContext().fileList();
        for (String each_file_name : allefiles) {
            if(babak.is_it_a_DSG_file(each_file_name))
            {
                getApplicationContext().deleteFile(each_file_name);
            }
        }

        String unzipto = getFilesDir()+"/";

        //werkt internal accecebale
        String zipfiletounzip = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/"+filetorestore;

        // werkt, internal
        //String zipfiletounzip = getFilesDir() + "/"+filetorestore;

        babak.unzip(zipfiletounzip, unzipto);

       // Toast.makeText(getApplicationContext(), "Backup file "+filetorestore+" has been successfully used to restore.", Toast.LENGTH_SHORT).show();

    }










    public void gotofirstpage(View View){
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   firstpage.class);
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