package com.modelagency.activities.talent;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.adapters.HomeTabPagerAdapter;
import com.modelagency.fragments.ProfileInfoFragment;
import com.modelagency.fragments.ProfilePortfolioFragment;
import com.modelagency.interfaces.OnFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends NetworkBaseActivity implements OnFragmentInteractionListener {

    private ProfileInfoFragment profileInfoFragment;
    private ProfilePortfolioFragment profilePortfolioFragment;
    private HomeTabPagerAdapter homeTabPagerAdapter;
    private ViewPager mViewPager,viewPagerImages;
    private TextView tv_save;
    private ImageView ivClose,ivDelete;
    private RelativeLayout rlToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        initFooter(this, 4);
        //setToolbarDetails(this);
        initViews();

    }

    private void initViews(){
        tv_save = findViewById(R.id.tv_save);
        ivClose = findViewById(R.id.iv_close);
        ivDelete = findViewById(R.id.iv_delete);
        rlToolbar = findViewById(R.id.toolbar);
        List<Fragment> fragmentList = new ArrayList<>();
        profileInfoFragment = ProfileInfoFragment.newInstance("editProfile","");
        profilePortfolioFragment = ProfilePortfolioFragment.newInstance("editProfile","");
        fragmentList.add(profileInfoFragment);
        fragmentList.add(profilePortfolioFragment);

        homeTabPagerAdapter = new HomeTabPagerAdapter(getSupportFragmentManager(),fragmentList);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mViewPager.setAdapter(homeTabPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TextView tvSave = findViewById(R.id.tv_save);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePortfolioFragment.clearAll();
                rlToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                ivClose.setVisibility(View.GONE);
                ivDelete.setVisibility(View.GONE);
                tv_save.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onFragmentInteraction(Object ob, int type) {
       if(type == 1){
          rlToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          ivClose.setVisibility(View.VISIBLE);
          ivDelete.setVisibility(View.VISIBLE);
          tv_save.setVisibility(View.GONE);
       }else if(type == 2){
           rlToolbar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
           ivClose.setVisibility(View.GONE);
           ivDelete.setVisibility(View.GONE);
           tv_save.setVisibility(View.VISIBLE);
       }
    }

}
