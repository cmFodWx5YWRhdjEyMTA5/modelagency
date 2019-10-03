package com.modelagency.activities.common;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.modelagency.R;

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
