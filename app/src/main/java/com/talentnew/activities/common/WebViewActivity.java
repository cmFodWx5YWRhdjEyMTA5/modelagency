package com.talentnew.activities.common;

import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.talentnew.R;

import java.util.Calendar;

public class WebViewActivity extends NetworkBaseActivity {

    private String flag;
    private WebView web_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        setToolbarDetails(this);
        initFooter(this,10);
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(flag);
        web_view = findViewById(R.id.web_view);
        web_view.setWebViewClient(new WebViewClient());
        showProgress(true);
        if(flag.equals("Blogs")){
            web_view.loadUrl("https://www.talentnew.com/blog");
        }else if(flag.equals("Privacy")){
            web_view.loadUrl("https://www.talentnew.com/privacy-policy");
        }else if(flag.equals("Terms of use")){
            web_view.loadUrl("https://www.talentnew.com/terms");
        }
    }

    public class WebViewClient extends android.webkit.WebViewClient
    {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            showProgress(true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            showProgress(false);
            /*long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();
            Log.i(TAG,"diff "+diff);
            if(diff > 800 || _breakerHits > _BreakerCount){
                _breakerHits = 0;
            }else{
                _breakerHits++;
                view.reload();
            }*/
            //webView.setBackgroundColor(Color.TRANSPARENT);
            //webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }

    }

}
