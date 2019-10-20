package com.talentnew.activities.common;

import android.os.Bundle;

import com.android.volley.Request;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.talentnew.R;
import com.talentnew.adapters.NotificationAdapter;
import com.talentnew.models.MyNotification;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationActivity extends NetworkBaseActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter myItemAdapter;
    private List<MyNotification> myItemList;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
       // Toolbar toolbar = findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        setToolbarDetails(this);
        initFooter(this,10);
        init();
    }

    private void init(){
        flag = getIntent().getStringExtra("flag");
        myItemList = new ArrayList<>();
        // getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new NotificationAdapter(this,myItemList);
        recyclerView.setAdapter(myItemAdapter);

        if(isNetworkAvailable()){
            getNotifications();
        }
    }

    private void getNotifications(){
        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        String url = getResources().getString(R.string.url)+Constants.GET_NOTIFICATIONS+"?userId="+
                sharedPreferences.getString(Constants.USER_ID,"")+"&userType="+
                sharedPreferences.getString(Constants.USER_TYPE,"");

        showProgress(true);
        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(params),"getNotification");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getNotification")){
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject dataObject = null;
                    MyNotification item = null;
                    int len = jsonArray.length();
                    myItemList.clear();
                    for(int i=0; i<len; i++){
                        dataObject = jsonArray.getJSONObject(i);
                        item = new MyNotification();
                        item.setMessage(dataObject.getString("message"));
                        item.setCreatedDate(Utility.parseDate(dataObject.getString("createdDate"),"yyyy-MM-dd",
                                "EEE, dd MMM yyyy"));
                        myItemList.add(item);
                    }

                    if(len > 0){
                        myItemAdapter.notifyDataSetChanged();
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        showError(true,"Currently no notification available.");
                    }
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

}
