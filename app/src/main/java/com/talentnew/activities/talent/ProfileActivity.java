package com.talentnew.activities.talent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.talentnew.R;
import com.talentnew.activities.common.BoostActivity;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.activities.common.SettingsActivity;
import com.talentnew.adapters.HomeTabPagerAdapter;
import com.talentnew.fragments.ProfileInfoFragment;
import com.talentnew.fragments.ProfilePortfolioFragment;
import com.talentnew.fragments.ProfileVidoFragment;
import com.talentnew.interfaces.OnFragmentInteractionListener;
import com.talentnew.models.InfoItem;
import com.talentnew.models.MyModel;
import com.talentnew.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends NetworkBaseActivity implements OnFragmentInteractionListener {

    private ProfileInfoFragment profileInfoFragment;
    private ProfilePortfolioFragment profilePortfolioFragment;
    private ProfileVidoFragment profileVidoFragment;
    private HomeTabPagerAdapter homeTabPagerAdapter;
    private ViewPager mViewPager,viewPagerImages;
    private String flag, profileImage, bannerImage, name, address;
    private MyModel model;
    private TextView text_address, tv_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        if(data != null){
            Log.i(TAG,"action "+action+" data "+data.getPath()+" "+data.getQueryParameter("id"));
            flag = "modelProfile";
            getModel(data.getQueryParameter("id"));
        }else{
            Log.i(TAG,"action "+action+" data null");
            flag = getIntent().getStringExtra("flag");
        }




        if(flag.equals("modelProfile")) {
            initFooter(this, 4);
            /*model =(MyModel) getIntent().getSerializableExtra("model");
            profileImage = model.getProfilePic();
            bannerImage = model.getBannerPic();
            name= model.getName();
            address = model.getAddress();*/
        }else if(flag.equals("ModelList") || flag.equals("JobApplicant")) {
            if(flag.equals("JobApplicant"))
            initFooter(this, 4);
            else initFooter(this, 0);
            model =(MyModel) getIntent().getSerializableExtra("model");
            profileImage = model.getProfilePic();
            bannerImage = model.getBannerPic();
            name= model.getName();
            address = model.getAddress();
            initViews();
        }
        else {
            initFooter(this, 4);
            profileImage = sharedPreferences.getString(Constants.PROFILE_PIC,"");
            bannerImage = sharedPreferences.getString(Constants.BANNER_PIC,"");
            name= sharedPreferences.getString(Constants.USERNAME,"");
            address = sharedPreferences.getString(Constants.LOCATION,"");
            initViews();
        }

    }

    private void getModel(String id){
        String url = getResources().getString(R.string.url)+"/useradmin/get_model_profile?id="+id;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(),"getModel");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getModel")){
                if(jsonObject.getBoolean("status")){
                    JSONObject dataObject = jsonObject.getJSONObject("result");
                    model = new MyModel();
                    model.setId(dataObject.getString("id"));
                    if(dataObject.getInt("isActive")==0)
                        model.setActive(false);
                    else model.setActive(true);
                    model.setName(dataObject.getString("userName"));
                    model.setMobile(dataObject.getString("mobile"));
                    model.setEmail(dataObject.getString("email"));
                    model.setProfilePic(dataObject.getString("profilePic"));
                    model.setFeatureTag(dataObject.getString("featureTag"));
                    model.setFcmToken(dataObject.getString("fcmToken"));
                    model.setBannerPic(dataObject.getString("bannerPic"));
                    model.setHeight(dataObject.getString("height"));
                    model.setWeight(dataObject.getString("waist"));
                    model.setBreast(dataObject.getString("breast"));
                    model.setWeight(dataObject.getString("weight"));
                    model.setHip(dataObject.getString("hip"));
                    model.setExperience(dataObject.getString("experience"));
                    model.setEthnicity(dataObject.getString("ethnicity"));
                    model.setSkinColor(dataObject.getString("skinColor"));
                    model.setHairColor(dataObject.getString("hairColor"));
                    model.setEyeColor(dataObject.getString("eyeColor"));
                    model.setHairLength(dataObject.getString("hairLength"));
                    model.setActingEducation(dataObject.getString("actingEducation"));
                    model.setGenre(dataObject.getString("genre"));
                    profileImage = model.getProfilePic();
                    bannerImage = model.getBannerPic();
                    name= model.getName();
                    address = model.getAddress();
                    initViews();
                }else{
                    showMyDialog(jsonObject.getString("message"));
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    private void initViews(){

        CircleImageView iv_profile_pic = findViewById(R.id.iv_profile_pic);
        ImageView iv_banner_image = findViewById(R.id.iv_banner_image);
        text_address = findViewById(R.id.text_address);
        text_address.setText(address);
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(name);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.dontTransform();
        //requestOptions.override(Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size),
        //        context), Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size), context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);
        Glide.with(this)
                .load(profileImage)
                .apply(requestOptions)
                .error(R.drawable.model_2)
                .into(iv_profile_pic);

        Glide.with(this)
                .load(bannerImage)
                .apply(requestOptions)
                .error(R.drawable.model_2)
                .into(iv_banner_image);

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


        List<Fragment> fragmentList = new ArrayList<>();
        profileInfoFragment = ProfileInfoFragment.newInstance("showProfile","");
        profilePortfolioFragment = ProfilePortfolioFragment.newInstance("showProfile","");
        profileVidoFragment = ProfileVidoFragment.newInstance("showProfile","");
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency") ||
                flag.equals("modelProfile")){
            profileInfoFragment.setMyModel(model);
            profilePortfolioFragment.setModelId(model.getId());
            profileVidoFragment.setModelId(model.getId());
        }
        fragmentList.add(profileInfoFragment);
        fragmentList.add(profilePortfolioFragment);
        fragmentList.add(profileVidoFragment);

        homeTabPagerAdapter = new HomeTabPagerAdapter(getSupportFragmentManager(),fragmentList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(homeTabPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        ImageView ivEdit = findViewById(R.id.iv_edit);
        ImageView iv_setting = findViewById(R.id.iv_setting);
        if(flag.equals("ModelList") || flag.equals("viewModel")) {
            ivEdit.setVisibility(View.GONE);
            iv_setting.setVisibility(View.GONE);
        }
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        ImageView iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView iv_share = findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareProfile();
            }
        });


        TextView tv_contact = findViewById(R.id.tv_contact);

        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ModelContactActivity.class);
                if(flag.equals("ModelList") || flag.equals("JobApplicant")) {
                    if(sharedPreferences.getString(Constants.SUBSC_CONTACT_MODEL, "").equals("1")) {
                        intent.putExtra("mobile", model.getMobile());
                        intent.putExtra("email", model.getEmail());
                        intent.putExtra("userId", model.getId());
                        intent.putExtra("name", model.getName());
                        intent.putExtra("profilePic", model.getProfilePic());
                        startActivity(intent);
                    }else showMyDialog("Please Boost your profile to view models contact");
                }else {
                    intent.putExtra("mobile", sharedPreferences.getString(Constants.MOBILE_NO, ""));
                    intent.putExtra("email", sharedPreferences.getString(Constants.EMAIL, ""));
                    intent.putExtra("userId", sharedPreferences.getString(Constants.USER_ID, ""));
                    intent.putExtra("name", sharedPreferences.getString(Constants.USERNAME, ""));
                    intent.putExtra("profilePic", sharedPreferences.getString(Constants.PROFILE_PIC, ""));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onFragmentInteraction(Object ob, int position,int type) {

    }

    @Override
    public void onDialogPositiveClicked(){
        startActivity(new Intent(this, BoostActivity.class));
        finish();
    }

    private void shareProfile(){
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String sAux = "\n "+sharedPreferences.getString(Constants.USERNAME,"")+ "\n\n";
            sAux = sAux + "http://www.talentnew.com/profile_model?id="+sharedPreferences.getString(Constants.USER_ID,"")+"\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "Choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }
}