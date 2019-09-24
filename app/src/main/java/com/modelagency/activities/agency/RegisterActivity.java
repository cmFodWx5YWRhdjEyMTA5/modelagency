package com.modelagency.activities.agency;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.talent.ProfileActivity;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends NetworkBaseActivity {

    private EditText editCompanyName,editEmail;
    private TextView tv_registration;
    private Button button_upload,button_submit;
    private String fullName,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_registration = findViewById(R.id.tv_registration);
        Typeface typeface = Utility.getFreeHandFont(this);
        tv_registration.setTypeface(typeface);

        editCompanyName=(EditText)findViewById(R.id.et_company_name);
        editEmail=(EditText)findViewById(R.id.et_email);
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
        editor.putBoolean(Constants.IS_REGISTERED,true);
        editor.commit();
        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
        //intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

    /*public void attemptRegister(){
        fullName=editFullName.getText().toString();
        email=editEmail.getText().toString();
        View focus=null;
        boolean cancel=false;

        if(TextUtils.isEmpty(email)){
            focus=editEmail;
            cancel=true;
            editEmail.setError(getResources().getString(R.string.email_required));
        }

        if(TextUtils.isEmpty(fullName)){
            focus=editFullName;
            cancel=true;
            editFullName.setError(getResources().getString(R.string.full_name_required));
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else {
            if(isChecked){
                if(isNetworkAvailable()) {
                    progressDialog.setMessage(getResources().getString(R.string.creating_account));
                    editor.putString(Constants.FULL_NAME,fullName);
                    editor.putString(Constants.EMAIL,email);
                    editor.putString(Constants.MOBILE_NO,mobile);
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                    showToast("Account created");
                    Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    showMyDialog(getResources().getString(R.string.no_internet));
                }

            }else {
                showMyDialog(getResources().getString(R.string.accept_terms));
            }

        }
    }*/

    public void volleyRequest(){
        Map<String,String> params=new HashMap<>();
        params.put("name",fullName);
        params.put("username",email.split("@")[0]);
        params.put("email",email);
        //params.put("mobile",mobile);
        String url=getResources().getString(R.string.url)+"/Users/SignUp";
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"SignUp");
    }

    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("SignUp")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject dataObject=response.getJSONObject("data");
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                    editor.putString(Constants.FULL_NAME,fullName);
                    editor.putString(Constants.EMAIL,email);
                    //editor.putString(Constants.MOBILE_NO,mobile);
                   // editor.putInt(Constants.USER_TYPE_ID,dataObject.getInt("user_type_id"));
                    editor.putString(Constants.USERNAME,dataObject.getString("username"));
                    editor.putString(Constants.ROLE,dataObject.getString("role"));
                    editor.putString(Constants.ACTIVATE_KEY,dataObject.getString("activate_key"));
                    editor.putString(Constants.GUID,dataObject.getString("guid"));
                    editor.putString(Constants.FORGOT_PASSWORD_REQUEST_TIME,dataObject.getString("forgot_password_request_time"));
                    editor.putString(Constants.STATUS,dataObject.getString("status"));
                    editor.putString(Constants.TOKEN,dataObject.getString("token"));
                    editor.putString(Constants.CREATED,dataObject.getString("created"));
                    editor.putString(Constants.MODIFIED,dataObject.getString("modified"));
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                    showToast("Account created");
                }else {
                    showMyDialog(response.getString("message"));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            showToast(getResources().getString(R.string.json_parser_error)+e.toString());
        }
    }

}
