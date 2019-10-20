package com.talentnew.activities.talent;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.android.volley.Request;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.talentnew.R;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        et_last_name = findViewById(R.id.et_last_name);
        et_mobile = findViewById(R.id.et_mobile);
        et_otp = findViewById(R.id.et_otp);
        et_first_name.setText(sharedPreferences.getString(Constants.FIRST_NAME,""));
        et_last_name.setText(sharedPreferences.getString(Constants.LAST_NAME,""));

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
        mobile = et_mobile.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            showMyDialog("Please enter mobile number.");
            return;
        }else if(mobile.length() != 10){
            showMyDialog("Please enter valid mobile number.");
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("mobile",mobile);
        params.put("email",sharedPreferences.getString(Constants.EMAIL,""));
        params.put("password",sharedPreferences.getString(Constants.SOCIAL_ID,""));
        params.put("socialId",sharedPreferences.getString(Constants.SOCIAL_ID,""));
        params.put("fcmToken",sharedPreferences.getString(Constants.FCM_TOKEN,""));
        String url = getResources().getString(R.string.url)+Constants.CREATE_MODEL;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"createModel");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
            if(apiName.equals("createModel")){
                if(jsonObject.getBoolean("status")){
                    editor.putBoolean(Constants.IS_USER_CREATED,true);
                    if(jsonObject.getInt("statusCode") == 1){
                        editor.putBoolean(Constants.IS_REGISTERED,true);
                        editor.putString(Constants.USER_ID,jsonObject.getJSONObject("result").getString("id"));
                        editor.putString(Constants.USER_TYPE,jsonObject.getJSONObject("result").getString("userType"));
                        editor.putString(Constants.TOKEN,jsonObject.getJSONObject("result").getString("token"));
                        editor.commit();
                        Intent intent = new Intent(TalentRegisterActivity.this, JobListActivity.class);
                        intent.putExtra("flag","home");
                        startActivity(intent);
                        finish();
                    }
                }else{
                    showMyDialog(jsonObject.getString("message"));
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

}
