package io.securypto.dsgv1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import com.google.zxing.WriterException;

import io.securypto.DSGV1.R;




public class walletspage extends AppCompatActivity implements SearchView.OnQueryTextListener {



    WebView mWebView;
    ProgressBar progressBar;
    private ProgressDialog dialog;






    SearchView searchView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_walletspage);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        ImageView qrholder;
        qrholder = (ImageView)findViewById(R.id.qrholder);
        qrholder.setVisibility(View.GONE);



        final TextView infofieldmsg = (TextView) findViewById(R.id.infofield);
        infofieldmsg.setVisibility(View.GONE);


        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = (WebView) findViewById(R.id.webView);

        progressBar.setVisibility(View.VISIBLE);
        mWebView.setWebViewClient(new Browser_home());
        mWebView.setWebChromeClient(new MyChrome());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setAllowFileAccessFromFileURLs(true);
        mWebView.setVisibility(View.GONE);










        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String walletsmode ="text";

        ImageButton ImageButtonlinks = findViewById(R.id.button_select_archive_sort);

        if(settings.getString("walletsmode", "").equals("BTC"))
        {
            walletsmode ="DSG_WALLET_BTC_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendfile);
        }
        else if(settings.getString("walletsmode", "").equals("SCU")){
            walletsmode ="DSG_WALLET_SCU_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendvoice);
        }
        else if(settings.getString("walletsmode", "").equals("ETH")){
            walletsmode ="DSG_WALLET_ETH_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendpic);
        }
        else {
            walletsmode ="DSG_WALLET_BTC_";
            ImageButtonlinks.setImageResource(R.mipmap.wsendfile);
        }



        final String walletsmodefinal = walletsmode;


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
                    if(babak.is_it_a_archive_file(each_file_name, vault_name_short, walletsmodefinal))
                    {


                        each_file_name=each_file_name.replace(walletsmodefinal,"");
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

                        searchView.setOnQueryTextListener(walletspage.this);

                        listView.setBackgroundResource(R.drawable.customshape);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                                Object clickItemObj = adapterView.getAdapter().getItem(index);



                                //Toast.makeText(archive.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                                confirmDialogDemomultibutton(clickItemObj.toString(), walletsmodefinal);




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
    private void confirmDialogDemomultibutton(final String selected_file_by_user, final String walletsmodefinal) {



        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();

        //enc desc using AES
        String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, selected_file_by_user);

        //gen false name
        encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

        final String selected_file_by_userNOW=walletsmodefinal+vault_name_short+"_"+encrypted_desc_to_use_as_file_name;



        int[] imageIdArr = {R.mipmap.sendfile, R.mipmap.sendfile, R.mipmap.sendfile, R.mipmap.delete, R.mipmap.cancel};
        final String[] listItemArr = {getResources().getString(R.string.Show_QR_XPUB), getResources().getString(R.string.Show_QR_XPRIV), getResources().getString(R.string.Show_Balance), getResources().getString(R.string.Delete), getResources().getString(R.string.Cancel)};
        final String CUSTOM_ADAPTER_IMAGE = "image";
        final String CUSTOM_ADAPTER_TEXT = "text";

        AlertDialog.Builder builder = new AlertDialog.Builder(walletspage.this);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(walletspage.this, dialogItemList,
                R.layout.layout_dialog_select_action_by_contact,
                new String[]{CUSTOM_ADAPTER_IMAGE, CUSTOM_ADAPTER_TEXT},
                new int[]{R.id.alertDialogItemImageView, R.id.alertDialogItemTextView});


        // Set the data adapter.
        builder.setAdapter(simpleAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {


                switch (which) {
                    case 0:

                        //showxpub(selected_file_by_userNOW);
                        showxpuborxprivorbalance("xpub",selected_file_by_userNOW, walletsmodefinal, selected_file_by_user);
                            break;

                    case 1:





                        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                        String mode = settings.getString("mode", "").toString();


                        if(settings.getString("mode", "").equals("On"))
                        {

//expert mode, can show

                            AlertDialog.Builder builder = new AlertDialog.Builder(walletspage.this);
                            builder.setTitle(R.string.WARNING);
                            builder.setMessage(R.string.xpriv_text);
                            builder.setCancelable(false);
                            builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showxpuborxprivorbalance("xpriv",selected_file_by_userNOW, walletsmodefinal, selected_file_by_user);
                                }

                            });



                            builder.setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //do nothing
                                }
                            });



                            builder.show();


                        }

                        else{
//not in exper mode, dont show!

                            AlertDialog.Builder builder = new AlertDialog.Builder(walletspage.this);
                            builder.setTitle(R.string.WARNING);
                            builder.setMessage(R.string.xpriv_text_need_expert);
                            builder.setCancelable(false);
                            builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//do nothing

                                }



                            });


                            builder.show();


                        }























                        break;


                    case 2:
                        showxpuborxprivorbalance("balance",selected_file_by_userNOW, walletsmodefinal, selected_file_by_user);
                        break;


                    case 3:
                        confirmDelete(selected_file_by_userNOW);
                        break;

                    case 4:
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











    private void showxpuborxprivorbalance(final String whattoshow, final String selected_file_by_userNOW, final String walletsmodefinal, final String selected_file_by_user) {






        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Decrypting_in_progress));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        new Thread() {
            public void run() {


                final TextView infofieldmsg = (TextView) findViewById(R.id.infofield);
                final Button button_return_archive = (Button) findViewById(R.id.button_return_archive);
                final ImageButton button_import_archive = (ImageButton) findViewById(R.id.button_import_archive);
                final ImageButton button_select_archive_sort = (ImageButton) findViewById(R.id.button_select_archive_sort);
                final ListView listView = (ListView)findViewById(R.id.listView_archive);


                Context context1 = getApplicationContext();
                String inhoudtextunenc = babak.read_file(context1, selected_file_by_userNOW);

               final String inhoudtextdecrypted = babak.dec_a_text_using_RSA_AND_AES(getApplicationContext(), inhoudtextunenc);

                GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                globalVariable.set_tmp_data1(inhoudtextdecrypted);



                if (whattoshow.equals("balance")) {

                  //  Toast.makeText(getApplicationContext(), selected_file_by_userNOW, Toast.LENGTH_SHORT).show();

                    //formaat
                    //alert("xpriv:"+xprv+"-xpub:"+xpub);

                    String[] datastukken = inhoudtextdecrypted.split("-", -1);
                    String[] datastukken2 = datastukken[0].split(":", -1);
                    String[] datastukken3 = datastukken[1].split(":", -1);

                    final String xpriv= datastukken2[1];
                    final String xpub= datastukken3[1];


                    runOnUiThread(new Runnable() {
                        public void run() {



                            ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = cm.getActiveNetworkInfo();
                            if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                                // mWebView.loadUrl("https://cdn1.securypto.io/app-videos/");
                                mWebView.loadUrl("https://cp.securypto.io/dsg.php");
                                mWebView.setVisibility(View.VISIBLE);

                            } else {

                               // showxpuborxprivorbalance("xpub",selected_file_by_userNOW, walletsmodefinal, selected_file_by_user);


                                mWebView.setVisibility(View.GONE);


                                try{

                                final Bitmap bitmap ;
                                final ImageView qrholder;
                                qrholder = (ImageView)findViewById(R.id.qrholder);
                                qrholder.setVisibility(View.VISIBLE);
                                    button_return_archive.setVisibility(View.GONE);
                                    button_import_archive.setVisibility(View.GONE);
                                    button_select_archive_sort.setVisibility(View.GONE);
                                    searchView.setVisibility(View.GONE);
                                    listView.setVisibility(View.GONE);


                                Context contextqr= getApplicationContext();
                                bitmap = babak.texttoqr(contextqr, xpub, 500);
                                qrholder.setImageBitmap(bitmap);

                                    infofieldmsg.setText(getString(R.string.xpub_text_offline_mode));
                                    infofieldmsg.setVisibility(View.VISIBLE);




                                    qrholder.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            qrholder.setVisibility(View.GONE);
                                            infofieldmsg.setVisibility(View.GONE);
                                            button_return_archive.setVisibility(View.VISIBLE);
                                            button_import_archive.setVisibility(View.VISIBLE);
                                            button_select_archive_sort.setVisibility(View.VISIBLE);
                                            searchView.setVisibility(View.VISIBLE);
                                            listView.setVisibility(View.VISIBLE);

                                        }
                                    });


                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }




                            }





                    dialog.dismiss();




                        }
                    });




                }else{
                //its not balance but xpub or xpriv to show


                runOnUiThread(new Runnable() {
                    public void run() {


                        try {



                            final Bitmap bitmap ;
                            final ImageView qrholder;
                            qrholder = (ImageView)findViewById(R.id.qrholder);
                            qrholder.setVisibility(View.VISIBLE);
                            button_return_archive.setVisibility(View.GONE);
                            button_import_archive.setVisibility(View.GONE);
                            button_select_archive_sort.setVisibility(View.GONE);
                            searchView.setVisibility(View.GONE);
                            listView.setVisibility(View.GONE);


                            qrholder.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    qrholder.setVisibility(View.GONE);
                                    infofieldmsg.setVisibility(View.GONE);
                                    button_return_archive.setVisibility(View.VISIBLE);
                                    button_import_archive.setVisibility(View.VISIBLE);
                                    button_select_archive_sort.setVisibility(View.VISIBLE);
                                    searchView.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.VISIBLE);

                                }
                            });

                            //formaat
                            //alert("xpriv:"+xprv+"-xpub:"+xpub);

                            String[] datastukken = inhoudtextdecrypted.split("-", -1);
                            String[] datastukken2 = datastukken[0].split(":", -1);
                            String[] datastukken3 = datastukken[1].split(":", -1);

                            String xpriv= datastukken2[1];
                            String xpub= datastukken3[1];

                            String msgforqr= "ERROR!";
                            String msgforinfofield= "ERROR!";
                            if (whattoshow.equals("xpriv")){
                                msgforqr= xpriv;
                                msgforinfofield=getString((R.string.xpriv_text));
                            }else if (whattoshow.equals("xpub")){
                                msgforqr= xpub;
                                msgforinfofield=getString((R.string.xpub_text));
                            }else{
                                msgforqr= "ERROR!";
                            }


                            final String msgforinfofieldfinal= msgforinfofield;

                    Context contextqr= getApplicationContext();
                    bitmap = babak.texttoqr(contextqr, msgforqr, 500);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            // Update UI elements
                            qrholder.setImageBitmap(bitmap);
                            infofieldmsg.setText(msgforinfofieldfinal);
                            infofieldmsg.setVisibility(View.VISIBLE);

                        }
                    });

                } catch (WriterException e) {
                    e.printStackTrace();
                }



                        dialog.dismiss();
                        // Toast.makeText(getApplicationContext(), inhoudtextdecrypted, Toast.LENGTH_SHORT).show();

                    }
                });


            }



/*
                globalVariable.set_current_data_msg_for_qr(inhoudtextdecrypted);
                globalVariable.set_current_data_array_part(0);
                Intent myIntent2 = new Intent(getBaseContext(),   qrshow.class);
                startActivity(myIntent2);
                finish();

 */






            }






        }.start();






    }




















    private void confirmDelete(final String selected_file_by_user) {


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String mode = settings.getString("mode", "").toString();


        if(settings.getString("mode", "").equals("On"))
        {

//expert mode, may delete



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

        else{
//not in exper mode, dont delete!

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.WARNING);
            builder.setMessage(R.string.Cant_delete_need_expert_mode);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startActivity(getIntent());

                }



            });


            builder.show();


        }



    }













    public void selectarchivesort(final View View){


        int[] imageIdArr = {R.mipmap.sendfile, R.mipmap.sendvoice, R.mipmap.sendpic};
        final String[] listItemArr = {getResources().getString(R.string.BTC_TEXT), getResources().getString(R.string.SCU_TEXT), getResources().getString(R.string.ETH_TEXT)};
        final String CUSTOM_ADAPTER_IMAGE = "image";
        final String CUSTOM_ADAPTER_TEXT = "text";

        AlertDialog.Builder builder = new AlertDialog.Builder(walletspage.this);
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
        SimpleAdapter simpleAdapter = new SimpleAdapter(walletspage.this, dialogItemList,
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
                        editor.putString("walletsmode","BTC");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(),   walletspage.class));
                        break;

                    case 1:
                        editor.putString("walletsmode","SCU");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(),   walletspage.class));
                        break;

                    case 2:
                        editor.putString("walletsmode","ETH");
                        editor.commit();
                        startActivity(new Intent(getBaseContext(),   walletspage.class));
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

























    public void askfornameandthengenkey(View View) {
        final EditText edtText = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // builder.setTitle(R.string.Save_as);
        builder.setMessage(getResources().getString(R.string.Save_wallet_as));
        builder.setCancelable(false);
        builder.setView(edtText);
        builder.setNeutralButton(R.string.Save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                final String filenameingevoerddooruser = edtText.getText().toString();

                if(filenameingevoerddooruser.length() > 0) {



                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                final String vault_name_short  = globalVariable.get_vault_name_short();
                final String vault_passwd  = globalVariable.get_vault_passwd();
                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                String walletsmode ="text";
                if(settings.getString("walletsmode", "").equals("BTC"))
                {
                    walletsmode ="DSG_WALLET_BTC_";
                }
                else if(settings.getString("walletsmode", "").equals("SCU")){
                    walletsmode ="DSG_WALLET_SCU_";
                }
                else if(settings.getString("walletsmode", "").equals("ETH")){
                    walletsmode ="DSG_WALLET_ETH_";
                }
                else {
                    walletsmode ="DSG_WALLET_BTC_";
                }

                //enc desc using AES
                String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, filenameingevoerddooruser);
                //gen false name
                encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

                String filenametowrite_archive = walletsmode + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;





                File file_dest = getApplicationContext().getFileStreamPath(filenametowrite_archive);
                if (file_dest == null || !file_dest.exists()) {
                    globalVariable.set_tmp_data1(filenameingevoerddooruser);
                    wehaveanamenowgenkey();
                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(walletspage.this);
                    builder.setTitle(R.string.WARNING);
                    builder.setMessage(R.string.Cant_overwrite_need_expert_mode);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {

                        //Context context_file_exist= getApplicationContext();

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

//do nothing

                        }


                    });



                    builder.show();


                }







                }else{
                    Toast.makeText(walletspage.this, R.string.Name_cant_be_empty, Toast.LENGTH_LONG).show();
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








    public void wehaveanamenowgenkey(){

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Wallet_creation_in_progress));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();


        SharedPreferences settings = getSharedPreferences("UserInfo", 0);


        if(settings.getString("walletsmode", "").equals("BTC"))
        {

         //   Toast.makeText(getApplicationContext(), "Mode: BTC"+" Name:"+namechoosenbyuser, Toast.LENGTH_SHORT).show();
            mWebView.loadUrl("file:///android_asset/btc/test.html");
            mWebView.setWebViewClient(new WebViewClient(){
                public void onPageFinished(WebView view, String url){
                    mWebView.loadUrl("javascript:create_random_btc_xpiv()");
                } });

        }
        else if(settings.getString("walletsmode", "").equals("SCU")){

         //   Toast.makeText(getApplicationContext(), "Mode: SCU"+" Name:"+namechoosenbyuser, Toast.LENGTH_SHORT).show();
            mWebView.loadUrl("file:///android_asset/scu/test.html");
            mWebView.setWebViewClient(new WebViewClient(){
                public void onPageFinished(WebView view, String url){
                    mWebView.loadUrl("javascript:create_random_scu_xpiv()");
                } });

        }
        else if(settings.getString("walletsmode", "").equals("ETH")){

        //    Toast.makeText(getApplicationContext(), "Mode: ETH"+" Name:"+namechoosenbyuser, Toast.LENGTH_SHORT).show();
            mWebView.loadUrl("file:///android_asset/eth/test.html");
            mWebView.setWebViewClient(new WebViewClient(){
                public void onPageFinished(WebView view, String url){
                    mWebView.loadUrl("javascript:create_random_eth_xpiv()");
                } });

        }
        else {
            Intent gotoshow_edit_archive = new Intent(getBaseContext(),   walletspage.class);
            startActivity(gotoshow_edit_archive);

        }








    }













    public void savexprivkey(final String text_to_enc1){




/*
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Data_Encryption));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
*/

        new Thread() {
            public void run() {



                final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
                final String vault_name_short  = globalVariable.get_vault_name_short();
                final String vault_passwd  = globalVariable.get_vault_passwd();

                String desc  = globalVariable.get_tmp_data1();


                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                String walletsmode ="text";
                if(settings.getString("walletsmode", "").equals("BTC"))
                {
                    walletsmode ="DSG_WALLET_BTC_";
                }
                else if(settings.getString("walletsmode", "").equals("SCU")){
                    walletsmode ="DSG_WALLET_SCU_";
                }
                else if(settings.getString("walletsmode", "").equals("ETH")){
                    walletsmode ="DSG_WALLET_ETH_";
                }
                else {
                    walletsmode ="DSG_WALLET_BTC_";
                }


                //enc desc using AES
                String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
                //gen false name
                encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

                String filenametowrite_archive = walletsmode + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;

                //enc using RSA&AES COMBINE
                String encedtext = babak.enc_a_text_using_RSA_AND_AES(getApplicationContext(), text_to_enc1);
                babak.write(getApplicationContext(), filenametowrite_archive, encedtext);





                runOnUiThread(new Runnable() {
                    public void run() {
                        // Update UI elements
                        // dialog.dismiss();

                        //Toast.makeText(getApplicationContext(), "Record has been saved!", Toast.LENGTH_SHORT).show();


                        Intent myIntent333316c = new Intent(getBaseContext(),   walletspage.class);
                        startActivity(myIntent333316c);
                        finish();

                    }
                });



            }
        }.start();


    }








    class Browser_home extends WebViewClient {

        Browser_home() {
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setTitle(view.getTitle());
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);

        }
    }





    private class MyChrome extends WebChromeClient {







        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            //Toast.makeText(getApplicationContext(), ""+message+"", Toast.LENGTH_SHORT).show();

            //formaat
            //alert("xpriv:"+xprv+"-xpub:"+xpub);


            String[] datastukkenorg = message.split(":", -1);


            if (datastukkenorg[0].equals("xpriv")){
                savexprivkey(message);
            }



            if (datastukkenorg[0].equals("msg")){
                Toast.makeText(getApplicationContext(), ""+message+"", Toast.LENGTH_SHORT).show();
            }


            result.confirm();
            return true;
        }






        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome() {}

        public Bitmap getDefaultVideoPoster()
        {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846);
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
