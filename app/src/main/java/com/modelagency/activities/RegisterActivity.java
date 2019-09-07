package com.modelagency.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.android.volley.Request;
import com.modelagency.R;
import com.modelagency.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends NetworkBaseActivity{

    private EditText editFullName,editEmail,editMobile,editPassword,editConfPassword;
    private CheckBox checkBoxTerms;
    private Button btnRegister;
    private String fullName,email,mobile,password,confPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        editFullName=(EditText)findViewById(R.id.edit_full_name);
        editEmail=(EditText)findViewById(R.id.edit_email);
        editMobile=(EditText)findViewById(R.id.edit_mobile);
        editPassword=(EditText)findViewById(R.id.edit_password);
        editConfPassword=(EditText)findViewById(R.id.edit_conf_password);
        checkBoxTerms=(CheckBox)findViewById(R.id.checkbox_terms_condition);

        if(!sharedPreferences.getBoolean(Constants.IS_DATABASE_CREATED,false)){
           // createDatabase();
        }

        btnRegister=(Button)findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }

    public void attemptRegister(){
        fullName=editFullName.getText().toString();
        email=editEmail.getText().toString();
        mobile=editMobile.getText().toString();
      //  location=editLocation.getText().toString();
       // password=editPassword.getText().toString();
       // confPassword=editConfPassword.getText().toString();
        password="Vipin@12345";
        confPassword="Vipin@12345";
        boolean isChecked=checkBoxTerms.isChecked();

        View focus=null;
        boolean cancel=false;

        if(TextUtils.isEmpty(password)){
            focus=editPassword;
            cancel=true;
            editPassword.setError(getResources().getString(R.string.password_required));
        }else if(!password.equals(confPassword)){
            focus=editPassword;
            cancel=true;
            editPassword.setError(getResources().getString(R.string.password_not_match));
        }

        if(TextUtils.isEmpty(mobile)){
            focus=editMobile;
            cancel=true;
            editMobile.setError(getResources().getString(R.string.mobile_required));
        }

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
                    //showProgress(true);
                   // volleyRequest();
                   /* new Thread(new Runnable() {
                        @Override
                        public void run() {
                            okHttpRequest();
                          //  multiPartRequest();
                        }
                    }).start();*/
                    editor.putString(Constants.FULL_NAME,fullName);
                    editor.putString(Constants.EMAIL,email);
                    editor.putString(Constants.MOBILE_NO,mobile);
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                    editor.commit();
                    showToast("Account created");
                    Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else {
                    showMyDialog(getResources().getString(R.string.no_internet));
                }

            }else {
                showMyDialog(getResources().getString(R.string.accept_terms));
            }

        }
    }

    public void volleyRequest(){
        Map<String,String> params=new HashMap<>();
        params.put("name",fullName);
        params.put("username",email.split("@")[0]);
        params.put("email",email);
        params.put("password",password);
        params.put("mobile",mobile);
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
                    editor.putString(Constants.MOBILE_NO,mobile);
                    editor.putInt(Constants.USER_TYPE_ID,dataObject.getInt("user_type_id"));
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
