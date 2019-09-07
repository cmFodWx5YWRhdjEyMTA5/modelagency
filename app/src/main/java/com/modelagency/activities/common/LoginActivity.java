package com.modelagency.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import com.modelagency.R;
import com.modelagency.activities.RegisterActivity;
import com.modelagency.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends NetworkBaseActivity {

    private EditText editTextEmailID,editTextPassword;
    private TextView textForgotPassword;
    private Button btnLogin,btnSignUp;
    private String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmailID=(EditText)findViewById(R.id.edit_email);
        editTextPassword=(EditText)findViewById(R.id.edit_password);
        textForgotPassword=(TextView)findViewById(R.id.text_forgot_password);
        progressDialog.setMessage(getResources().getString(R.string.logging_user));

        textForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                //startActivity(intent);
            }
        });

        //dbHelper.dropAndCreateAllTable();
        //createDatabase();



        btnLogin=(Button)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        btnSignUp=(Button)findViewById(R.id.btn_register);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void attemptLogin(){
        email=editTextEmailID.getText().toString();
        password=editTextPassword.getText().toString();
        View focus=null;
        boolean cancel=false;

        if(TextUtils.isEmpty(password)){
            editTextPassword.setError(getResources().getString(R.string.password_required));
            focus=editTextPassword;
            cancel=true;
        }

        if(TextUtils.isEmpty(email)){
            editTextEmailID.setError(getResources().getString(R.string.email_required));
            focus=editTextEmailID;
            cancel=true;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches()){
            editTextEmailID.setError(getResources().getString(R.string.valid_email));
            focus=editTextEmailID;
            cancel=true;
        }

        if(cancel){
            focus.requestFocus();
            return;
        }else {
            if(isNetworkAvailable()) {
                progressDialog.setMessage(getResources().getString(R.string.logging_user));
                //showProgress(true);
                editor.putString(Constants.FULL_NAME,"Vipin Dhama");
                editor.putString(Constants.EMAIL,email);
                editor.putString(Constants.MOBILE_NO,"9718181697");
                editor.putString(Constants.LOCATION,"Delhi");
                editor.putBoolean(Constants.IS_LOGGED_IN,true);
                editor.commit();
                showToast("Account created");
                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }else {
                showMyDialog(getResources().getString(R.string.no_internet));
            }

        }
    }

    public void volleyRequest(){
        Map<String,String> params=new HashMap<>();
        if(email.contains("@"))
            params.put("username",email.split("@")[0]);
        else
            params.put("username",email);
        params.put("password",password);
        String url=getResources().getString(R.string.url)+"/Users/login";
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"login");
    }


    @Override
    public void onJsonObjectResponse(JSONObject response, String apiName) {
        showProgress(false);
        try {
            // JSONObject jsonObject=response.getJSONObject("response");
            if(apiName.equals("login")){
                if(response.getString("status").equals("true")||response.getString("status").equals(true)){
                    JSONObject dataObject=response.getJSONObject("data");
                    editor.putString(Constants.FULL_NAME,dataObject.getString("name"));
                    editor.putString(Constants.EMAIL,dataObject.getString("email"));
                    editor.putInt(Constants.MOBILE_NO,dataObject.getInt("mobile"));
                    editor.putString(Constants.LOCATION,dataObject.getString("city"));
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
