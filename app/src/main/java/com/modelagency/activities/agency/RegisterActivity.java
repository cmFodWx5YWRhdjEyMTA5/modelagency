package com.modelagency.activities.agency;

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
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.talent.JobListActivity;
import com.modelagency.activities.talent.ProfileActivity;
import com.modelagency.activities.talent.TalentRegisterActivity;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends NetworkBaseActivity {

    private EditText editCompanyName,editEmail, editMobile;
    private TextView tv_registration;
    private Button button_upload,button_submit;
    private String fullName,email, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_registration = findViewById(R.id.tv_registration);
        Typeface typeface = Utility.getFreeHandFont(this);
        tv_registration.setTypeface(typeface);

        editMobile = findViewById(R.id.et_mobile);
        editCompanyName=(EditText)findViewById(R.id.et_company_name);
        editCompanyName.setText(sharedPreferences.getString(Constants.USERNAME, ""));
        editEmail=(EditText)findViewById(R.id.et_email);
        editEmail.setText(sharedPreferences.getString(Constants.EMAIL, ""));
        button_upload=(Button)findViewById(R.id.button_upload);

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // attemptRegister();
            }
        });

        button_submit = findViewById(R.id.button_submit);
        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegister();
            }
        });

    }

    private void onRegister(){
        mobile = editMobile.getText().toString();
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
        params.put("password",sharedPreferences.getString(Constants.PASSWORD,""));
        params.put("socialId",sharedPreferences.getString(Constants.SOCIAL_ID,""));
        params.put("fcmToken",sharedPreferences.getString(Constants.FCM_TOKEN,""));
        String url = getResources().getString(R.string.url)+Constants.CREATE_AGENCY;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"createAgency");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
            if(apiName.equals("createAgency")){
                if(jsonObject.getBoolean("status")){
                    editor.putBoolean(Constants.IS_USER_CREATED,true);
                    if(jsonObject.getInt("statusCode") == 1){
                        editor.putBoolean(Constants.IS_REGISTERED,true);
                        editor.putString(Constants.USER_ID,jsonObject.getJSONObject("result").getString("id"));
                        editor.putString(Constants.USER_TYPE,jsonObject.getJSONObject("result").getString("userType"));
                        editor.putString(Constants.TOKEN,jsonObject.getJSONObject("result").getString("token"));
                        editor.commit();
                        Intent intent = new Intent(RegisterActivity.this, ModelListActivity.class);
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
