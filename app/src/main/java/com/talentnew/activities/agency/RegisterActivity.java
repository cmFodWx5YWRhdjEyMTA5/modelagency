package com.talentnew.activities.agency;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.talentnew.R;
import com.talentnew.activities.common.BaseImageActivity;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.activities.talent.JobListActivity;
import com.talentnew.activities.talent.ProfileActivity;
import com.talentnew.activities.talent.TalentRegisterActivity;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseImageActivity {

    private EditText et_company_name, editMobile;
    private TextView tv_registration, tv_email;
    private Button button_upload_id_proof, button_upload_gst,button_submit;
    private String fullName,email, mobile;
    private int docType;
    private String gstFilePath, idFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tv_registration = findViewById(R.id.tv_registration);
        Typeface typeface = Utility.getFreeHandFont(this);
        tv_registration.setTypeface(typeface);

        editMobile = findViewById(R.id.et_mobile);
        et_company_name=(EditText)findViewById(R.id.et_company_name);
        et_company_name.setText(sharedPreferences.getString(Constants.USERNAME, ""));
        tv_email= findViewById(R.id.tv_email);
        tv_email.setText(sharedPreferences.getString(Constants.EMAIL, ""));
        button_upload_id_proof = findViewById(R.id.button_upload_id_proof);
        button_upload_gst = findViewById(R.id.button_upload_gst);

        button_upload_gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docType = 1;
               selectImage();
            }
        });
        button_upload_id_proof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                docType = 2;
                selectImage();
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
        if(!TextUtils.isEmpty(gstFilePath))
        params.put("gstPic", convertToBase64(new File(gstFilePath)));
        if(!TextUtils.isEmpty(idFilePath))
        params.put("idProofPic", convertToBase64(new File(idFilePath)));
        params.put("password",sharedPreferences.getString(Constants.SOCIAL_ID,""));
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

    @Override
    protected void imageAdded() {
        super.imageAdded();
        if(docType==1){
            File file = new File(imagePath);
            TextView tvGst = findViewById(R.id.tv_gst);
            tvGst.setText(file.getName());
            gstFilePath = imagePath;
        }else {
            File file = new File(imagePath);
            TextView tv_id_proof = findViewById(R.id.tv_id_proof);
            tv_id_proof.setText(file.getName());
            idFilePath = imagePath;
        }
    }
}
