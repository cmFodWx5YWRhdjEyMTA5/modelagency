package com.modelagency.activities.talent;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.utilities.Constants;

import de.hdodenhof.circleimageview.CircleImageView;

public class ModelContactActivity extends NetworkBaseActivity {

    private CircleImageView iv_profile_pic;
    private TextView tv_username,tv_mobile,tv_mail;

    private RelativeLayout rl_show_mobile,rl_input_mobile,rl_show_email,rl_input_email;
    private EditText et_mobile,et_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_contact);

        setToolbarDetails(this);
        init();
    }

    private void init(){
        iv_profile_pic = findViewById(R.id.iv_profile_pic);
        tv_username = findViewById(R.id.tv_username);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_mail = findViewById(R.id.tv_mail);
        rl_show_mobile = findViewById(R.id.rl_show_mobile);
        rl_input_mobile = findViewById(R.id.rl_input_mobile);
        rl_show_email = findViewById(R.id.rl_show_email);
        rl_input_email = findViewById(R.id.rl_input_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_email = findViewById(R.id.et_email);

        Intent intent = getIntent();
        String mobile = intent.getStringExtra("mobile");
        String email = intent.getStringExtra("email");
        String userName = intent.getStringExtra("name");
        String profilePic = intent.getStringExtra("profilePic");
        String userId = intent.getStringExtra("userId");

       // mobile = "";

        if(TextUtils.isEmpty(mobile)){
            rl_show_mobile.setVisibility(View.GONE);
            if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("model"))
            rl_input_mobile.setVisibility(View.VISIBLE);
        }else{
            tv_mobile.setText(mobile);
        }

        if(TextUtils.isEmpty(email)){
            rl_show_mobile.setVisibility(View.GONE);
            if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("model"))
                rl_input_mobile.setVisibility(View.VISIBLE);
        }else{
            tv_mail.setText(email);
        }

        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
            iv_profile_pic.setVisibility(View.VISIBLE);
            tv_username.setVisibility(View.VISIBLE);
            tv_username.setText(userName);
        }


    }

}
