package com.talentnew.activities.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.talentnew.R;
import com.talentnew.activities.agency.AgentProfileActivity;
import com.talentnew.activities.agency.CourseListActivity;
import com.talentnew.activities.agency.ModelListActivity;
import com.talentnew.activities.agency.PostJobActivity;
import com.talentnew.activities.talent.BasicProfileActivity;
import com.talentnew.activities.talent.JobDetailActivity;
import com.talentnew.activities.talent.JobListActivity;
import com.talentnew.activities.talent.ModelContactActivity;
import com.talentnew.activities.talent.ProfileActivity;
import com.talentnew.database.DbHelper;
import com.talentnew.utilities.Constants;

public class BaseActivity extends AppCompatActivity {

    protected  String  TAG = "Base";
    protected ProgressDialog progressDialog;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;
    protected DbHelper dbHelper;
    protected String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper=new DbHelper(this);
        sharedPreferences=getSharedPreferences(Constants.MYPREFERENCEKEY,MODE_PRIVATE);
        editor=sharedPreferences.edit();

        token = sharedPreferences.getString(Constants.TOKEN,"");
        Log.i(TAG,"token "+token);

        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        // Disable the back button
        DialogInterface.OnKeyListener keyListener = new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK;
            }
        };
        progressDialog.setOnKeyListener(keyListener);
    }

    public void setToolbarDetails(Context context){
        TextView tvTitle = findViewById(R.id.tv_title);
        RelativeLayout container = findViewById(R.id.toolbar);
        if(context instanceof JobDetailActivity){
            tvTitle.setText("Job Details");
        }else if(context instanceof ProfileActivity){
            tvTitle.setText("PROFILE");
            container.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }else if(context instanceof ModelListActivity){
            tvTitle.setText("Models");
        }else if(context instanceof CourseListActivity){
            tvTitle.setText("Courses");
        }else if(context instanceof SettingsActivity){
            tvTitle.setText("Settings");
        }else if(context instanceof BoostActivity){
            tvTitle.setText("Profile Boost");
        }else if(context instanceof PostJobActivity){
            tvTitle.setText("Post Job");
        }else if(context instanceof AgentProfileActivity){
            tvTitle.setText("My Profile");
        }else if(context instanceof WebViewActivity){
            tvTitle.setText("Webview");
        }else if(context instanceof NotificationActivity){
            tvTitle.setText("My Messages");
        }else if(context instanceof BasicProfileActivity){
            tvTitle.setText("Basic Details");
        }else if(context instanceof ModelContactActivity){
            tvTitle.setText("Contacts");
        }
    }

    public void showMyDialog(String msg) {
        //  errorNoInternet.setText("Oops... No internet");
        //  errorNoInternet.setVisibility(View.VISIBLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        // alertDialogBuilder.setTitle("Oops...No internet");
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onDialogPositiveClicked();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showMyBothDialog(String msg,String negative,String positive) {
        //  errorNoInternet.setText("Oops... No internet");
        //  errorNoInternet.setVisibility(View.VISIBLE);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set title
        // alertDialogBuilder.setTitle("Oops...No internet");
        // set dialog message
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onDialogNegativeClicked();
                    }
                })
                .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onDialogPositiveClicked();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void onDialogPositiveClicked(){

    }

    public void onDialogNegativeClicked(){

    }

    public void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }


    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showProgress(boolean show,String message){
        if(show){
            progressDialog.setMessage(message);
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    public void showProgress(boolean show){
        if(show){
            progressDialog.show();
        }else{
            progressDialog.dismiss();
        }
    }

    public void showError(boolean show,String message){
        RelativeLayout rl_error_layout = findViewById(R.id.rl_error_layout);
        TextView tv_error = findViewById(R.id.tv_error);
        if(show){
            rl_error_layout.setVisibility(View.VISIBLE);
            tv_error.setText(message);
        }else{
            rl_error_layout.setVisibility(View.GONE);
        }
    }

    public void initFooter(final Context context, int type) {

        int backColor = getResources().getColor(R.color.white);
        int textColor = getResources().getColor(R.color.primary_text_color);
        int colorTheme = getResources().getColor(R.color.bottom_nav_back_color);

        findViewById(R.id.linear_footer).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_1).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_2).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_3).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_4).setBackgroundColor(backColor);
        findViewById(R.id.separator_footer_5).setBackgroundColor(backColor);

        RelativeLayout relativeLayoutFooter1 = findViewById(R.id.relative_footer_1);
        RelativeLayout relativeLayoutFooter2 = findViewById(R.id.relative_footer_2);
        RelativeLayout relativeLayoutFooter3 = findViewById(R.id.relative_footer_3);
        RelativeLayout relativeLayoutFooter4 = findViewById(R.id.relative_footer_4);
        RelativeLayout relativeLayoutFooter5 = findViewById(R.id.relative_footer_5);

        ImageView imageViewFooter1 = findViewById(R.id.image_footer_1);
        ImageView imageViewFooter2 = findViewById(R.id.image_footer_2);
        ImageView imageViewFooter3 = findViewById(R.id.image_footer_3);
        ImageView imageViewFooter4 = findViewById(R.id.image_footer_4);
        ImageView imageViewFooter5 = findViewById(R.id.image_footer_5);

        TextView textViewFooter1 = findViewById(R.id.text_footer_1);
        TextView textViewFooter2 = findViewById(R.id.text_footer_2);
        TextView textViewFooter3 = findViewById(R.id.text_footer_3);
        TextView textViewFooter4 = findViewById(R.id.text_footer_4);
        TextView textViewFooter5 = findViewById(R.id.text_footer_5);

        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
            textViewFooter1.setText("Models");
            textViewFooter2.setText("Post Job");
            textViewFooter3.setText("Course");
            textViewFooter4.setText("Boost");
            textViewFooter5.setText("Profile");
        }

        View view1 = findViewById(R.id.separator_footer_1);
        View view2 = findViewById(R.id.separator_footer_2);
        View view3 = findViewById(R.id.separator_footer_3);
        View view4 = findViewById(R.id.separator_footer_4);
        View view5 = findViewById(R.id.separator_footer_5);

        switch (type) {
            case 0:
                imageViewFooter1.setColorFilter(colorTheme);
                textViewFooter1.setTextColor(colorTheme);
                view1.setBackgroundColor(colorTheme);
                break;
            case 1:
                imageViewFooter2.setColorFilter(colorTheme);
                textViewFooter2.setTextColor(colorTheme);
                view2.setBackgroundColor(colorTheme);
                break;
            case 2:
                imageViewFooter3.setColorFilter(colorTheme);
                textViewFooter3.setTextColor(colorTheme);
                view3.setBackgroundColor(colorTheme);
                break;
            case 3:
                imageViewFooter4.setColorFilter(colorTheme);
                textViewFooter4.setTextColor(colorTheme);
                view4.setBackgroundColor(colorTheme);
                break;
            case 4:
                imageViewFooter5.setColorFilter(colorTheme);
                textViewFooter5.setTextColor(colorTheme);
                view5.setBackgroundColor(colorTheme);
                break;
        }

        relativeLayoutFooter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("agency")) {
                    if (context instanceof ModelListActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(BaseActivity.this, ModelListActivity.class);
                        startActivity(intent);
                    }
                }else{
                    if (context instanceof JobListActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(BaseActivity.this, JobListActivity.class);
                        intent.putExtra("flag","jobs");
                        startActivity(intent);
                    }
                }
            }
        });
        relativeLayoutFooter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("agency")) {
                    if (context instanceof PostJobActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(BaseActivity.this, PostJobActivity.class);
                        startActivity(intent);
                    }
                }else{
                    if (context instanceof CourseListActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(BaseActivity.this, CourseListActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        relativeLayoutFooter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("agency")) {
                    if (context instanceof CourseListActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(BaseActivity.this, CourseListActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        relativeLayoutFooter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (context instanceof BoostActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        //  DialogAndToast.showToast("Profile clicked in Search ",BaseActivity.this);
                        Intent intent = new Intent(BaseActivity.this, BoostActivity.class);
                        if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("model"))
                            intent.putExtra("flag", "model");
                        else
                            intent.putExtra("flag", "agency");
                        startActivity(intent);
                    }

            }
        });

        relativeLayoutFooter5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getString(Constants.USER_TYPE, "").equals("agency")) {
                    if (context instanceof AgentProfileActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        //  DialogAndToast.showToast("Profile clicked in Search ",BaseActivity.this);
                        Intent intent = new Intent(BaseActivity.this, AgentProfileActivity.class);
                        startActivity(intent);
                    }
                } else {
                    if (context instanceof ProfileActivity) {
                        //DialogAndToast.showToast("Profile clicked in profile",BaseActivity.this);
                    } else {
                        Intent intent = new Intent(BaseActivity.this, ProfileActivity.class);
                        intent.putExtra("flag", "model");
                        startActivity(intent);
                    }
                }
            }
        });

    }

    public void logout(){
        //String IMEI_NO = sharedPreferences.getString(Constants.IMEI_NO,"");
        String fcmToken = sharedPreferences.getString(Constants.FCM_TOKEN,"");
        editor.clear();
      //  editor.putString(Constants.IMEI_NO,IMEI_NO);
        editor.putString(Constants.FCM_TOKEN,fcmToken);
        editor.commit();

        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }else{
            GoogleSignInOptions gso = new GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build();

            GoogleSignInClient googleSignInClient= GoogleSignIn.getClient(this,gso);
            googleSignInClient.signOut();
        }
    }

}
