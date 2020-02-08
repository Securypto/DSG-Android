package io.securypto.dsgv1;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.os.StrictMode;
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
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import android.app.AlertDialog;
import android.widget.VideoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;




import io.securypto.DSGV1.R;

public class backup extends AppCompatActivity {

    private ProgressDialog dialog;

    private static final int buffer = 1024;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activiy_backup);



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


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


       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listValue_backups);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.babaklistlayout, R.id.text1, listValue_backups);
        ListView listView = (ListView) findViewById(R.id.listView_backup);

        listView.setAdapter(adapter);

        listView.setBackgroundResource(R.drawable.customshape);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                Object clickItemObj = adapterView.getAdapter().getItem(index);
                // Toast.makeText(backup.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                askwhattododialog(clickItemObj.toString());


            }
        });








        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));


    }


    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));
    }









    private void askwhattododialog(final String selected_file_by_user) {







        int[] imageIdArr = {R.mipmap.sendfile, R.mipmap.sendfile, R.mipmap.delete, R.mipmap.cancel};
        final String[] listItemArr = {getResources().getString(R.string.Export), getResources().getString(R.string.Restore), getResources().getString(R.string.Delete), getResources().getString(R.string.Cancel)};


        final String CUSTOM_ADAPTER_IMAGE = "image";
        final String CUSTOM_ADAPTER_TEXT = "text";

        AlertDialog.Builder builder = new AlertDialog.Builder(backup.this);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(backup.this, dialogItemList,
                R.layout.layout_dialog_select_action_by_contact,
                new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});


        // Set the data adapter.
        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

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






    private void shareFile(File file) {


        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        intentShareFile.setType("application/zip");
        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Send DSG Encrypted Data");
        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Send DSG Encrypted Data");
        startActivity(Intent.createChooser(intentShareFile, "Send DSG Encrypted Data"));

    }






    private void confirmrestoredialog(final String selected_file_by_user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.WARNING);
        builder.setMessage(R.string.You_are_about_to_restore_a_backup+"\n\n"+selected_file_by_user+"\n\n"+R.string.Wipe_all_date);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

            //Context context_file_exist= getApplicationContext();

            @Override
            public void onClick(DialogInterface dialog, int which) {
                restore_a_backup(selected_file_by_user);
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



    //test new


    private void confirmdeletedialog(final String selected_file_by_user) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.WARNING);
        builder.setMessage(R.string.Delete_backup+"\n\n"+selected_file_by_user+"\n\n"+R.string.Are_You_Sure);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

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


        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
            }
        });

        builder.show();
    }





/*
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
*/





    public void make_a_backup(View View) {



        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Data_Encryption));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();



        new Thread() {
            public void run() {




                Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("y-M-d-HHmmss");
        String nametouseforbackup = mdformat.format(calendar.getTime());

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
        final String zipfiledest = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/DigisafeGuard_Backup_"+nametouseforbackup+".zip";
        final File filetoshare = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/DigisafeGuard_Backup_" + nametouseforbackup+".zip");

       // Toast.makeText(backup.this, zipfiledest, Toast.LENGTH_SHORT).show();
        babak.zip(listValue2, zipfiledest, buffer);





                //hier komt einde tread

                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        dialog.dismiss();
                        alertbackupdonealert(filetoshare);
                       // finish();
                       // startActivity(getIntent());
                    }
                });


            }
        }.start();




    }




    private void alertbackupdonealert(final File filetoshare) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.Backup_has_been_created);
        builder.setMessage(R.string.MSG_Backup_Done);
        builder.setCancelable(false);
        builder.setNeutralButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();


                finish();
                shareFile(filetoshare);
            //startActivity(getIntent());

            }
        });
        builder.show();
    }






    public void restore_a_backup(final String filetorestore) {



        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Decrypting_in_progress));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();



        new Thread() {
            public void run() {



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



                //hier komt einde tread

                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        dialog.dismiss();
                    }
                });


            }
        }.start();



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