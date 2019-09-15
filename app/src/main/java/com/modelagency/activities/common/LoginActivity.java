package com.modelagency.activities.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import com.modelagency.R;
import com.modelagency.activities.RegisterActivity;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends NetworkBaseActivity {

    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv_login = findViewById(R.id.tv_login);
        Typeface typeface = Utility.getFreeHandFont(this);
        tv_login.setTypeface(typeface);


    }

}
