package com.talentnew.activities.talent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.talentnew.R;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.adapters.JobListAdapter;
import com.talentnew.interfaces.MyItemClickListener;
import com.talentnew.models.MyJob;
import com.talentnew.utilities.Constants;
import com.talentnew.utilities.Utility;

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
        final RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new JobListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
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

        getItemList();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    private void getItemList(){
        loading = true;
        Map<String,String> params = new HashMap<>();
        params.put("id",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("location",sharedPreferences.getString(Constants.LOCATION,""));
        params.put("limit",""+limit);
        params.put("offset",""+offset);
        String url = null;
        if(flag.equals("applied")){
            url = getResources().getString(R.string.url)+Constants.GET_APPLIED_JOB_MODEL;
        }else{
            url = getResources().getString(R.string.url)+Constants.GET_JOBS_FOR_MODEL;
        }

        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"getJobs");

    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("getJobs")){
                loading = false;
                if(jsonObject.getBoolean("status")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject dataObject = null;
                    MyJob item = null;
                    int len = jsonArray.length();
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
        intent.putExtra("position",position);
        intent.putExtra("flag",flag);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(data != null){
                int position = data.getIntExtra("position",-1);
                if(position >= 0){
                    myItemList.remove(position);
                    myItemAdapter.notifyItemRemoved(position);
                }

            }
        }
    }
}
