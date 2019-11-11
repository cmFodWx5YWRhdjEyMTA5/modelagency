package com.talentnew.activities.talent;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.android.volley.Request;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.talentnew.R;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.models.InfoItem;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TalentRegisterActivity extends NetworkBaseActivity {

    private EditText et_first_name,et_last_name,et_mobile,et_otp;
    private TextView tv_registration;
    private Button button_submit;
    private String firstName,lastName,mobile,otp;

    //firebase auth object
    private FirebaseAuth mAuth;
    private String mVerificationId;
    private boolean generateOTP = true,verifyOtp,otpAutoDetected;

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
                mobile = et_mobile.getText().toString();
                if(TextUtils.isEmpty(mobile)){
                    showMyDialog("Please enter mobile number.");
                    return;
                }else if(mobile.length() != 10){
                    showMyDialog("Please enter valid mobile number.");
                    return;
                }
                if(generateOTP){
                    showProgress(true);
                    initFirebaseOtp(mobile);
                }else if(verifyOtp){
                    verifyVerificationCode(otp);
                }

               // onRegister();
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

                        if(dbHelper.getAllInfoItem(1).size() == 0){
                            InfoItem item = null;
                            String infoArray[] = getResources().getStringArray(R.array.profile_info_1);
                            String lebel = null;
                            for(String info : infoArray){
                                item = new InfoItem();
                                item.setShowLabel(info);
                                item.setValue("-");
                                lebel = info.replaceAll(" ","");
                                lebel = lebel.substring(0, 1).toLowerCase() + lebel.substring(1);
                                item.setLabel(lebel);
                                item.setType(1);
                                dbHelper.addInfoItem(item);
                            }

                            infoArray = getResources().getStringArray(R.array.profile_info_2);
                            for(String info : infoArray) {
                                item = new InfoItem();
                                item.setShowLabel(info);
                                item.setValue("-");
                                lebel = info.replaceAll(" ","");
                                lebel = lebel.substring(0, 1).toLowerCase() + lebel.substring(1);
                                item.setLabel(lebel);
                                item.setType(2);
                                dbHelper.addInfoItem(item);
                            }
                        }
                        Intent intent = new Intent(TalentRegisterActivity.this, BasicProfileActivity.class);
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

    private void initFirebaseOtp(String mobile){
        mAuth = FirebaseAuth.getInstance();
        sendVerificationCode(mobile);
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);

        generateOTP = false;
        verifyOtp = true;
        button_submit.setText("Verify");
    }

    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            otpAutoDetected = true;
            //Getting the code sent by SMS
            otp = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (otp != null) {
                et_otp.setText(otp);
                //verifying the code
                showProgress(true);
                verifyVerificationCode(otp);
            }else{
                onRegister();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            showToast(e.getMessage());
            generateOTP = true;
            button_submit.setText("Get OTP");
            showProgress(false);
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Log.i(TAG,"Code sent");
            //storing the verification id that is sent to the user
            mVerificationId = s;
            generateOTP = false;
            verifyOtp = true;
            button_submit.setText("Verify");
            showProgress(false);
        }
    };

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(TalentRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           onRegister();
                        } else {

                            showProgress(false);
                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

}
