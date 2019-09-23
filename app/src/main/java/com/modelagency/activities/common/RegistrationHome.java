package com.modelagency.activities.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.modelagency.R;
import com.modelagency.activities.agency.RegisterActivity;
import com.modelagency.activities.model.TalentRegisterActivity;
import com.modelagency.utilities.Utility;

public class RegistrationHome extends NetworkBaseActivity implements View.OnClickListener {

    private TextView tv_registration;
    private Button button_talent, button_agency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_home);

        tv_registration = findViewById(R.id.tv_registration);
        button_talent = findViewById(R.id.button_talent);
        button_talent.setOnClickListener(this);
        button_agency = findViewById(R.id.button_agency);
        button_agency.setOnClickListener(this);
        Typeface typeface = Utility.getFreeHandFont(this);
        tv_registration.setTypeface(typeface);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.button_talent){
            Intent intent = new Intent(this, TalentRegisterActivity.class);
            intent.putExtra("user", "talent");
            startActivity(intent);
        }else if(v.getId()==R.id.button_agency){
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("user", "agency");
            startActivity(intent);
        }
    }
}
