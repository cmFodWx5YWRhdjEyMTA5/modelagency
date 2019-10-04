package com.modelagency.activities.talent;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.modelagency.R;
import com.modelagency.activities.common.BaseImageActivity;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class BasicProfileActivity extends BaseImageActivity {

    private EditText et_username,et_location,et_dob;
    private TextView tv_female,tv_male;
    private String gender,location,username,birthDay;
    private Button btn_save;
    private CircleImageView iv_profile_pic;

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);
        //Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        setToolbarDetails(this);
      //  initFooter(this,10);

        init();
    }

    private void init(){
        et_username =findViewById(R.id.et_username);
        et_location =findViewById(R.id.et_location);
        et_dob =findViewById(R.id.et_dob);
        tv_female =findViewById(R.id.tv_female);
        tv_male =findViewById(R.id.tv_male);
        iv_profile_pic =findViewById(R.id.iv_profile_pic);
        btn_save =findViewById(R.id.btn_save);
        username = sharedPreferences.getString(Constants.USERNAME,"");
        gender = sharedPreferences.getString(Constants.GENDER,"");
        location = sharedPreferences.getString(Constants.LOCATION,"");
        birthDay = sharedPreferences.getString(Constants.DOB,"");
        et_username.setText(username);
        et_location.setText(location);
        et_dob.setText(birthDay);

        if(!TextUtils.isEmpty(gender)){
            if(gender.equals("F")){
                tv_female.setBackgroundResource(R.drawable.accent_solid_circle);
                tv_female.setTextColor(getResources().getColor(R.color.white));
                Utility.setColorFilter(tv_female.getBackground(),getResources().getColor(R.color.grey900));
                tv_male.setBackgroundResource(R.drawable.black_stroke_white_circle_background);
                tv_male.setTextColor(getResources().getColor(R.color.primary_text_color));
            }else{
                tv_male.setBackgroundResource(R.drawable.accent_solid_circle);
                tv_male.setTextColor(getResources().getColor(R.color.white));
                Utility.setColorFilter(tv_male.getBackground(),getResources().getColor(R.color.grey900));
                tv_female.setBackgroundResource(R.drawable.black_stroke_white_circle_background);
                tv_female.setTextColor(getResources().getColor(R.color.primary_text_color));
            }
        }

        tv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_female.setBackgroundResource(R.drawable.accent_solid_circle);
                tv_female.setTextColor(getResources().getColor(R.color.white));
                Utility.setColorFilter(tv_female.getBackground(),getResources().getColor(R.color.grey900));
                tv_male.setBackgroundResource(R.drawable.black_stroke_white_circle_background);
                tv_male.setTextColor(getResources().getColor(R.color.primary_text_color));
                gender = "F";
            }
        });

        tv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_male.setBackgroundResource(R.drawable.accent_solid_circle);
                tv_male.setTextColor(getResources().getColor(R.color.white));
                Utility.setColorFilter(tv_male.getBackground(),getResources().getColor(R.color.grey900));
                tv_female.setBackgroundResource(R.drawable.black_stroke_white_circle_background);
                tv_female.setTextColor(getResources().getColor(R.color.primary_text_color));
                gender = "M";
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()){
                    saveProfile();
                }else{
                    showMyDialog(getResources().getString(R.string.no_internet));
                }
            }
        });

        ImageView iv_upload_profile_pic = findViewById(R.id.iv_upload_profile_pic);

        iv_upload_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerDialog=new DatePickerDialog(BasicProfileActivity.this,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int yr, int mon, int dy) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,yr);
                cal.set(Calendar.MONTH,mon);
                cal.set(Calendar.DATE,dy);
                et_dob.setText(Utility.parseDate(cal,"dd/MM/yyyy"));
            }
        },year,month,day);
        datePickerDialog.setCancelable(false);
        datePickerDialog.setMessage("Select Birthday");

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

    }

    private void saveProfile(){

        username = et_username.getText().toString();
        location = et_location.getText().toString();
        birthDay = et_dob.getText().toString();

        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        if(!TextUtils.isEmpty(imagePath))
        params.put("profilePic",convertToBase64(new File(imagePath)));
        params.put("gender",gender);
        params.put("location",location);
        params.put("birthDay",birthDay);
        String url = getResources().getString(R.string.url)+Constants.UPDATE_MODEL_BASIC_DETAILS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateProfile");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("updateProfile")){
                if(jsonObject.getBoolean("status")){
                    if(!TextUtils.isEmpty(imagePath))
                    editor.putString(Constants.PROFILE_PIC,imagePath);

                    editor.putString(Constants.USERNAME,username);
                    editor.putString(Constants.LOCATION,location);
                    editor.putString(Constants.GENDER,gender);
                    editor.putString(Constants.DOB,birthDay);
                    editor.commit();
                }
                showMyDialog(jsonObject.getString("message"));
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    protected void imageAdded(){
        Glide.with(this)
                .load(imagePath)
                .into(iv_profile_pic);
    }

}
