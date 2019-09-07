package com.modelagency.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;

import com.modelagency.utilities.Constants;

public class SplashActivity extends BaseActivity {

    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        editor.putString(Constants.FULL_NAME,"Vipin Dhama");
        editor.putString(Constants.EMAIL,"vipinsuper19@gmail.com");
        editor.putString(Constants.MOBILE_NO,"9718181697");
        editor.putString(Constants.LOCATION,"Delhi");
        editor.putBoolean(Constants.IS_LOGGED_IN,true);
        editor.commit();

        if(sharedPreferences.getBoolean(Constants.IS_LOGGED_IN,false)){
            intent=new Intent(SplashActivity.this,HomeActivity.class);
        }else {
            intent=new Intent(SplashActivity.this,HomeActivity.class);
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
