package io.securypto.dsgv1;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.VideoView;

import io.securypto.DSGV1.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class settings extends AppCompatActivity {





    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "English", "Dutch", "Deutsch", "Francais", "Espanol", "中文",
            "فارسی", "Turkish", "العربية", "Indonesian", "Korean", "Hindustani", "Greece", "Русский",
    };


    int[] listviewImage = new int[]{
            R.mipmap.flagenglish, R.mipmap.flagnetherlands, R.mipmap.flaggermany,R.mipmap.flagfrance,R.mipmap.flagspain,R.mipmap.flagchina,
            R.mipmap.flagiran, R.mipmap.flagturkey, R.mipmap.flagsaudiarabia,R.mipmap.flagindonesia,R.mipmap.flagsouthkorea,R.mipmap.flagindia,R.mipmap.flaggreece,R.mipmap.flagrussian,
    };






    Switch expertmodeswitch;
    Switch screenshotsmodeswitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_settings);







        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < listviewTitle.length; i++) {


            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("ids", Integer.toString(i));
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));

            aList.add(hm);
        }

        final String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.listofvideos);
        androidListView.setAdapter(simpleAdapter);


        androidListView.setBackgroundResource(R.drawable.customshape);



        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                HashMap<String, Object> obj = (HashMap<String, Object>) adapterView.getAdapter().getItem(position);
                String langid = (String) obj.get("ids");
                String listview_title = (String) obj.get("listview_title");
                Toast.makeText(settings.this, "-"+langid+"-", Toast.LENGTH_SHORT).show();

                SharedPreferences settings = getSharedPreferences("lang", 0);
                SharedPreferences.Editor editor = settings.edit();



                if (langid.equals("0")) {
                    editor.putString("lang","en");
                    editor.commit();
                    babak.restartapp(getApplicationContext());


                } else if (langid.equals("1UIT")) {
                    editor.putString("lang","nl");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                } else if (langid.equals("9UIT")) {
                    editor.putString("lang","in");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                } else if (langid.equals("12UIT")) {
                    editor.putString("lang","el");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                } else if (langid.equals("13UIT")) {
                    editor.putString("lang","kv");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                } else if (langid.equals("7UIT")) {
                    editor.putString("lang","tr");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                } else if (langid.equals("10UIT")) {
                    editor.putString("lang","ko");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                } else if (langid.equals("8UIT")) {
                    editor.putString("lang","ar");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                } else if (langid.equals("3UIT")) {
                    editor.putString("lang","fr");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                }

                else
                {

                    AlertDialog.Builder builder = new AlertDialog.Builder(settings.this);
                    builder.setTitle(R.string.Info);
                    builder.setMessage(R.string.lang_not_ready);
                    builder.setCancelable(false);
                    builder.setNeutralButton(R.string.Ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //  Toast.makeText(getApplicationContext(), "Neutral button clicked", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(getIntent());
                        }
                    });
                    builder.show();
                }




            }





        });










        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionnameis = pInfo.versionName;
            Button p1_button = (Button)findViewById(R.id.buttontextViewversion);
            p1_button.setText( getResources().getString(R.string.Version) +": "+versionnameis);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }





        SharedPreferences settings = getSharedPreferences("UserInfo", 0);


        expertmodeswitch = (Switch)findViewById(R.id.switch_mode);
        if(settings.getString("mode", "").equals("On"))
        {
            expertmodeswitch.setChecked(true);
        }


        expertmodeswitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(expertmodeswitch.isChecked())
                {
                    Toast.makeText(getApplicationContext(), R.string.Expert_mode_has_been_enabled, Toast.LENGTH_SHORT).show();


                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("mode","On");
                    editor.commit();


                }
                else {
                    Toast.makeText(getApplicationContext(), R.string.Expert_mode_has_been_disabled, Toast.LENGTH_SHORT).show();
                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("mode","Off");
                    editor.commit();
                }

            }
        });









        screenshotsmodeswitch = (Switch)findViewById(R.id.switch_screenshot);
        if(settings.getString("screenshot", "").equals("On"))
        {
            screenshotsmodeswitch.setChecked(true);
        }


        screenshotsmodeswitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(screenshotsmodeswitch.isChecked())
                {

                    confirmscreenshot();

                }
                else {

                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("screenshot","Off");
                    editor.commit();
                    babak.restartapp(getApplicationContext());
                }

            }
        });


















        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));

    }



    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));
    }






    private void confirmscreenshot() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.WARNING);
        builder.setMessage(R.string.Screenshot_confim);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("screenshot","On");
                editor.commit();
                babak.restartapp(getApplicationContext());

            }

        });



        builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getApplicationContext(), "You've changed your mind to delete all records", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());
            }
        });

        builder.show();
    }








    public void checkappinfo(View View) {
        String packageName = "io.securypto.dsgv1";

        try {
            //Open the specific App Info page:
            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + packageName));
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            //e.printStackTrace();

            //Open the generic Apps page:
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
            startActivity(intent);

        }
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
