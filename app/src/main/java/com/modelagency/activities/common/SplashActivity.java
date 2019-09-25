package com.modelagency.activities.common;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;

import com.modelagency.activities.agency.ModelListActivity;
import com.modelagency.activities.agency.RegisterActivity;
import com.modelagency.activities.talent.JobListActivity;
import com.modelagency.activities.talent.ProfileActivity;
import com.modelagency.utilities.Constants;

public class SplashActivity extends BaseActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(sharedPreferences.getBoolean(Constants.IS_USER_CREATED,false)){
            if(sharedPreferences.getBoolean(Constants.IS_REGISTERED,false)){
                if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("model"))
                intent=new Intent(SplashActivity.this, JobListActivity.class);
                else intent = new Intent(SplashActivity.this, ModelListActivity.class);
            }else{
                intent=new Intent(SplashActivity.this,RegistrationHome.class);
            }

        }else {
            intent=new Intent(SplashActivity.this, LoginActivity.class);
        }


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
                // overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        }, 2000);
    }
}
