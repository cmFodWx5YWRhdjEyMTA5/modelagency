package com.talentnew.activities.talent;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.talentnew.R;
import com.talentnew.activities.common.BaseImageActivity;
import com.talentnew.adapters.HomeTabPagerAdapter;
import com.talentnew.adapters.SimpleItemAdapter;
import com.talentnew.fragments.ProfileInfoFragment;
import com.talentnew.fragments.ProfilePortfolioFragment;
import com.talentnew.fragments.ProfileVidoFragment;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.interfaces.OnFragmentInteractionListener;
import com.talentnew.models.Album;
import com.talentnew.models.Genre;
import com.talentnew.models.InfoItem;
import com.talentnew.models.PortFolio;
import com.talentnew.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends BaseImageActivity implements OnFragmentInteractionListener, MyItemClickListener {

    private ProfileInfoFragment profileInfoFragment;
    private ProfilePortfolioFragment profilePortfolioFragment;
    private ProfileVidoFragment profileVidoFragment;
    private HomeTabPagerAdapter homeTabPagerAdapter;
    private ViewPager mViewPager,viewPagerImages;
    private TextView tv_save;
    private ImageView ivClose,ivDelete;
    private RelativeLayout rlToolbar,rl_info_input;
    private TextView tv_info_header;
    private RecyclerView recyclerViewInfo;
    private SimpleItemAdapter profileInfoAdapter;
    private List<String> infoItemList;
    private TextView tv_title;
    private InfoItem clickedInfoItem;
    private int type;
    private HashMap<String,String> infoMap;
    private Set<String> genreSet;

    private Album album;
    private int position;

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

        infoMap = new HashMap<>();
        genreSet = new HashSet<>();
        tv_title = findViewById(R.id.tv_title);
        tv_save = findViewById(R.id.tv_save);
        ivClose = findViewById(R.id.iv_close);
        ivDelete = findViewById(R.id.iv_delete);
        rlToolbar = findViewById(R.id.toolbar);
        rl_info_input = findViewById(R.id.rl_info_input);
        tv_info_header = findViewById(R.id.tv_info_header);
        List<Fragment> fragmentList = new ArrayList<>();
        profileInfoFragment = ProfileInfoFragment.newInstance("editProfile","");
        profilePortfolioFragment = ProfilePortfolioFragment.newInstance("editProfile","");
        profileVidoFragment = ProfileVidoFragment.newInstance("editProfile","");
        fragmentList.add(profileInfoFragment);
        fragmentList.add(profilePortfolioFragment);
        fragmentList.add(profileVidoFragment);

        String genre = sharedPreferences.getString(Constants.GENRE,"");
        Log.i(TAG,"genre "+genre);
        String[] arr = genre.split(",");
        for(String gen : arr){
            genreSet.add(gen);
        }

        for(InfoItem item : dbHelper.getMyInfoItem(1)){
            infoMap.put(item.getLabel(),item.getValue());
        }

        for(InfoItem item : dbHelper.getMyInfoItem(2)){
            infoMap.put(item.getLabel(),item.getValue());
        }

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
               if(isNetworkAvailable()){
                   updateProfile();
               }else{
                   showMyDialog(getResources().getString(R.string.no_internet));
               }
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

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePortfolioFragment.deletePhotoDialog();
            }
        });

        infoItemList= new ArrayList<>();
        recyclerViewInfo = findViewById(R.id.recycler_view_info);
        recyclerViewInfo.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2=new LinearLayoutManager(this);
        recyclerViewInfo.setLayoutManager(layoutManager2);
        recyclerViewInfo.setItemAnimator(new DefaultItemAnimator());
        profileInfoAdapter=new SimpleItemAdapter(this,infoItemList);
        profileInfoAdapter.setMyItemClickListener(this);
        recyclerViewInfo.setAdapter(profileInfoAdapter);

        rl_info_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoItemList.clear();
                rl_info_input.setVisibility(View.GONE);
                profileInfoAdapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    public void onFragmentInteraction(Object ob, int position, int type) {
        Log.i("Adapter","edit profile info clicked  type "+type);
        this.type = type;
       if(type == 0){
           this.position = position;
           album = (Album)ob;
           selectImage();
        }else if(type == 1){
          rlToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
          ivClose.setVisibility(View.VISIBLE);
          ivDelete.setVisibility(View.VISIBLE);
          tv_save.setVisibility(View.GONE);
          tv_title.setTextColor(getResources().getColor(android.R.color.white));
        //  Utility.setColorFilter(ivClose.getDrawable(),getResources().getColor(android.R.color.white));
       //   Utility.setColorFilter(ivDelete.getDrawable(),getResources().getColor(android.R.color.white));
       }else if(type == 2){
           rlToolbar.setBackgroundColor(getResources().getColor(android.R.color.white));
           ivClose.setVisibility(View.GONE);
           ivDelete.setVisibility(View.GONE);
           tv_save.setVisibility(View.VISIBLE);
           tv_title.setTextColor(getResources().getColor(R.color.primary_text_color));
       }else if(type == 3){
           rl_info_input.setVisibility(View.VISIBLE);
           clickedInfoItem = (InfoItem)ob;
           tv_info_header.setText(clickedInfoItem.getShowLabel());
           getInfoDetails(clickedInfoItem.getShowLabel());
       }else if(type == 4){
           rl_info_input.setVisibility(View.VISIBLE);
           clickedInfoItem = (InfoItem)ob;
           tv_info_header.setText(clickedInfoItem.getShowLabel());
           getInfoDetails(clickedInfoItem.getShowLabel());
       }else if(type == 5){
           Genre item = (Genre)ob;
           genreSet.add(item.getName());
       }else if(type == 6){
           Genre item = (Genre)ob;
           genreSet.remove(item.getName());
       }else if(type == 7){
           selectImage();
       }else if(type == 8){
           selectVideo();
       }
    }

    private void updateProfile(){
        HashMap<String,String> params = new HashMap<>();
        for(String key : infoMap.keySet()){
            params.put(key,infoMap.get(key));
        }
        StringBuilder genreBuilder = new StringBuilder();
        for(String gen : genreSet){
            if(genreBuilder.length() == 0){
                genreBuilder.append(gen);
            }else{
                genreBuilder.append(",").append(gen);
            }

        }
        params.put("genre",genreBuilder.toString());
        params.put("updatedBy",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        showProgress(true);
        String url = getResources().getString(R.string.url)+ Constants.UPDATE_MODEL_PROFILE;
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateProfile");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("updateProfile")){
                if(jsonObject.getBoolean("status")){
                    for(String key : infoMap.keySet()){
                        dbHelper.updateInfoItem(key,infoMap.get(key));
                    }
                    StringBuilder genreBuilder = new StringBuilder();
                    for(String gen : genreSet){
                        if(genreBuilder.length() == 0){
                            genreBuilder.append(gen);
                        }else{
                            genreBuilder.append(",").append(gen);
                        }

                    }
                    editor.putString(Constants.GENRE,genreBuilder.toString());
                    editor.commit();
                    showMyDialog(jsonObject.getString("message"));
                }else{
                    showMyAlertDialog(jsonObject.getString("message"));
                }

            }else if(apiName.equals("uploadPhoto")){
                if(jsonObject.getBoolean("status")){
                    showMyDialog(jsonObject.getString("message"));
                    JSONObject dataObject = jsonObject.getJSONObject("result");
                    PortFolio portFolio = new PortFolio();
                    portFolio.setImageUrl(dataObject.getString("photoUrl"));
                    profilePortfolioFragment.uploadSuccess(portFolio);
                }else{
                    showMyDialog(jsonObject.getString("message"));
                }
            }else if(apiName.equals("uploadBanner")){
                if(jsonObject.getBoolean("status")){
                    //showMyDialog(jsonObject.getString("message"));
                    JSONObject dataObject = jsonObject.getJSONObject("result");
                    profilePortfolioFragment.uploadBannerSuccess(dataObject.getString("photoUrl"));
                }else{
                    showMyDialog(jsonObject.getString("message"));
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    private void getInfoDetails(String label){
        infoItemList.clear();
        String value = null;
        if(label.equals("Height")){
            //cm and ft and inches

            for(int i = 140; i<220; i++){
               // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }
        }else if(label.equals("Weight")){
            //kg and lbs
            for(int i = 40; i<220; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }
        }else if(label.equals("Breast")){
            //cm and inches
            for(int i = 50; i<220; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }
        }else if(label.equals("Waist")){
            //cm and inches
            for(int i = 50; i<220; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }
        }else if(label.equals("Hip")){
            //cm and inches
            for(int i = 50; i<220; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }
        }else if(label.equals("Clothing")){
            //RU +2
            for(int i = 38; i<66; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }

            //UK
            for(int i = 4; i<32; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }

            //RU
            for(int i = 0; i<28; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }

            //UK
            for(int i = 32; i<60; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }
        }else if(label.equals("Footwear")){
            //RU
            for(int i = 30; i<47; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }

            //UK + .05
            for(int i = 2; i<14; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }

            //US +.05
            for(int i = 3; i<15; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }

            //UK +.05
            for(int i = 35; i<50; i++){
                // infoItemList.add(i+" cm | 9'10\"");
                value = centimeterToFeet(i);
                if(!infoItemList.contains(value)){
                    infoItemList.add(centimeterToFeet(i));
                }
            }
        }else if(label.equals("Experience")){
            infoItemList.add("less 1 year");
            infoItemList.add("1-2 years");
            infoItemList.add("3-5 years");
            infoItemList.add("more 5 years");
        }else if(label.equals("Ethnicity")){
            infoItemList.add("European");
            infoItemList.add("African");
            infoItemList.add("Mulatto");
            infoItemList.add("Caucasian");
            infoItemList.add("Asian");
            infoItemList.add("Indian");
            infoItemList.add("Hispanic");
            infoItemList.add("Other");
        }else if(label.equals("Skin Color")){
            infoItemList.add("Pale");
            infoItemList.add("Light");
            infoItemList.add("Dark");
            infoItemList.add("Black");
        }else if(label.equals("Eye Color")){
            infoItemList.add("Black");
            infoItemList.add("Brown");
            infoItemList.add("Light Brown");
            infoItemList.add("Blue");
            infoItemList.add("Light Blue");
            infoItemList.add("Green");
            infoItemList.add("Grey");
            infoItemList.add("Hazel");
        }else if(label.equals("Hair Color")){
            infoItemList.add("Black");
            infoItemList.add("Dark");
            infoItemList.add("Auburn");
            infoItemList.add("Dark Brown");
            infoItemList.add("Light Brown");
            infoItemList.add("Blonde");
            infoItemList.add("Dirty Blonde");
            infoItemList.add("Gray");
            infoItemList.add("Red");
            infoItemList.add("Other");
        }else if(label.equals("Hair Length")){
            infoItemList.add("Very Long");
            infoItemList.add("Long");
            infoItemList.add("Medium");
            infoItemList.add("Short");
            infoItemList.add("Bald");

        }else if(label.equals("Tattoo")){
            infoItemList.add("Yes");
            infoItemList.add("No");
        }else if(label.equals("Piercing")){
            infoItemList.add("Yes");
            infoItemList.add("No");
        }else if(label.equals("Acting Education")){
            infoItemList.add("Yes");
            infoItemList.add("No");
        }else if(label.equals("Working Abroad")){
            infoItemList.add("Yes");
            infoItemList.add("No");
        }

        profileInfoAdapter.notifyDataSetChanged();
    }

    public String centimeterToFeet(int centemeter) {
        int feetPart = 0;
        int inchesPart = 0;
        feetPart = (int) Math.floor((centemeter / 2.54) / 12);
        System.out.println((centemeter / 2.54) - (feetPart * 12));
        inchesPart = (int) Math.ceil((centemeter / 2.54) - (feetPart * 12));
        return String.format("%d' %d''", feetPart, inchesPart);
    }

    @Override
    public void onItemClicked(int position, int type) {
          Log.i(TAG,"info item "+infoItemList.get(position)+" type "+type+" this.type "+this.type);
        if(type == 2){
            clickedInfoItem.setValue(infoItemList.get(position));
            profileInfoFragment.setInfoItemValue(clickedInfoItem);
            infoMap.put(clickedInfoItem.getLabel(),clickedInfoItem.getValue());
           /* if(this.type ==3){
                profileInfoFragment.setInfoItemValue(clickedInfoItem);
                infoMap.put(clickedInfoItem.getLabel(),clickedInfoItem.getValue());
            }else if(this.type  == 4){
                profileInfoFragment.setInfoItemValue(clickedInfoItem);
                infoMap.put(clickedInfoItem.getLabel(),clickedInfoItem.getValue());
            }*/

            infoItemList.clear();
            rl_info_input.setVisibility(View.GONE);
            profileInfoAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void imageAdded(){
        Log.i(TAG,"imagepath "+imagePath);
        Log.i(TAG,"fileName "+fileName);
        Map<String,String> params = new HashMap<>();
        params.put("modelId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("photoUrl",convertToBase64(new File(imagePath)));
        params.put("photoName",fileName);
        String url = getResources().getString(R.string.url)+Constants.UPLOAD_PHOTO;
        String api = null;
        if(type == 0){
            url = getResources().getString(R.string.url)+Constants.UPLOAD_PHOTO;
            api = "uploadPhoto";
            params.put("albumId",""+album.getId());
            params.put("albumTitle",album.getHeader());
        }else if(type == 7){
            profilePortfolioFragment.showBannerSuccess(imagePath);
            url = getResources().getString(R.string.url)+Constants.UPLOAD_BANNER;
            api = "uploadBanner";
            params.put("albumTitle","banner");
        }

        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),api);
    }

    @Override
    protected void videoAdded(){
       Log.i(TAG,"video path "+videoPath);

        Map<String,String> params = new HashMap<>();
        params.put("modelId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        params.put("videoUrl",convertToBase64(new File(imagePath)));
        params.put("videoName",fileName);
        String url = getResources().getString(R.string.url)+Constants.UPLOAD_VIDEO;
       // showProgress(true);
       // jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"uploadVideo");

    }

    @Override
    public void onDialogPositiveClicked(){

        if(getIntent().getStringExtra("flag") != null && getIntent().getStringExtra("flag").equals("home")){
            Intent intent = new Intent(EditProfileActivity.this, JobListActivity.class);
            intent.putExtra("flag","model");
            startActivity(intent);
        }else{
            finish();
        }

    }

    @Override
    public void onBackPressed(){
        if(rl_info_input.getVisibility() == View.VISIBLE){
            infoItemList.clear();
            rl_info_input.setVisibility(View.GONE);
            profileInfoAdapter.notifyDataSetChanged();
        }else{
            super.onBackPressed();
        }
    }
}
