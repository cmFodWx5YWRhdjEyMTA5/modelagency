package com.modelagency.utilities;

/**
 * Created by Shweta on 8/10/2016.
 */
public class Constants {

    public static String APP_NAME="ModelAgency";

    public static String MYPREFERENCEKEY="com."+APP_NAME+".MyPrefs";
    public static String USER_ID="userID";
    public static String SOCIAL_ID="socialId";
    public static String USER_TYPE="userType";
    public static String EMAIL="email";
    public static String PASSWORD="password";
    public static String MOBILE_NO="mobileNo";
   // public static String DOB="dob";
    public static String LOCATION="location";
    public static String FULL_NAME="fullName";
    public static String USERNAME="username";
    public static String ROLE="role";
    public static String ACTIVATE_KEY="activate_key";
    public static String GUID="guid";
    public static String TOKEN="token";
    public static String CREATED="created";
    public static String MODIFIED="modified";
    public static String FORGOT_PASSWORD_REQUEST_TIME="forgot_password_request_time";
    public static String FIRST_NAME="firstName";
    public static String LAST_NAME="lastName";
    public static String IS_LOGGED_IN="isLoggedIn";
    public static String IS_USER_CREATED="isUserCreated";
    public static String IS_REGISTERED="isRegistered";
    public static String PROFILE_PIC="profilePic";
    public static String VEHICLE_TYPE="vehicleType";

    public static String FCM_TOKEN="fcmToken";
    public static String IS_TOKEN_SAVED="isTokenSaved";
    public static String DEVICE_ID="deviceId";
    public static String NOTIFICATION_COUNTER="notificationCounter";

    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String PACKAGE_NAME =
            "com."+APP_NAME;
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME +
            ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +
            ".LOCATION_DATA_EXTRA";
    public static final String STATUS = PACKAGE_NAME +
            ".STATUS";

    public static String CREATE_USER="/useradmin/create_user";
    public static String CREATE_MODEL="/useradmin/create_model";
    public static String CREATE_AGENCY="/useradmin/create_agency";
}
