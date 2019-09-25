package com.modelagency.activities.talent;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.adapters.HomeTabPagerAdapter;
import com.modelagency.adapters.ProfileInfoAdapter;
import com.modelagency.fragments.ProfileInfoFragment;
import com.modelagency.fragments.ProfilePortfolioFragment;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.interfaces.OnFragmentInteractionListener;
import com.modelagency.models.InfoItem;

import java.util.ArrayList;
import java.util.List;

public class EditProfileActivity extends NetworkBaseActivity implements OnFragmentInteractionListener, MyItemClickListener {

    private ProfileInfoFragment profileInfoFragment;
    private ProfilePortfolioFragment profilePortfolioFragment;
    private HomeTabPagerAdapter homeTabPagerAdapter;
    private ViewPager mViewPager,viewPagerImages;
    private TextView tv_save;
    private ImageView ivClose,ivDelete;
    private RelativeLayout rlToolbar,rl_info_input;
    private TextView tv_info_header;
    private RecyclerView recyclerViewInfo;
    private ProfileInfoAdapter profileInfoAdapter;
    private List<InfoItem> infoItemList;

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
        rl_info_input = findViewById(R.id.rl_info_input);
        tv_info_header = findViewById(R.id.tv_info_header);
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

        infoItemList= new ArrayList<>();
        recyclerViewInfo = findViewById(R.id.recycler_view_info);
        recyclerViewInfo.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(this);
        recyclerViewInfo.setLayoutManager(layoutManager2);
        recyclerViewInfo.setItemAnimator(new DefaultItemAnimator());
        profileInfoAdapter=new ProfileInfoAdapter(this,infoItemList,"inputInfo");
        profileInfoAdapter.setMyItemClickListener(this);
        recyclerViewInfo.setAdapter(profileInfoAdapter);
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
       }else if(type == 3){
           rl_info_input.setVisibility(View.VISIBLE);
           InfoItem item = (InfoItem)ob;
           tv_info_header.setText(item.getLabel());
           getInfoDetails(item.getLabel());
       }else if(type == 4){

       }
    }

    private void getInfoDetails(String label){
        InfoItem item = new InfoItem();
        item.setLabel("Height");
        item.setValue("175 cm | 9'10\"");
        infoItemList.add(item);
        item = new InfoItem();
        item.setLabel("Weight");
        item.setValue("57 kg | 126 lbs");
        infoItemList.add(item);
    }

    @Override
    public void onItemClicked(int position, int type) {


    }
}
