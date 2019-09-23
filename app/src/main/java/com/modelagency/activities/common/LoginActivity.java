package com.modelagency.activities.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.modelagency.R;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends NetworkBaseActivity {
    private static final String EMAIL = "email";
    private TextView tv_login;
    private CallbackManager callbackManager;

    private AccessToken accessToken;
    private ProfileTracker profileTracker;
    private String email,password,facebookUserId;

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
                            editor.putString(Constants.USER_ID,profile2.getId());
                            editor.putString(Constants.FIRST_NAME,profile2.getFirstName());
                            editor.putString(Constants.LAST_NAME,profile2.getLastName());
                            editor.putString(Constants.USERNAME,profile2.getFirstName()+" "+profile2.getLastName());
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
                    editor.putString(Constants.USER_ID,profile.getId());
                    editor.putString(Constants.FIRST_NAME,profile.getFirstName());
                    editor.putString(Constants.LAST_NAME,profile.getLastName());
                    editor.putString(Constants.USERNAME,profile.getFirstName()+" "+profile.getLastName());
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
            password = "123456";
            String name = account.getDisplayName();
            if(name.contains(" ")){
                String[] nameArray = name.split(" ");
                editor.putString(Constants.FIRST_NAME,nameArray[0]);
                editor.putString(Constants.LAST_NAME,nameArray[nameArray.length - 1]);
            }else{
                editor.putString(Constants.FIRST_NAME,name);
                editor.putString(Constants.LAST_NAME,"");
            }
            editor.putString(Constants.EMAIL,email);
            editor.putString(Constants.USERNAME,account.getDisplayName());
            editor.putString(Constants.PROFILE_PIC,account.getPhotoUrl().toString());
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
            password = "123456";
            String name = account.getDisplayName();
            if(name.contains(" ")){
                String[] nameArray = name.split(" ");
                editor.putString(Constants.FIRST_NAME,nameArray[0]);
                editor.putString(Constants.LAST_NAME,nameArray[nameArray.length - 1]);
            }else{
                editor.putString(Constants.FIRST_NAME,name);
                editor.putString(Constants.LAST_NAME,"");
            }
            editor.putString(Constants.EMAIL,email);
            editor.putString(Constants.USERNAME,account.getDisplayName());
            editor.putString(Constants.PROFILE_PIC,account.getPhotoUrl().toString());
            Log.i(TAG,"Google login successfully.");
            attemptLogin();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            showMyDialog("Unable to login");
        }
    }

    private void attemptLogin(){
        editor.commit();
        Intent intent = new Intent(LoginActivity.this,RegistrationHome.class);
        //intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

}
