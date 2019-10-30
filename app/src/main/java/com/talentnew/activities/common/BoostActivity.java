package com.talentnew.activities.common;

import android.os.Bundle;

import com.android.volley.Request;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;

import com.talentnew.R;
import com.talentnew.adapters.BoostAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.interfaces.MyItemLevelClickListener;
import com.talentnew.models.Boost;
import com.talentnew.models.BoostInfo;
import com.talentnew.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoostActivity extends NetworkBaseActivity implements MyItemClickListener, MyItemLevelClickListener {

    private RecyclerView recyclerView;
    private BoostAdapter myItemAdapter;
    private List<Object> myItemList;
    private int selectedSchemePosition;

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
        myItemAdapter.setMyItemLevelClickListener(this);
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

    private void appliedBoost(Object object){
        Boost boost = (Boost) object;
        BoostInfo boostInfo = (BoostInfo) boost.getItemList().get(selectedSchemePosition);
        Map<String,String> params = new HashMap<>();
        params.put("id", "");
        params.put("agentId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("boostId",String.valueOf(boost.getId()));
        params.put("jobPost",);
        params.put("emailShoutOut",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("fbShoutOut",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("boostJob",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("customAdd",sharedPreferences.getString(Constants.LOCATION,""));

        params.put("title",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("dedicatedManager",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("contactModel",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("proTag",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("verifiedTag",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("validity",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("scheme",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("userName",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("amount",sharedPreferences.getString(Constants.LOCATION,""));
        String url = null;
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
            url = getResources().getString(R.string.url)+Constants.APPLY_AGENCY_BOOST;
        }else{
            url = getResources().getString(R.string.url)+Constants.APPLY_MODEL_BOOST;
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
                    if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
                        manageAgencyBoost(jsonArray);
                    }else{
                        manageModelBoost(jsonArray);
                    }
                  /*  JSONObject dataObject = null,infoObject = null;
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
                    }*/
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    private void manageModelBoost(JSONArray jsonArray){

    }

    private void manageAgencyBoost(JSONArray jsonArray){
        Boost boost = null;
        JSONObject jsonObject = null;
        JSONArray mSchemeArray = null;
        BoostInfo boostInfo =null;
        List<Object> objectInfoList =null;
        try {
        for(int i =0;i<jsonArray.length();i++){
                objectInfoList = new ArrayList<>();
                jsonObject = jsonArray.getJSONObject(i);
                boost = new Boost();
                boost.setId(jsonObject.getInt("id"));
                boost.setHeader(jsonObject.getString("title"));
                if(!jsonObject.getString("jobPost").equals("null")){
                    if(jsonObject.getInt("jobPost")==1){
                        objectInfoList.add(jsonObject.getString("jobPost"));
                    }
                }
                if(!jsonObject.getString("fbShoutOut").equals("null") && jsonObject.getInt("fbShoutOut") == 1) {
                    objectInfoList.add("Enhanced your visibility on our facebook page");
                }
                if(!jsonObject.getString("emailShoutOut").equals("null") && jsonObject.getInt("emailShoutOut") == 1) {
                objectInfoList.add("Send Emails to talents");
                }
                if(!jsonObject.getString("boostJob").equals("null") && jsonObject.getInt("boostJob") == 1) {
                    objectInfoList.add("Boost Jobs");
                }
                if(!jsonObject.getString("customAdd").equals("null") && jsonObject.getInt("customAdd") == 1) {
                    objectInfoList.add("Advertise with custom Add");
                }
                if(!jsonObject.getString("dedicatedManager").equals("null") && jsonObject.getString("dedicatedManager").equals("Y")) {
                    objectInfoList.add("Dedicated Manager");
                }
                if(!jsonObject.getString("contactModel").equals("null") && jsonObject.getInt("contactModel") == 1) {
                    objectInfoList.add("View Model Contacts");
                }
                if(!jsonObject.getString("proTag").equals("null") && jsonObject.getString("proTag").equals("Y")) {
                    objectInfoList.add("Pro Tag");
                }
                if(!jsonObject.getString("verifiedTag").equals("null") && jsonObject.getString("verifiedTag").equals("Y")) {
                objectInfoList.add("Verified Tag");
                }

                mSchemeArray = jsonObject.getJSONArray("mySchemeList");
                if(mSchemeArray.length()>1) {
                    for (int j = 0; j < mSchemeArray.length(); j++) {
                        boostInfo = new BoostInfo();
                        String title = mSchemeArray.getJSONObject(j).getString("scheme").concat(" Package Rs " + mSchemeArray.getJSONObject(j).getString("amount") + " /-");
                        boostInfo.setTitle(title);
                        boostInfo.setScheme( mSchemeArray.getJSONObject(j).getString("scheme"));
                        boostInfo.setAmount((float) (mSchemeArray.getJSONObject(j).getDouble("amount")));
                        boostInfo.setPosition(i);
                        objectInfoList.add(boostInfo);
                    }
                }else {
                    boost.setPay("INR "+ String.format("%.02f",(float)mSchemeArray.getJSONObject(0).getDouble("amount"))+"/"+
                            mSchemeArray.getJSONObject(0).getString("scheme"));
                }
                boost.setItemList(objectInfoList);
                myItemList.add(boost);
        }
        myItemAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
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
        appliedBoost(myItemList.get(position));
    }

    @Override
    public void onItemClicked(int parentPosition, int position, int type) {
        selectedSchemePosition = position;
        Boost boost =(Boost) myItemList.get(parentPosition);
        BoostInfo boostInfo = (BoostInfo) boost.getItemList().get(position);
        boost.setPay("INR "+ String.format("%.02f",(float)boostInfo.getAmount())+"/"+ boostInfo.getScheme());
        myItemAdapter.notifyItemChanged(parentPosition);
    }

}
