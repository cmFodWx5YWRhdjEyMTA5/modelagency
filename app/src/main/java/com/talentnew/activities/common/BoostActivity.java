package com.talentnew.activities.common;

import android.os.Bundle;

import com.android.volley.Request;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
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
    private int selectedSchemePosition,preSelectBoost,preSelectedInfo;

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

    private void appliedAgencyBoost(Object object){
        Boost boost = (Boost) object;
        BoostInfo boostInfo = (BoostInfo) boost.getItemList().get(selectedSchemePosition);
        Map<String,String> params = new HashMap<>();
        params.put("id", "");
        params.put("agentId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("boostId",String.valueOf(boost.getId()));
        params.put("jobPost", String.valueOf(boost.getJobPost()));
        params.put("emailShoutOut", String.valueOf(boost.getEmailShoutOut()));
        params.put("fbShoutOut", String.valueOf(boost.getFbShoutOut()));
        params.put("boostJob", String.valueOf(boost.getBoostJob()));
        params.put("customAdd", String.valueOf(boost.getCustomAdd()));

        params.put("title", boost.getTitle());
        params.put("dedicatedManager", boost.getDedicatedManager());
        params.put("contactModel", boost.getContactModel());
        params.put("proTag", boost.getProTag());
        params.put("verifiedTag", boost.getVerifiedTag());
        params.put("validity", boost.getValidity() );
        params.put("scheme", boostInfo.getScheme());
        params.put("userName", sharedPreferences.getString(Constants.USERNAME, "") );
        params.put("amount", String.valueOf(boostInfo.getAmount()));
        String url = getResources().getString(R.string.url)+Constants.APPLY_AGENCY_BOOST;

        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"applyAgencyBoost");
    }

    private void applyModelBoost(Boost boost){

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
                }
            }else if (apiName.equals("applyAgencyBoost")) {

            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    private void manageModelBoost(JSONArray jsonArray){
        try {
            JSONObject dataObject = null,schemeObject = null;
            Boost item = null;
            List<Object> boostInfoList = null;
            JSONArray schemeArray = null;
            int len = jsonArray.length();
            for(int i=0; i<len; i++) {
                dataObject = jsonArray.getJSONObject(i);
                item = new Boost();
                item.setHeader(dataObject.getString("title"));
                schemeArray = dataObject.getJSONArray("mySchemeList");
                boostInfoList = new ArrayList<>();
                if(!dataObject.getString("onlineCourse").equals("N")){
                    boostInfoList.add(dataObject.getString("onlineCourse"));
                }
                if(!dataObject.getString("photoshoot").equals("0")){
                    boostInfoList.add(dataObject.getString("photoshoot")+" Photoshoots");
                }

                if(!dataObject.getString("featureTag").equals("N")){
                    boostInfoList.add("Feature tag");
                }

                if(!dataObject.getString("email").equals("N")){
                    boostInfoList.add("Send Email to agents");
                }

                if(!dataObject.getString("email").equals("N")){
                    boostInfoList.add("Send Email to agents");
                }

                Log.i(TAG,"boost position "+i);

                if(schemeArray.length() == 1){
                    schemeObject = schemeArray.getJSONObject(0);
                    item.setPay("INR "+ String.format("%.02f",(float)schemeObject.getDouble("amount"))+"/"+
                            schemeObject.getString("scheme"));
                }else{
                    BoostInfo boostInfo = null;
                    for (int j = 0; j < schemeArray.length(); j++) {
                        boostInfo = new BoostInfo();
                        schemeObject = schemeArray.getJSONObject(j);
                        String title = schemeObject.getString("scheme").concat(" Package Rs " + schemeObject.getString("amount") + " /-");
                        boostInfo.setTitle(title);
                        boostInfo.setScheme( schemeObject.getString("scheme"));
                        boostInfo.setAmount((float) (schemeObject.getDouble("amount")));
                        boostInfo.setPosition(i);
                        Log.i(TAG,"parent position "+i+" "+boostInfo.getPosition());
                        boostInfoList.add(boostInfo);
                    }
                }

                item.setItemList(boostInfoList);
                myItemList.add(item);
            }

            myItemAdapter.notifyDataSetChanged();
        }catch (JSONException error){
            error.printStackTrace();
        }
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
        if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
                appliedAgencyBoost(myItemList.get(position));
        }else{
            applyModelBoost((Boost)myItemList.get(position));
        }

    }

    @Override
    public void onItemClicked(int parentPosition, int position, int type) {

        Log.i(TAG,"boost position "+parentPosition+" info position "+position);
        Log.i(TAG,"pre boost position "+preSelectBoost+" pre info position "+preSelectedInfo);

        selectedSchemePosition = position;
        Boost boost =(Boost) myItemList.get(parentPosition);
        BoostInfo boostInfo = (BoostInfo) boost.getItemList().get(position);
        boost.setPay("INR "+ String.format("%.02f",(float)boostInfo.getAmount())+"/"+ boostInfo.getScheme());


        Boost preBoost =(Boost) myItemList.get(preSelectBoost);
        BoostInfo preBoostInfo = (BoostInfo) preBoost.getItemList().get(preSelectedInfo);
        preBoostInfo.setSelected(false);
        myItemAdapter.notifyItemChanged(preSelectBoost);

        boostInfo.setSelected(true);
        preSelectedInfo = position;
        preSelectBoost = parentPosition;
        myItemAdapter.notifyItemChanged(parentPosition);
    }

}
