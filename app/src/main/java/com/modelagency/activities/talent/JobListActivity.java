package com.modelagency.activities.talent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.modelagency.R;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.adapters.JobListAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.MyJob;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobListActivity extends NetworkBaseActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private JobListAdapter myItemAdapter;
    private List<MyJob> myItemList;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);
        init();
        initFooter(this,0);
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
        myItemAdapter=new JobListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        getItemList();
    }

    private void getItemList(){
        MyJob item = null;
        /*for(int i=0; i<20; i++){
            item = new MyJob();
            item.setTitle("Youtiful is seeking real people of all types and ages for new campaign");
            item.setLocation("Delhi");
            item.setCloseDate("Sat, 28 Sep 2019");
            item.setLocalImage(R.drawable.model);
            myItemList.add(item);
        }*/

        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("location",sharedPreferences.getString(Constants.LOCATION,""));
        String url = null;
        if(flag.equals("applied")){
            url = getResources().getString(R.string.url)+Constants.GET_APPLIED_JOB_MODEL+"?id="+sharedPreferences.getString(Constants.USER_ID,"");
        }else{
            url = getResources().getString(R.string.url)+Constants.GET_JOBS_FOR_MODEL+"?myLocation=Delhi";
        }

        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getJobs");

    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getJobs")){
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject dataObject = null;
                    MyJob item = null;
                    int len = jsonArray.length();
                    myItemList.clear();
                    for(int i=0; i<len; i++){
                        dataObject = jsonArray.getJSONObject(i);
                        item = new MyJob();
                        item.setId(dataObject.getString("id"));
                        item.setTitle(dataObject.getString("title"));
                        item.setLocation(dataObject.getString("location"));
                        item.setCompensation(dataObject.getString("compensation"));
                        item.setGenres(dataObject.getString("genres"));
                        item.setDescription(dataObject.getString("description"));
                        item.setPreferences(dataObject.getString("preferences"));
                        item.setImageUrl(dataObject.getString("bannerImage"));
                       // item.setCloseDate("Sat, 28 Sep 2019");
                        item.setCloseDate(Utility.parseDate(dataObject.getString("closeDate"),"yyyy-MM-dd",
                                "EEE, dd MMM yyyy"));
                        item.setLocalImage(R.drawable.model);
                        myItemList.add(item);
                    }

                    if(len > 0){
                        myItemAdapter.notifyDataSetChanged();
                    }else{
                        recyclerView.setVisibility(View.GONE);
                        showError(true,"Currently no jobs available. Please try again later.");
                    }
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(int position, int type) {
        Intent intent = new Intent(JobListActivity.this,JobDetailActivity.class);
        intent.putExtra("job",myItemList.get(position));
        intent.putExtra("flag",flag);
        startActivity(intent);
    }
}
