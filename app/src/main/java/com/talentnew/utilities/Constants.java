package com.talentnew.utilities;

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
 public static String GENDER="gender";
 public static String DOB="dob";
 public static String TOKEN="token";
 public static String GST_DOC="gst_doc";
 public static String ID_PROOF_DOC="id_proof_doc";
 public static String CREATED="created";
 public static String MODIFIED="modified";
 public static String FORGOT_PASSWORD_REQUEST_TIME="forgot_password_request_time";
 public static String FIRST_NAME="firstName";
 public static String LAST_NAME="lastName";
 public static String IS_LOGGED_IN="isLoggedIn";
 public static String IS_USER_CREATED="isUserCreated";
 public static String IS_REGISTERED="isRegistered";
 public static String PROFILE_PIC="profilePic";
 public static String BANNER_PIC="bannerPic";
 public static String VIDEO_FILE="videoFile";
 public static String VIDEO_FILE_ID="videoFileId";

 public static String FCM_TOKEN="fcmToken";
 public static String IS_TOKEN_SAVED="isTokenSaved";
 public static String DEVICE_ID="deviceId";
 public static String NOTIFICATION_COUNTER="notificationCounter";

 public static String HEIGHT="height";
 public static String WEIGHT="weight";
 public static String BREAST="breast";
 public static String WAIST="waist";
 public static String HIP="hip";
 public static String EXPERIENCE="experience";

 public static String ETHNICITY="ethnicity";
 public static String HAIR_COLOR="hairColor";
 public static String HAIR_LENGTH="hairLength";
 public static String EYE_COLOR="eyeColor";
 public static String SKIN_COLOR="skinColor";
 public static String ACTING_EDUCATION="actingEducation";
 public static String GENRE="genre";

 //Models
 public static String BOOST_TITLE="boostTitle";
 public static String BOOST_PHOTO_SHOOT="boostPhotoShoot";
 public static String BOOST_PHOTO_LOCATION="boostPhotoShoot";
 public static String BOOST_ONLINE_COURSE="boostOnlineCourse";
 public static String BOOST_FEATURE_TAG="boostFeatureTag";
 public static String BOOST_EMAIL="boostEmail";
 public static String BOOST_SCHEME="boostScheme";
 public static String BOOST_VALIDITY="boostValidity";
 public static String BOOST_AMOUNT="boostAmount";
 public static String BOOST_APPLY_JOB="boostApplyJob";
 public static String BOOST_START_DATE="boostStartDate";
 public static String BOOST_END_DATE="boostEndDate";


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
 public static String UPLOAD_AGENCY_DOCUMENTS="/api/profile/agency/upload_docs";
 public static String UPDATE_AGENCY="/api/profile/agency/update";
 public static String UPDATE_MODEL_PROFILE="/api/profile/update_model";
 public static String UPDATE_MODEL_BASIC_DETAILS="/api/profile/update_basic_details";
 public static String GET_COURSES="/api/course/get_all_courses";
 public static String GET_JOBS_FOR_MODEL="/api/job/get_model_jobs";
 public static String GET_APPLIED_JOB_MODEL="/api/job/get_applied_jobs";
 public static String GET_MODEL_BOOST="/api/boost/get_all_boost";
 public static String GET_AGENCY_BOOST="/api/boost/get_all_agency_boost";
 public static String APPLY_JOB="/api/job/apply_job";
 public static String UPDATE_VIEWS="/api/job/update_views";
 public static String CREATE_ALBUM="/api/profile/create_album";
 public static String GET_ALBUM="/api/profile/get_album";
    public static String GET_VIDEO="/api/profile/get_video";
 public static String UPLOAD_PHOTO="/api/profile/upload_photo";
 public static String UPLOAD_BANNER="/api/profile/upload_banner";
 public static String UPLOAD_VIDEO="/api/profile/upload_video";
    public static String ADD_VIDEO_URL="/api/profile/add_video_url";

 public static String GET_JOBS_FOR_AGENCY="/api/job/get_agency_jobs";
 public static String POST_JOBS="/api/job/post_job";
 public static String GET_APPLIED_MODEL="/api/job/get_applied_models";
 public static String GET_ALL_MODEL="/api/model/get_model_list";

 public static String GET_NOTIFICATIONS="/api/notification/get_notification";
}
