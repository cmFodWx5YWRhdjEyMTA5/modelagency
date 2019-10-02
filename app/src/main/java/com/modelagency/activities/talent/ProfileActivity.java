package com.modelagency.activities.talent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.common.SettingsActivity;
import com.modelagency.adapters.HomeTabPagerAdapter;
import com.modelagency.fragments.ProfileInfoFragment;
import com.modelagency.fragments.ProfilePortfolioFragment;
import com.modelagency.interfaces.OnFragmentInteractionListener;
import com.modelagency.models.InfoItem;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends NetworkBaseActivity implements OnFragmentInteractionListener {

    private ProfileInfoFragment profileInfoFragment;
    private ProfilePortfolioFragment profilePortfolioFragment;
    private HomeTabPagerAdapter homeTabPagerAdapter;
    private ViewPager mViewPager,viewPagerImages;
    private String flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        flag = getIntent().getStringExtra("flag");
        if(flag.equals("ModelList"))
            initFooter(this, 0);
        else
        initFooter(this, 4);
        //setToolbarDetails(this);
        initViews();
    }

    private void initViews(){
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
        fragmentList.add(profileInfoFragment);
        fragmentList.add(profilePortfolioFragment);

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
    }

    @Override
    public void onFragmentInteraction(Object ob, int type) {

    }
}