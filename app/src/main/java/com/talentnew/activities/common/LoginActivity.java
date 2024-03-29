package com.talentnew.activities.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.talentnew.R;
import com.talentnew.activities.agency.ModelListActivity;
import com.talentnew.activities.agency.UploadDocumentActivity;
import com.talentnew.activities.talent.JobListActivity;
import com.talentnew.models.InfoItem;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends NetworkBaseActivity {
    private static final String EMAIL = "email";
    private TextView tv_login;
    private CallbackManager callbackManager;

    private AccessToken accessToken;
    private ProfileTracker profileTracker;
    private String id,userName,mobile,email,password,facebookUserId;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       // FacebookSdk.sdkInitialize(getApplicationContext());
      //  AppEventsLogger.activateApp(this);

        tv_login = findViewById(R.id.tv_login);
        Typeface typeface = Utility.getFreeHandFont(this);
        tv_login.setTypeface(typeface);
        ImageView iv_facebook =findViewById(R.id.iv_facebook);
        ImageView iv_google =findViewById(R.id.iv_google);

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

        iv_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleLogin();
            }
        });

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken= loginResult.getAccessToken();
                facebookUserId =accessToken.getUserId();
                Log.i("Login","Permissions: "+accessToken.getPermissions().toString());
                if(Profile.getCurrentProfile() == null) {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            int dimensionPixelSize = getResources().getDimensionPixelSize(com.facebook.R.dimen.com_facebook_profilepictureview_preset_size_large);
                            Uri profilePictureUri= profile2.getProfilePictureUri(dimensionPixelSize , dimensionPixelSize);
                            id =  profile2.getId();
                            editor.putString(Constants.SOCIAL_ID,profile2.getId());
                            editor.putString(Constants.FIRST_NAME,profile2.getFirstName());
                            editor.putString(Constants.LAST_NAME,profile2.getLastName());
                            editor.putString(Constants.USERNAME,profile2.getFirstName()+" "+profile2.getLastName());
                            userName = profile2.getFirstName()+" "+profile2.getLastName();
                            editor.putString(Constants.PROFILE_PIC,profilePictureUri.toString());
                            editor.putBoolean(Constants.IS_LOGGED_IN,true);
                            editor.commit();
                            makeGraphRequest();
                            profileTracker.stopTracking();
                        }
                    };
                    // no need to call startTracking() on mProfileTracker
                    // because it is called by its constructor, internally.
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    int dimensionPixelSize = getResources().getDimensionPixelSize(com.facebook.R.dimen.com_facebook_profilepictureview_preset_size_large);
                    Uri profilePictureUri= profile.getProfilePictureUri(dimensionPixelSize , dimensionPixelSize);
                    id =  profile.getId();
                    editor.putString(Constants.SOCIAL_ID,profile.getId());
                    editor.putString(Constants.FIRST_NAME,profile.getFirstName());
                    editor.putString(Constants.LAST_NAME,profile.getLastName());
                    editor.putString(Constants.USERNAME,profile.getFirstName()+" "+profile.getLastName());
                    userName = profile.getFirstName()+" "+profile.getLastName();
                    editor.putString(Constants.PROFILE_PIC,profilePictureUri.toString());
                    editor.putBoolean(Constants.IS_LOGGED_IN,true);
                   // editor.commit();
                    makeGraphRequest();
                }

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.i("Login","Error "+error.toString());
            }
        });

    }

    private void makeGraphRequest(){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        try {
                            email = object.getString("email");
                            password = "123456";
                            editor.putString(Constants.EMAIL,email);
                            attemptLogin();
                            Log.i(TAG,"Facebook login successfully.");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Registration","Login "+object.toString());

                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void googleLogin(){
// Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            email = account.getEmail();
            id =  account.getId();
            editor.putString(Constants.SOCIAL_ID,id);
            password = "123456";
            userName = account.getDisplayName();
            if(userName.contains(" ")){
                String[] nameArray = userName.split(" ");
                editor.putString(Constants.FIRST_NAME,nameArray[0]);
                editor.putString(Constants.LAST_NAME,nameArray[nameArray.length - 1]);
            }else{
                editor.putString(Constants.FIRST_NAME,userName);
                editor.putString(Constants.LAST_NAME,"");
            }
            editor.putString(Constants.EMAIL,email);
            editor.putString(Constants.USERNAME,userName);
           // editor.putString(Constants.PROFILE_PIC,account.getPhotoUrl().toString());
            attemptLogin();
            Log.i(TAG,"Google login successfully.");
        }else{
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, 2);
        }
        // updateUI(account);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            email = account.getEmail();
            id =  account.getId();
            editor.putString(Constants.SOCIAL_ID,id);
            password = "123456";
            userName = account.getDisplayName();
            if(userName.contains(" ")){
                String[] nameArray = userName.split(" ");
                editor.putString(Constants.FIRST_NAME,nameArray[0]);
                editor.putString(Constants.LAST_NAME,nameArray[nameArray.length - 1]);
            }else{
                editor.putString(Constants.FIRST_NAME,userName);
                editor.putString(Constants.LAST_NAME,"");
            }
            editor.putString(Constants.EMAIL,email);
            editor.putString(Constants.USERNAME,userName);
           // editor.putString(Constants.PROFILE_PIC,account.getPhotoUrl().toString());
            Log.i(TAG,"Google login successfully.");
            attemptLogin();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode()+" message"+e.getMessage());
            showMyDialog("Unable to login");
        }
    }

    private void attemptLogin(){
        Map<String,String> params = new HashMap<>();
        params.put("userName",userName);
      //  params.put("mobile",mobile);
        params.put("email",email);
        params.put("password",id);
        params.put("socialId",id);
        params.put("fcmToken",sharedPreferences.getString(Constants.FCM_TOKEN,""));
        String url = getResources().getString(R.string.url)+Constants.CREATE_USER;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"createUser");

      //  editor.putBoolean(Constants.IS_LOGGED_IN,true);
     //   editor.commit();
      /*  Intent intent = new Intent(LoginActivity.this,RegistrationHome.class);
        //intent.putExtra("email",email);
        startActivity(intent);
        finish();*/
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try {
            if(apiName.equals("createUser")){
                if(jsonObject.getBoolean("status")){
                    editor.putBoolean(Constants.IS_USER_CREATED,true);
                    if(jsonObject.getInt("statusCode") == 0){
                        JSONObject dataObject = jsonObject.getJSONObject("result");
                        editor.putBoolean(Constants.IS_REGISTERED,true);
                        editor.putString(Constants.USER_ID,jsonObject.getJSONObject("result").getString("id"));
                        editor.putString(Constants.EMAIL,jsonObject.getJSONObject("result").getString("email"));
                        editor.putString(Constants.MOBILE_NO,jsonObject.getJSONObject("result").getString("mobile"));
                        editor.putString(Constants.USER_TYPE,jsonObject.getJSONObject("result").getString("userType"));
                        editor.putString(Constants.TOKEN,jsonObject.getJSONObject("result").getString("token"));
                        editor.putBoolean(Constants.IS_LOGGED_IN,true);
                        editor.commit();
                        Intent intent;
                        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("model")) {
                            intent = new Intent(LoginActivity.this, JobListActivity.class);
                            editor.putString(Constants.GENRE,dataObject.getString("genre"));
                            editor.putString(Constants.GENDER,dataObject.getString("gender"));
                            editor.putString(Constants.DOB,dataObject.getString("birthDay"));
                            editor.putString(Constants.LOCATION,dataObject.getString("location"));
                           // editor.putString(Constants.,dataObject.getString("city"));
                            editor.putString(Constants.PROFILE_PIC,dataObject.getString("profilePic"));
                            editor.putString(Constants.BANNER_PIC,dataObject.getString("bannerPic"));
                            /*editor.putString(Constants.BOOST_TITLE,dataObject.getString("title"));
                            editor.putInt(Constants.BOOST_PHOTO_SHOOT,dataObject.getInt("photoshoot"));
                            editor.putString(Constants.BOOST_PHOTO_LOCATION,dataObject.getString("photoShootLocation"));
                            editor.putString(Constants.BOOST_ONLINE_COURSE,dataObject.getString("onlineCourse"));
                            editor.putString(Constants.BOOST_FEATURE_TAG,dataObject.getString("featureTag"));
                            editor.putString(Constants.BOOST_EMAIL,dataObject.getString("allowedEmail"));
                            editor.putString(Constants.BOOST_SCHEME,dataObject.getString("scheme"));
                            editor.putString(Constants.BOOST_VALIDITY,dataObject.getString("validity"));
                            editor.putFloat(Constants.BOOST_AMOUNT,(float)dataObject.getDouble("amount"));
                            editor.putInt(Constants.BOOST_APPLY_JOB,dataObject.getInt("applyJob"));
                            editor.putString(Constants.BOOST_START_DATE,dataObject.getString("startDate"));
                            editor.putString(Constants.BOOST_END_DATE,dataObject.getString("endDate"));
                            editor.putString(Constants.BOOST_RENEW_DATE,dataObject.getString("renewDate"));
                            editor.putString(Constants.BOOST_ONLINE_COURSE_END_DATE,dataObject.getString("onlineCourseEndDate"));*/
                            InfoItem item = null;
                            String infoArray[] = getResources().getStringArray(R.array.profile_info_1);
                            String lebel = null;
                            for(String info : infoArray){
                                item = new InfoItem();
                                item.setShowLabel(info);
                                lebel = info.replaceAll(" ","");
                                lebel = lebel.substring(0, 1).toLowerCase() + lebel.substring(1);
                                item.setLabel(lebel);
                                if(dataObject.getString(lebel) == null || dataObject.getString(lebel).equals("null")){
                                    item.setValue("-");
                                }else{
                                    item.setValue(dataObject.getString(lebel));
                                }
                                item.setType(1);
                                dbHelper.addInfoItem(item);
                            }

                            infoArray = getResources().getStringArray(R.array.profile_info_2);
                            for(String info : infoArray) {
                                item = new InfoItem();
                                item.setShowLabel(info);
                                lebel = info.replaceAll(" ","");
                                lebel = lebel.substring(0, 1).toLowerCase() + lebel.substring(1);
                                item.setLabel(lebel);
                                if(dataObject.getString(lebel) == null || dataObject.getString(lebel).equals("null")){
                                    item.setValue("-");
                                }else{
                                    item.setValue(dataObject.getString(lebel));
                                }
                                item.setType(2);
                                dbHelper.addInfoItem(item);
                            }
                        }
                        else{
                           /* editor.putInt(Constants.SUBSC_BOOST_ID,jsonObject.getJSONObject("result").getInt("boostId"));
                            editor.putInt(Constants.SUBSC_JOB_POST,jsonObject.getJSONObject("result").getInt("jobPost"));
                            editor.putInt(Constants.SUBSC_EMIL_SHOUTOUT,jsonObject.getJSONObject("result").getInt("emailShoutOut"));
                            editor.putInt(Constants.SUBSC_FB_SHOUTOUT,jsonObject.getJSONObject("result").getInt("fbShoutOut"));
                            editor.putInt(Constants.SUBSC_BOOSTJOB,jsonObject.getJSONObject("result").getInt("boostJob"));
                            editor.putInt(Constants.SUBSC_CUSTOMADD,jsonObject.getJSONObject("result").getInt("customAdd"));
                            editor.putString(Constants.SUBSC_TITLE,jsonObject.getJSONObject("result").getString("title"));
                            editor.putString(Constants.SUBSC_DEDICATED_MANAGER,jsonObject.getJSONObject("result").getString("dedicatedManager"));
                            editor.putString(Constants.SUBSC_CONTACT_MODEL,jsonObject.getJSONObject("result").getString("contactModel"));
                            editor.putString(Constants.SUBSC_PRO_TAG,jsonObject.getJSONObject("result").getString("proTag"));
                            editor.putString(Constants.SUBSC_VERIFIED_TAG,jsonObject.getJSONObject("result").getString("verifiedTag"));
                            editor.putString(Constants.SUBSC_VALIDITY,jsonObject.getJSONObject("result").getString("validity"));
                            editor.putString(Constants.SUBSC_SCHEME,jsonObject.getJSONObject("result").getString("scheme"));
                            editor.putString(Constants.SUBSC_AMOUNT,jsonObject.getJSONObject("result").getString("amount"));*/
                            editor.putString(Constants.GST_DOC,jsonObject.getJSONObject("result").getString("gstPic"));
                            editor.putString(Constants.ID_PROOF_DOC,jsonObject.getJSONObject("result").getString("idProofPic"));
                            editor.commit();
                            final String gstPic = sharedPreferences.getString(Constants.GST_DOC,"");
                            final String idProofPic =sharedPreferences.getString(Constants.ID_PROOF_DOC, "");
                            if(TextUtils.isEmpty(idProofPic) || TextUtils.isEmpty(gstPic))
                            intent = new Intent(LoginActivity.this, UploadDocumentActivity.class);
                            else intent = new Intent(LoginActivity.this, ModelListActivity.class);
                        }

                        editor.commit();

                        intent.putExtra("flag","home");
                        startActivity(intent);
                        finish();
                    }else{
                        editor.commit();
                        Intent intent = new Intent(LoginActivity.this, RegistrationHome.class);
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
