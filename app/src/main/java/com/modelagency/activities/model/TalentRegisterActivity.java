package com.modelagency.activities.model;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.modelagency.R;
import com.modelagency.activities.common.HomeActivity;
import com.modelagency.activities.common.LoginActivity;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.common.RegistrationHome;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

public class TalentRegisterActivity extends NetworkBaseActivity {

    private EditText et_first_name,et_last_name,et_mobile,et_otp;
    private TextView tv_registration;
    private Button button_submit;
    private String firstName,lastName,mobile,otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_register);

        TextView tv_registration = findViewById(R.id.tv_registration);
        Typeface typeface = Utility.getFreeHandFont(this);
        tv_registration .setTypeface(typeface);

        et_first_name = findViewById(R.id.et_first_name);
        et_last_name = findViewById(R.id.et_first_name);
        et_first_name.setText(sharedPreferences.getString(Constants.FIRST_NAME,""));
        et_last_name.setText(sharedPreferences.getString(Constants.LAST_NAME,""));

        button_submit = findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        });

    }

    private void onRegister(){
        Intent intent = new Intent(TalentRegisterActivity.this, HomeActivity.class);
        //intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

}
