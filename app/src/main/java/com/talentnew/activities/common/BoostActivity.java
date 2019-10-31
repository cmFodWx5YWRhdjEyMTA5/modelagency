package com.talentnew.activities.common;

import android.os.Bundle;

import com.android.volley.Request;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.TextView;

import com.talentnew.R;
import com.talentnew.adapters.BoostAdapter;
import com.talentnew.adapters.SimpleItemAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.interfaces.MyItemLevelClickListener;
import com.talentnew.models.Boost;
import com.talentnew.models.BoostInfo;
import com.talentnew.models.MyLocation;
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
    private int selectedSchemePosition,preSelectBoost,preSelectedInfo,parentPosition,position;
    private Boost selectedBoost;
    private String location;

    private RecyclerView recyclerViewDialog;
    private SimpleItemAdapter dialogListAdapter;
    private List<String> diallogItemList;
    private AlertDialog alertDialog;

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
        diallogItemList = new ArrayList<>();
        getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new BoostAdapter(this,myItemList,"homeList");
        myItemAdapter.setMyItemClickListener(this);
        myItemAdapter.setMyItemLevelClickListener(this);
        recyclerView.setAdapter(myItemAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isScroll) {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    Log.i(TAG,"visible "+visibleItemCount+" total "+totalItemCount);
                    pastVisibleItems = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                    Log.i(TAG,"past visible "+(pastVisibleItems));

                    if (!loading) {
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            loading = true;
                            offset = limit + offset;
                            getItemList();
                        }
                    }

                }
            }
        });

    }

    private void getItemList(){
        loading = true;
        Boost item = null;
        List<Object> infoList = null;
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
        selectedBoost = (Boost) object;
        BoostInfo boostInfo = null;
        Log.d("selectedSchemePosition ", selectedSchemePosition+"");
        if(selectedBoost.getItemList().get(selectedSchemePosition) instanceof BoostInfo)
            boostInfo = (BoostInfo) selectedBoost.getItemList().get(selectedSchemePosition);
        Map<String,String> params = new HashMap<>();
        params.put("id", "");
        params.put("agentId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("boostId",String.valueOf(selectedBoost.getId()));
        params.put("jobPost", String.valueOf(selectedBoost.getJobPost()));
        params.put("emailShoutOut", String.valueOf(selectedBoost.getEmailShoutOut()));
        params.put("fbShoutOut", String.valueOf(selectedBoost.getFbShoutOut()));
        params.put("boostJob", String.valueOf(selectedBoost.getBoostJob()));
        params.put("customAdd", String.valueOf(selectedBoost.getCustomAdd()));

        params.put("title", selectedBoost.getHeader());
        params.put("dedicatedManager", selectedBoost.getDedicatedManager());
        params.put("contactModel", selectedBoost.getContactModel());
        params.put("proTag", selectedBoost.getProTag());
        params.put("verifiedTag", selectedBoost.getVerifiedTag());
        if(boostInfo!=null) {
            params.put("scheme", boostInfo.getScheme());
            params.put("amount", String.valueOf(boostInfo.getAmount()));
            params.put("validity", boostInfo.getValidity() );
        }
        else {
            params.put("scheme", selectedBoost.getScheme());
            params.put("amount", String.valueOf(selectedBoost.getAmount()));
            params.put("validity", selectedBoost.getValidity());
        }
        params.put("userName", sharedPreferences.getString(Constants.USERNAME, "") );

        String url = getResources().getString(R.string.url)+Constants.APPLY_AGENCY_BOOST;

        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"applyAgencyBoost");
    }

    private void applyModelBoost(Boost boost){
        selectedBoost = boost;
        if(boost.getPhotoshoot() > 0){
            if(location == null){
                showMyAlertDialog("Please select photo shoot location.");
                return;
            }
        }


        Map<String,String> params = new HashMap<>();
        params.put("id", "");
        params.put("modelId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("boostId",String.valueOf(boost.getId()));
        params.put("featureTag", String.valueOf(boost.getFeatureTag()));
        params.put("photoshoot", String.valueOf(boost.getPhotoshoot()));
        params.put("applyJob", String.valueOf(boost.getApplyJob()));
        params.put("title", boost.getHeader());
        params.put("onlineCourse", boost.getOnlineCourse());
        if(boost.getScheme() == null){
            BoostInfo boostInfo = (BoostInfo) boost.getItemList().get(selectedSchemePosition);
            params.put("validity", boostInfo.getValidity());
            params.put("scheme", boostInfo.getScheme());
            params.put("amount", String.valueOf(boostInfo.getAmount()));
            selectedBoost.setValidity(boostInfo.getValidity());
            selectedBoost.setAmount(boostInfo.getAmount());
            selectedBoost.setScheme(boostInfo.getScheme());
        }else{
            params.put("validity", boost.getValidity());
            params.put("scheme", boost.getScheme());
            params.put("amount", String.valueOf(boost.getAmount()));
        }
        selectedBoost.setLocation(location);
        params.put("location", location);
        params.put("userName", sharedPreferences.getString(Constants.USERNAME, "") );

        String url = getResources().getString(R.string.url)+Constants.APPLY_MODEL_BOOST;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"applyModelBoost");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getBoost")){
                loading = false;
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
                        manageAgencyBoost(jsonArray);
                    }else{
                        manageModelBoost(jsonArray);
                    }
                }
            }else if (apiName.equals("applyAgencyBoost")) {
                if(jsonObject.getBoolean("status")) {
                    editor.putInt(Constants.SUBSC_BOOST_ID, selectedBoost.getBoostId());
                    editor.putInt(Constants.SUBSC_JOB_POST, selectedBoost.getJobPost());
                    editor.putInt(Constants.SUBSC_EMIL_SHOUTOUT, selectedBoost.getEmailShoutOut());
                    editor.putInt(Constants.SUBSC_FB_SHOUTOUT, selectedBoost.getFbShoutOut());
                    editor.putInt(Constants.SUBSC_BOOSTJOB, selectedBoost.getBoostJob());
                    editor.putInt(Constants.SUBSC_CUSTOMADD, selectedBoost.getCustomAdd());
                    editor.putString(Constants.SUBSC_TITLE, selectedBoost.getHeader());
                    editor.putString(Constants.SUBSC_DEDICATED_MANAGER, selectedBoost.getDedicatedManager());
                    editor.putString(Constants.SUBSC_CONTACT_MODEL, selectedBoost.getContactModel());
                    editor.putString(Constants.SUBSC_PRO_TAG, selectedBoost.getProTag());
                    editor.putString(Constants.SUBSC_VERIFIED_TAG, selectedBoost.getVerifiedTag());
                    editor.putString(Constants.SUBSC_VALIDITY, selectedBoost.getValidity());
                    editor.putString(Constants.SUBSC_SCHEME, selectedBoost.getScheme());
                    editor.putString(Constants.SUBSC_AMOUNT, String.valueOf(selectedBoost.getAmount()));
                    editor.commit();
                }
            }else if (apiName.equals("applyModelBoost")) {
                if(jsonObject.getBoolean("status")){
                    JSONObject dataObject = jsonObject.getJSONObject("result");
                    editor.putString(Constants.BOOST_TITLE,selectedBoost.getHeader());
                    editor.putInt(Constants.BOOST_PHOTO_SHOOT,selectedBoost.getPhotoshoot());
                    editor.putString(Constants.BOOST_PHOTO_LOCATION,selectedBoost.getLocation());
                    editor.putString(Constants.BOOST_ONLINE_COURSE,selectedBoost.getOnlineCourse());
                    editor.putString(Constants.BOOST_FEATURE_TAG,selectedBoost.getFeatureTag());
                    editor.putString(Constants.BOOST_EMAIL,selectedBoost.getEmail());
                    editor.putString(Constants.BOOST_SCHEME,selectedBoost.getScheme());
                    editor.putString(Constants.BOOST_VALIDITY,selectedBoost.getValidity());
                    editor.putFloat(Constants.BOOST_AMOUNT,selectedBoost.getAmount());
                    editor.putInt(Constants.BOOST_APPLY_JOB,selectedBoost.getApplyJob());
                    editor.putString(Constants.BOOST_START_DATE,dataObject.getString("startDate"));
                    editor.putString(Constants.BOOST_END_DATE,dataObject.getString("endDate"));
                    editor.putString(Constants.BOOST_RENEW_DATE,dataObject.getString("renewDate"));
                    editor.putString(Constants.BOOST_ONLINE_COURSE_END_DATE,dataObject.getString("onlineCourseEndDate"));
                    editor.commit();
                    showMyAlertDialog(jsonObject.getString("message"));
                }else{
                    showMyAlertDialog(jsonObject.getString("message"));
                }
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
            List<Object> locationList = null;
            JSONArray schemeArray = null,locationArray = null;
            int len = jsonArray.length();
            for(int i=0; i<len; i++) {
                dataObject = jsonArray.getJSONObject(i);
                item = new Boost();
                item.setHeader(dataObject.getString("title"));
                schemeArray = dataObject.getJSONArray("mySchemeList");
                if(dataObject.has("myPhotoShootList")){
                    locationArray =  dataObject.getJSONArray("myPhotoShootList");
                    locationList = new ArrayList<>();
                    MyLocation myLocation = null;
                    for(int k = 0;k<locationArray.length();k++){
                        myLocation = new MyLocation();
                        myLocation.setLocation(locationArray.getJSONObject(k).getString("location"));
                        myLocation.setPosition(i);
                        locationList.add(myLocation);
                    }
                    item.setLocationList(locationList);
                }
                boostInfoList = new ArrayList<>();
                if(!dataObject.getString("onlineCourse").equals("null") &&
                        !dataObject.getString("onlineCourse").equals("N")){
                    boostInfoList.add(dataObject.getString("onlineCourse"));
                    item.setOnlineCourse(dataObject.getString("onlineCourse"));
                }else{
                    item.setOnlineCourse("N");
                }
                if(!dataObject.getString("photoshoot").equals("null") &&
                        !dataObject.getString("photoshoot").equals("0")){
                    boostInfoList.add(dataObject.getString("photoshoot")+" Photoshoots");
                    item.setPhotoshoot(dataObject.getInt("photoshoot"));
                    MyLocation myLocation = new MyLocation();
                    myLocation.setLocation("Select Location");
                    myLocation.setPosition(i);
                    boostInfoList.add(myLocation);
                }else{
                    item.setPhotoshoot(0);
                }

                if(!dataObject.getString("featureTag").equals("null") &&
                        !dataObject.getString("featureTag").equals("N")){
                    boostInfoList.add("Feature tag");
                    item.setFeatureTag(dataObject.getString("featureTag"));
                }else{
                    item.setFeatureTag("N");
                }

                if(!dataObject.getString("email").equals("N") &&
                        !dataObject.getString("email").equals("N")){
                    boostInfoList.add("Send Email to agents");
                    item.setEmail(dataObject.getString("email"));
                }else{
                    item.setEmail("N");
                }

                if(schemeArray.length() == 1){
                    schemeObject = schemeArray.getJSONObject(0);
                    item.setPay("INR "+ String.format("%.02f",(float)schemeObject.getDouble("amount"))+"/"+
                            schemeObject.getString("scheme"));
                    item.setAmount((float)schemeObject.getDouble("amount"));
                    item.setScheme( schemeObject.getString("scheme"));
                    item.setValidity( schemeObject.getString("validity"));
                }else{
                    BoostInfo boostInfo = null;
                    String title = null;
                    for (int j = 0; j < schemeArray.length(); j++) {
                        boostInfo = new BoostInfo();
                        schemeObject = schemeArray.getJSONObject(j);
                        if(schemeObject.getInt("applyJob") > 0){
                            title = schemeObject.getInt("applyJob")+" Jobs at".concat(" Rs " + schemeObject.getString("amount") + " /-");
                        }else{
                            title = schemeObject.getString("scheme").concat(" Package Rs " + schemeObject.getString("amount") + " /-");
                        }
                        boostInfo.setTitle(title);
                        boostInfo.setScheme( schemeObject.getString("scheme"));
                        boostInfo.setValidity( schemeObject.getString("validity"));
                        boostInfo.setAmount((float) (schemeObject.getDouble("amount")));
                        boostInfo.setPosition(i);
                        boostInfoList.add(boostInfo);
                        if(j==0){
                            item.setPay("INR "+ String.format("%.02f",(float)schemeObject.getDouble("amount"))+"/"+
                                    schemeObject.getString("scheme"));
                        }
                    }
                }

                item.setItemList(boostInfoList);
                myItemList.add(item);
            }

            if(len > 0){
                if(len < limit){
                    isScroll = false;
                }
                if(offset == 0){
                    myItemAdapter.notifyDataSetChanged();
                }else{
                    recyclerView.post(new Runnable() {
                        public void run() {
                            myItemAdapter.notifyItemRangeInserted(offset,limit);
                            loading = false;
                        }
                    });
                    Log.d(TAG, "NEXT ITEMS LOADED");
                }
            }

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
                if(!jsonObject.getString("jobPost").equals("null")  && jsonObject.getInt("jobPost") == 1){
                    boost.setJobPost(jsonObject.getInt("jobPost"));
                    objectInfoList.add("Post unlimited Jobs");
                }else boost.setJobPost(0);

                if(!jsonObject.getString("fbShoutOut").equals("null") && jsonObject.getInt("fbShoutOut") == 1) {
                    boost.setFbShoutOut(jsonObject.getInt("fbShoutOut"));
                    objectInfoList.add("Enhanced your visibility on our facebook page");
                }else boost.setFbShoutOut(0);

                if(!jsonObject.getString("emailShoutOut").equals("null") && jsonObject.getInt("emailShoutOut") == 1) {
                    boost.setEmailShoutOut(jsonObject.getInt("emailShoutOut"));
                    objectInfoList.add("Send Emails to talents");
                }else  boost.setEmailShoutOut(0);

                if(!jsonObject.getString("boostJob").equals("null") && jsonObject.getInt("boostJob") == 1) {
                    boost.setBoostJob(jsonObject.getInt("boostJob"));
                    objectInfoList.add("Boost Jobs");
                }else  boost.setBoostJob(0);

                if(!jsonObject.getString("customAdd").equals("null") && jsonObject.getInt("customAdd") == 1) {
                    boost.setCustomAdd(jsonObject.getInt("customAdd"));
                    objectInfoList.add("Advertise with custom Add");
                }else  boost.setCustomAdd(0);

                if(!jsonObject.getString("dedicatedManager").equals("null") && jsonObject.getString("dedicatedManager").equals("Y")) {
                    boost.setDedicatedManager(jsonObject.getString("dedicatedManager"));
                    objectInfoList.add("Dedicated Manager");
                }else boost.setDedicatedManager("N");

                if(!jsonObject.getString("contactModel").equals("null") && jsonObject.getInt("contactModel") == 1) {
                    boost.setContactModel(jsonObject.getString("contactModel"));
                    objectInfoList.add("View Model Contacts");
                }else boost.setContactModel("N");

                if(!jsonObject.getString("proTag").equals("null") && jsonObject.getString("proTag").equals("Y")) {
                    boost.setProTag(jsonObject.getString("proTag"));
                    objectInfoList.add("Pro Tag");
                }else boost.setProTag("N");

                if(!jsonObject.getString("verifiedTag").equals("null") && jsonObject.getString("verifiedTag").equals("Y")) {
                    boost.setVerifiedTag(jsonObject.getString("verifiedTag"));
                    objectInfoList.add("Verified Tag");
                }else boost.setVerifiedTag("N");

                mSchemeArray = jsonObject.getJSONArray("mySchemeList");
                if(mSchemeArray.length()>1) {
                    for (int j = 0; j < mSchemeArray.length(); j++) {
                        boostInfo = new BoostInfo();
                        String title = mSchemeArray.getJSONObject(j).getString("scheme").concat(" Package Rs " + mSchemeArray.getJSONObject(j).getString("amount") + " /-");
                        boostInfo.setTitle(title);
                        boostInfo.setScheme( mSchemeArray.getJSONObject(j).getString("scheme"));
                        boostInfo.setAmount((float) (mSchemeArray.getJSONObject(j).getDouble("amount")));
                        boostInfo.setValidity(mSchemeArray.getJSONObject(0).getString("validity"));
                        boostInfo.setPosition(i);
                        objectInfoList.add(boostInfo);
                    }
                }else {
                    boost.setScheme(mSchemeArray.getJSONObject(0).getString("scheme"));
                    boost.setAmount((float) mSchemeArray.getJSONObject(0).getDouble("amount"));
                    boost.setValidity(mSchemeArray.getJSONObject(0).getString("validity"));
                    boost.setPay("INR "+ String.format("%.02f",(float)mSchemeArray.getJSONObject(0).getDouble("amount"))+"/"+
                            mSchemeArray.getJSONObject(0).getString("scheme"));
                }
                boost.setItemList(objectInfoList);
                myItemList.add(boost);
            }

            if(myItemList.size() > 0){
                if(myItemList.size() < limit){
                    isScroll = false;
                }
                if(offset == 0){
                    myItemAdapter.notifyDataSetChanged();
                }else{
                    recyclerView.post(new Runnable() {
                        public void run() {
                            myItemAdapter.notifyItemRangeInserted(offset,limit);
                            loading = false;
                        }
                    });
                    Log.d(TAG, "NEXT ITEMS LOADED");
                }
            }
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
        if(type == 2){
            Boost boost =(Boost) myItemList.get(parentPosition);
            MyLocation myLocation = (MyLocation)boost.getItemList().get(this.position);
            myLocation.setLocation(diallogItemList.get(position));
            myItemAdapter.notifyItemChanged(parentPosition);
            location = myLocation.getLocation();
            alertDialog.dismiss();
        }else{
            if(sharedPreferences.getString(Constants.USER_TYPE,"").equals("agency")){
                appliedAgencyBoost(myItemList.get(position));
            }else{
                applyModelBoost((Boost)myItemList.get(position));
            }
        }
    }

    @Override
    public void onItemClicked(int parentPosition, int position, int type) {

        if(type == 1){
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
        }else if(type == 2){
            this.parentPosition = parentPosition;
            this.position = position;
            openLocationDialog();
        }

    }

    private void openLocationDialog(){
        Boost boost =(Boost) myItemList.get(parentPosition);
        diallogItemList.clear();
        MyLocation myLocation1 = null;
        for(Object ob : boost.getLocationList()){
            myLocation1 = (MyLocation)ob;
            diallogItemList.add(myLocation1.getLocation());
        }
        int view=R.layout.info_input_dialog_layout;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setCancelable(false)
                .setView(view);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        //   alertDialog.setContentView(inflater.inflate(R.layout.update_email_dialog_view, null));
        alertDialog.show();

        final TextView textName=(TextView) alertDialog.findViewById(R.id.tv_info_header);
        // final ImageView btnCancel= alertDialog.findViewById(R.id.image_clear);
        recyclerViewDialog=alertDialog.findViewById(R.id.recycler_view_info);

        recyclerViewDialog.setHasFixedSize(true);
        recyclerViewDialog.setLayoutManager(new LinearLayoutManager(this));
        dialogListAdapter = new SimpleItemAdapter(this,diallogItemList);
        textName.setText("Select Location");
        dialogListAdapter.setMyItemClickListener(this);
        recyclerViewDialog.setAdapter(dialogListAdapter);

        // show it
        alertDialog.show();
    }

}
