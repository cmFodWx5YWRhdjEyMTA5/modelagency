package com.talentnew.activities.talent;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.talentnew.R;
import com.talentnew.activities.common.BoostActivity;
import com.talentnew.activities.common.NetworkBaseActivity;
import com.talentnew.models.MyJob;
import com.talentnew.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JobDetailActivity extends NetworkBaseActivity {

    private MyJob myJob;
    private Button btn_apply;

    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
       // Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        setToolbarDetails(this);
        //initFooter(this,0);
        init();
    }

    private void init(){
        myJob = (MyJob)getIntent().getSerializableExtra("job");
        TextView tv_job = findViewById(R.id.tv_job);
        tv_job.setText(myJob.getLocation());
        TextView tv_close_date = findViewById(R.id.tv_close_date);
        tv_close_date.setText(myJob.getCloseDate());
        TextView tv_preferences = findViewById(R.id.tv_preferences);
        tv_preferences.setText(myJob.getPreferences());
        TextView tv_genre = findViewById(R.id.tv_genre);
        tv_genre.setText(myJob.getGenres());
        TextView tv_compensation = findViewById(R.id.tv_compensation);
        tv_compensation.setText("Rs "+String.format("%.00f",Float.parseFloat(myJob.getCompensation()))+" for work");
        TextView tv_desc = findViewById(R.id.tv_desc);
        tv_desc.setText(myJob.getDescription());
        flag = getIntent().getStringExtra("flag");

        ImageView iv_image = findViewById(R.id.iv_image);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        // requestOptions.dontTransform();
        //requestOptions.override(Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size),
        //        context), Utility.dpToPx((int)context.getResources().getDimension(R.dimen.job_list_image_size), context));
        requestOptions.centerCrop();
        requestOptions.skipMemoryCache(false);
        Glide.with(this)
                .load(myJob.getImageUrl())
                .apply(requestOptions)
                .error(R.drawable.model_2)
                .into(iv_image);

        btn_apply = findViewById(R.id.btn_apply);

        if(flag.equals("applied")){
            btn_apply.setEnabled(false);
            btn_apply.setText("APPLIED");
        }else{
            if(isNetworkAvailable()){
                updateView();
            }
        }

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getInt(Constants.BOOST_APPLY_JOB,0) == 0){
                    showMyBothDialog("Please buy a subscription to apply for the job.","NO","YES",1);
                }else{
                    if(isNetworkAvailable()){
                        applyJob();
                    }else{
                        showMyAlertDialog(getResources().getString(R.string.no_internet));
                    }
                }

            }
        });

    }

    private void applyJob(){
        Map<String,String> params = new HashMap<>();
        params.put("jobId",myJob.getId());
        params.put("modelId",sharedPreferences.getString(Constants.USER_ID,""));
        params.put("userName",sharedPreferences.getString(Constants.USERNAME,""));
        String url = getResources().getString(R.string.url)+Constants.APPLY_JOB;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"applyJob");
    }

    private void updateView(){
        Map<String,String> params = new HashMap<>();
        params.put("id",myJob.getId());
        String url = getResources().getString(R.string.url)+Constants.UPDATE_VIEWS;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.POST,url,new JSONObject(params),"updateViews");
    }

    @Override
    public void onJsonObjectResponse(JSONObject jsonObject, String apiName) {
        try{
            if(apiName.equals("applyJob")){
                if(jsonObject.getBoolean("status")){
                    int applyJob = sharedPreferences.getInt(Constants.BOOST_APPLY_JOB,0);
                    if(applyJob > 0){
                        applyJob--;
                        editor.putInt(Constants.BOOST_APPLY_JOB,applyJob);
                        editor.commit();
                    }
                    showMyDialog(jsonObject.getString("message"));
                    btn_apply.setEnabled(false);
                    btn_apply.setText("APPLIED");
                }else{
                    showMyAlertDialog(jsonObject.getString("message"));
                }
            }
        }catch (JSONException error){
            error.printStackTrace();
        }
    }

    @Override
    public void onDialogPositiveClicked(){
        Intent intent = new Intent();
        intent.putExtra("position",getIntent().getIntExtra("position",-1));
        setResult(-1,intent);
        finish();
    }

    @Override
    public void onDialogPositiveClicked(int type){
       Intent intent = new Intent(JobDetailActivity.this, BoostActivity.class);
       startActivity(intent);
    }

    @Override
    public void onDialogNegativeClicked(int type){

    }

}
