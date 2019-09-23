package com.modelagency.activities.common;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;

import com.modelagency.activities.model.ProfileActivity;
import com.modelagency.utilities.Constants;

public class SplashActivity extends BaseActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(sharedPreferences.getBoolean(Constants.IS_LOGGED_IN,false)){
            if(sharedPreferences.getBoolean(Constants.IS_REGISTERED,false)){
                intent=new Intent(SplashActivity.this,ProfileActivity.class);
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
