package com.talentnew.activities.common;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;

import com.talentnew.activities.agency.ModelListActivity;
import com.talentnew.activities.agency.RegisterActivity;
import com.talentnew.activities.talent.JobListActivity;
import com.talentnew.utilities.Constants;

public class SplashActivity extends BaseActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG,"type "+sharedPreferences.getString(Constants.USER_TYPE,""));

        if(sharedPreferences.getBoolean(Constants.IS_USER_CREATED,false)){
            if(sharedPreferences.getBoolean(Constants.IS_REGISTERED,false)){
                if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("model"))
                intent=new Intent(SplashActivity.this, JobListActivity.class);
                else intent = new Intent(SplashActivity.this, ModelListActivity.class);

                intent.putExtra("flag","home");

            }else{
                intent=new Intent(SplashActivity.this,RegistrationHome.class);
            }

        }else {
            intent = new Intent(this, LoginActivity.class);
            //intent.putExtra("user", "agency");
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
