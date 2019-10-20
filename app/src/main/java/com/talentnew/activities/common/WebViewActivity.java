package com.talentnew.activities.common;

import android.os.Bundle;

import android.widget.TextView;

import com.talentnew.R;

public class WebViewActivity extends NetworkBaseActivity {

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);
        initFooter(this,10);
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(flag);

    }

}
