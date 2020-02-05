package io.securypto.dsgv1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.time.Instant;

import io.securypto.DSGV1.R;

public class help extends AppCompatActivity {

    WebView mWebView;
    ProgressBar progressBar;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check if screenshot is allowed
        babak.checkscreenshotstatus(getSharedPreferences("UserInfo", 0), getWindow());

        setContentView(R.layout.help);



        final TextView infofieldmsg = (TextView) findViewById(R.id.infofield);
        infofieldmsg.setVisibility(View.GONE);
        final Button button_close = (Button) findViewById(R.id.button_return_archive);
        button_close.setVisibility(View.GONE);


        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = (WebView) findViewById(R.id.webView);
/*
        progressBar.setVisibility(View.VISIBLE);
        mWebView.setWebViewClient(new Browser_home());
        mWebView.setWebChromeClient(new MyChrome());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAppCacheEnabled(false);
*/
        progressBar.setVisibility(View.VISIBLE);
        mWebView.setWebViewClient(new help.Browser_home());
        mWebView.setWebChromeClient(new help.MyChrome());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
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

        loadWebsite();




        babak.startvideo(getApplicationContext(), (VideoView) findViewById(R.id.videoView));


    }






    private void loadWebsite() {
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
           // mWebView.loadUrl("https://cdn1.securypto.io/app-videos/");
            mWebView.setVisibility(View.VISIBLE);
          mWebView.loadUrl("https://kb.securypto.io/");
            progressBar.setVisibility(View.GONE);

            Button button_close = (Button) findViewById(R.id.button_return_archive);
            button_close.setVisibility(View.GONE);


        } else {
            mWebView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
          //  mWebView.loadUrl("file:///android_asset/bip32-master/index.html");
            final TextView infofieldmsg = (TextView) findViewById(R.id.infofield);
            infofieldmsg.setVisibility(View.VISIBLE);
            infofieldmsg.setText(getString(R.string.please_visit_kwnolagebase));

            Button button_close = (Button) findViewById(R.id.button_return_archive);
            button_close.setVisibility(View.VISIBLE);

            Button backtoapp = (Button) findViewById(R.id.backtoapp);
            backtoapp.setVisibility(View.GONE);

            Button backpage = (Button) findViewById(R.id.backpage);
            backpage.setVisibility(View.GONE);


        }
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










/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                       // finish();
                        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   firstpage.class);
                        startActivity(myIntentgotofirstpage);
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }



 */


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    public void backpage(View View){
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }





    public void backtoapp(View View){
        Intent myIntentgotofirstpage = new Intent(getBaseContext(),   firstpage.class);
        startActivity(myIntentgotofirstpage);
    }




    public void gotologinsuccespage(View View){
        Intent myIntentgotologinsucces = new Intent(getBaseContext(),   login_succes.class);
        startActivity(myIntentgotologinsucces);
    }





}
