package com.talentnew.activities.talent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.talentnew.R;
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
        flag = getIntent().getStringExtra("flag");
        if(flag.equals("ModelList") || flag.equals("JobApplicant")) {
            if(flag.equals("JobApplicant"))
            initFooter(this, 4);
            else initFooter(this, 0);
            model =(MyModel) getIntent().getSerializableExtra("model");
            profileImage = model.getProfilePic();
            bannerImage = model.getBannerPic();
            name= model.getName();
            address = model.getAddress();
        }
        else {
            initFooter(this, 4);
            profileImage = sharedPreferences.getString(Constants.PROFILE_PIC,"");
            bannerImage = sharedPreferences.getString(Constants.BANNER_PIC,"");
            name= sharedPreferences.getString(Constants.USERNAME,"");
            address = sharedPreferences.getString(Constants.LOCATION,"");
        }
        initViews();
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
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
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

        TextView tv_contact = findViewById(R.id.tv_contact);
        tv_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ModelContactActivity.class);
                if(flag.equals("ModelList")) {
                    intent.putExtra("mobile", model.getMobile());
                    intent.putExtra("email", model.getEmail());
                    intent.putExtra("userId", model.getId());
                    intent.putExtra("name", model.getName());
                    intent.putExtra("profilePic", model.getProfilePic());
                }else {
                    intent.putExtra("mobile", sharedPreferences.getString(Constants.MOBILE_NO, ""));
                    intent.putExtra("email", sharedPreferences.getString(Constants.EMAIL, ""));
                    intent.putExtra("userId", sharedPreferences.getString(Constants.USER_ID, ""));
                    intent.putExtra("name", sharedPreferences.getString(Constants.USERNAME, ""));
                    intent.putExtra("profilePic", sharedPreferences.getString(Constants.PROFILE_PIC, ""));
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Object ob, int position,int type) {

    }
}