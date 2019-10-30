package com.talentnew.activities.common;

import android.os.Bundle;

import com.android.volley.Request;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.talentnew.R;
import com.talentnew.adapters.BoostAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.models.Boost;
import com.talentnew.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoostActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private BoostAdapter myItemAdapter;
    private List<Object> myItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost);
        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        setToolbarDetails(this);
        init();
        //if(getIntent().getExtras().equals("model"))
        initFooter(this,3);
        //else initFooter(this, 4);
    }

    private void init(){
        myItemList = new ArrayList<>();
        getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new BoostAdapter(this,myItemList,"homeList");
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        Boost item = null;
        List<Object> infoList = null;
       /* for(int i =0; i<3; i++){
            item = new Boost();
            infoList = new ArrayList<>();
            item.setHeader("Basic");
            item.setPay("INR 480.00/month");
            infoList.add("up to 3 albums");
            infoList.add("up to 50 photos");
            infoList.add("Basic Listed");
            infoList.add("apply to 15 job per month");
            item.setItemList(infoList);
            myItemList.add(item);
        }*/

        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("limit", ""+limit);
        params.put("offset", ""+offset);
        params.put("location",sharedPreferences.getString(Constants.LOCATION,""));
        String url = null;
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
            url = getResources().getString(R.string.url)+Constants.GET_AGENCY_BOOST;
        }else{
            url = getResources().getString(R.string.url)+Constants.GET_MODEL_BOOST;
        }
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getBoost");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getBoost")){
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject dataObject = null,infoObject = null;
                    Boost item = null;
                    List<Object> boostInfoList = null;
                    int tempBoostId = 0;
                    int len = jsonArray.length();
                    for(int i=0; i<len; i++){
                        dataObject = jsonArray.getJSONObject(i);
                        if(tempBoostId != dataObject.getInt("id")){
                            tempBoostId = dataObject.getInt("id");
                            item = new Boost();
                            boostInfoList = new ArrayList<>();
                            item.setHeader(dataObject.getString("type"));
                            item.setPay("INR "+ String.format("%.02f",(float)dataObject.getDouble("amount"))+"/month");
                            infoObject = dataObject.getJSONObject("boostinfo");
                            boostInfoList.add(infoObject.getString("description"));
                            item.setItemList(boostInfoList);
                            myItemList.add(item);
                        }else{
                            item = getBoostItem(dataObject.getInt("id"));
                            infoObject = dataObject.getJSONObject("boostinfo");
                            item.getItemList().add(infoObject.getString("description"));
                        }
                    }

                    if(len > 0){
                        myItemAdapter.notifyDataSetChanged();
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        showError(true,"Currently no boost available. Please try again later.");
                    }
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    private Boost getBoostItem(int id){
        Boost boost = null;
        for(Object ob : myItemList){
            boost = (Boost)ob;
            if(boost.getId() == id){
                break;
            }
        }

        return boost;
    }

    @Override
    public void onItemClicked(int position, int type) {

    }
}
