package com.talentnew.activities.agency;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.talentnew.R;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.activities.talent.ProfileActivity;
import com.talentnew.adapters.ModelListAdapter;
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

public class ModelListActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private ModelListAdapter myItemAdapter;
    private List<MyModel> myItemList;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_list);
        initFooter(this, 0);
        setToolbarDetails(this);
        init();
    }

    private void init(){
        myItemList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                    getItemList();
                }
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ModelListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getItemList();
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
        String id =  sharedPreferences.getString(Constants.USER_ID, "");
        //String location =  sharedPreferences.getString(Constants.USER_LOCATION, "");
        String location = "Delhi";
        String url = getResources().getString(R.string.url)+Constants.GET_ALL_MODEL+"?id="+id+"&"+"location="+location;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.GET,url,null,"getModel");
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
                        if(dataObject.getInt("isActive")==0)
                        item.setActive(false);
                        else item.setActive(true);
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
                        showError(true,"No Models available.");
                    }
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        Intent intent = new Intent(ModelListActivity.this, ProfileActivity.class);
        intent.putExtra("flag","ModelList");
        intent.putExtra("model",myItemList.get(position));
        startActivity(intent);
    }
}
