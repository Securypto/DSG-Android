package io.securypto.dsgv1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.securypto.DSGV1.R;

public class scu_wallet extends AppCompatActivity {

    WebView mWebView;
    ProgressBar progressBar;
    private ProgressDialog dialog;

    final String archivemodefinal = "DSG_SCU_WALLET_V5";

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.activity_scu_wallet);


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





        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.Decrypting_in_progress));
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();





        new Thread() {
            public void run() {



        String []allefiles = getApplicationContext().fileList();
        //filterout array contain only what yo want
        final List<String> listValue = new ArrayList<>(); //a LinkedList may be more appropriate
        for (String each_file_name : allefiles) {
            if(babak.does_a_scu_wallet_exist(each_file_name, vault_name_short, archivemodefinal))
            {


                each_file_name=each_file_name.replace(archivemodefinal,"");
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


                        TextView textViewnowallet = findViewById(R.id.textViewnowallet);
                        Button button_create_scu_wallet = findViewById(R.id.button_create_scu_wallet);
                        textViewnowallet.setVisibility(View.GONE);
                        button_create_scu_wallet.setVisibility(View.GONE);

                        if (listValue.isEmpty()) {
                            textViewnowallet.setVisibility(View.VISIBLE);
                            button_create_scu_wallet.setVisibility(View.VISIBLE);
                        }
                        else{

                            String firstNum = listValue.get(0);
                            Toast.makeText(getApplicationContext(), "Result: "+firstNum+"", Toast.LENGTH_SHORT).show();

                            mWebView.loadUrl("https://cp.securypto.io/");
                            mWebView.setVisibility(View.VISIBLE);

                        }



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



    }






    @Override
    public void onResume(){
        super.onResume();
        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));
    }










    public void genpriv(View View){

        //final String vartopas = "xpub1GTLgu4eqFscfKxqAW2cPy4Zph6pVyYhHP1cPy4Zph6pVyYhHPend";

       // mWebView.loadUrl("file:///android_asset/webview.html");
       // mWebView.loadUrl("file:///android_asset/bip32-master/index.html");
        mWebView.loadUrl("file:///android_asset/dsg/test.html");
        mWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
               // mWebView.loadUrl("javascript:updateFromAndroid('" + vartopas + "')");

                mWebView.loadUrl("javascript:create_random_scu_xpiv()");



            }
        });

    }





    public void returnjavascriptresult(String message){

        Toast.makeText(getApplicationContext(), "Result: "+message+"", Toast.LENGTH_SHORT).show();

        savenewwallet(message);

    }








    public void savenewwallet(String texttoenc){


        String desc ="SCUXPRIV";

        final GlobalClass globalVariable = (GlobalClass) getApplicationContext();
        final String current_valt_Pub_key = globalVariable.get_current_valt_Pub_key();
        final String vault_name_short  = globalVariable.get_vault_name_short();
        final String vault_passwd  = globalVariable.get_vault_passwd();


        // First create a rand string 20ch long to use as passwd to enc user data
        String randomstring = babak.randomAlphaNumeric(20);

        //enc data using AES + just created randstring
        String encrypted_data_by_AES_and_randstring = AESCrypt.encrypt(randomstring, texttoenc);
        //enc the randstring using RSA key
        String encrypted_randstring_by_user_pubkey = encclass.encryptRSAToString(current_valt_Pub_key, randomstring);
        String combinedmsg = encrypted_randstring_by_user_pubkey +"DSGSEPERATOR"+encrypted_data_by_AES_and_randstring;


        //enc desc using AES
        String encrypted_desc_to_use_as_file_name = AESCrypt.encrypt(vault_passwd, desc);
        //gen false name
        encrypted_desc_to_use_as_file_name = Base64.encodeToString(encrypted_desc_to_use_as_file_name.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);

        String filenametowrite_archive = archivemodefinal + "" + vault_name_short+"_"+encrypted_desc_to_use_as_file_name;

        babak.write(getApplicationContext(), filenametowrite_archive, combinedmsg);


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
            //Log.d("LogTag", message);
            returnjavascriptresult(message);
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
