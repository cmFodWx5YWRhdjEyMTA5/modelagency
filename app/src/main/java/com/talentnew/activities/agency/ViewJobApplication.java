package com.talentnew.activities.agency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.talentnew.R;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.activities.talent.ProfileActivity;
import com.talentnew.adapters.ViewJobListAdapter;
import com.talentnew.adapters.ViewJobsModelListAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.models.MyJob;
import com.talentnew.models.MyModel;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewJobApplication extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private ViewJobsModelListAdapter myItemAdapter;
    private List<MyModel> myItemList;
    private MyJob myJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_job_application);
        initFooter(this, 4);
        setToolbarDetails(this);
        initViews();
    }

    private void initViews(){
        myJob = (MyJob) getIntent().getSerializableExtra("job");
        myItemList = new ArrayList<>();
         getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ViewJobsModelListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        MyModel item = null;
        /*for(int i=0; i<20; i++){
            item = new MyModel();
            item.setCompany("Tech Model");
            item.setName("Jess");
            item.setDesignation("Model");
            item.setAddress("Delhi");
            item.setJoining_Date("28 Sep 2017");
            item.setEnding_Date("31 Dec 2018");
            item.setLocalImage(R.drawable.model);
            myItemList.add(item);
        }*/

        Map<String,String> params = new HashMap<>();
        String url = getResources().getString(R.string.url)+Constants.GET_APPLIED_MODEL+"?jobId="+myJob.getId();
        showProgress(true);
        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(params),"getModel");

    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getModel")){
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject dataObject = null;
                    MyModel item = null;
                    int len = jsonArray.length();
                    myItemList.clear();
                    for(int i=0; i<len; i++){
                        dataObject = jsonArray.getJSONObject(i);
                        item = new MyModel();
                        item.setId(dataObject.getString("id"));
                        item.setActive(dataObject.getBoolean("isActive"));
                        item.setName(dataObject.getString("userName"));
                        item.setMobile(dataObject.getString("mobile"));
                        item.setEmail(dataObject.getString("email"));
                        item.setProfilePic(dataObject.getString("profilePic"));
                       // item.setToken(dataObject.getString("token"));
                        item.setFcmToken(dataObject.getString("fcmToken"));
                        item.setBannerPic(dataObject.getString("bannerPic"));
                        item.setHeight(dataObject.getString("height"));
                        item.setWeight(dataObject.getString("waist"));
                        item.setBreast(dataObject.getString("breast"));
                        item.setWeight(dataObject.getString("weight"));
                        item.setHip(dataObject.getString("hip"));
                        item.setExperience(dataObject.getString("experience"));
                        item.setEthnicity(dataObject.getString("ethnicity"));
                        item.setSkinColor(dataObject.getString("skinColor"));
                        item.setHairColor(dataObject.getString("hairColor"));
                        item.setEyeColor(dataObject.getString("eyeColor"));
                        item.setHairLength(dataObject.getString("hairLength"));
                        item.setActingEducation(dataObject.getString("actingEducation"));
                        item.setGenre(dataObject.getString("genre"));
                        myItemList.add(item);
                    }

                    if(len > 0){
                        myItemAdapter.notifyDataSetChanged();
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        showError(true,"No View Available.");
                    }
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        if(type==1){
            Log.d("clicked ", myItemList.get(position).getName()+"");
            Intent intent = new Intent(ViewJobApplication.this, ProfileActivity.class);
            intent.putExtra("flag","viewModel");
            intent.putExtra("id",myItemList.get(position).getId());
            startActivity(intent);
        }
    }
}
