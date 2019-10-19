package com.modelagency.activities.agency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.modelagency.R;
import com.modelagency.activities.common.BaseImageActivity;
import com.modelagency.activities.common.NetworkBaseActivity;
import com.modelagency.activities.talent.JobDetailActivity;
import com.modelagency.activities.talent.JobListActivity;
import com.modelagency.adapters.JobListAdapter;
import com.modelagency.adapters.ViewJobListAdapter;
import com.modelagency.interfaces.MyItemClickListener;
import com.modelagency.models.MyJob;
import com.modelagency.utilities.Constants;
import com.modelagency.utilities.DialogAndToast;
import com.modelagency.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentProfileActivity extends BaseImageActivity implements MyItemClickListener {

    private RecyclerView recyclerView;
    private ViewJobListAdapter myItemAdapter;
    private List<MyJob> myItemList;
    private EditText et_company_name;
    private ImageView iv_edit, iv_upload_banner;
    private CircleImageView iv_profile_pic;
    private TextView tv_save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_profile);
        initFooter(this, 4);
        setToolbarDetails(this);
        initViews();
    }

    @Override
    public void onResume(){
        super.onResume();
        getItemList();
    }

    private void initViews(){
        iv_upload_banner = findViewById(R.id.iv_upload_banner);
        iv_profile_pic = findViewById(R.id.iv_profile_pic);
        iv_upload_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        et_company_name = findViewById(R.id.et_company_name);
        iv_edit = findViewById(R.id.iv_edit);
        iv_edit.setVisibility(View.VISIBLE);
        tv_save = findViewById(R.id.tv_save);
        et_company_name.setText(sharedPreferences.getString(Constants.USERNAME, ""));
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_edit.setVisibility(View.GONE);
                tv_save.setVisibility(View.VISIBLE);
                et_company_name.setFocusable(true);
            }
        });

        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_edit.setVisibility(View.VISIBLE);
                tv_save.setVisibility(View.GONE);
                et_company_name.setFocusable(false);

                String  comName = et_company_name.getText().toString();
                String image = convertToBase64(new File(imagePath));
                if(!TextUtils.isEmpty(comName)) {
                    DialogAndToast.showDialog("Image " +image +" name " +comName , AgentProfileActivity.this);
                }
            }
        });

        myItemList = new ArrayList<>();
        // getItemList();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemAdapter=new ViewJobListAdapter(this,myItemList);
        myItemAdapter.setMyItemClickListener(this);
        recyclerView.setAdapter(myItemAdapter);
    }

    private void getItemList(){
        MyJob item = null;
        for(int i=0; i<20; i++){
            item = new MyJob();
            item.setTitle("Youtiful is seeking real people of all types and ages for new campaign");
            item.setLocation("Delhi");
            item.setCloseDate("Sat, 28 Sep 2019");
            item.setApplicationCount(15);
            item.setViewCount(15);
            item.setLocalImage(R.drawable.model);
            myItemList.add(item);
        }

        Map<String,String> params = new HashMap<>();
        String id = sharedPreferences.getString(Constants.USER_ID,"");
        //params.put("location",sharedPreferences.getString(Constants.LOCATION,""));
        String url = getResources().getString(R.string.url)+Constants.GET_JOBS_FOR_AGENCY+"?id="+id;
        showProgress(true);
        jsonObjectApiRequest(Request.Method.GET,url,new JSONObject(params),"getJobs");

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
                        item.setAgencyId(dataObject.getString("agencyId"));
                        item.setViewCount(dataObject.getInt("views"));
                        item.setViewCount(dataObject.getInt("application"));
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
        if(type==1){
            Log.d("clicked ", myItemList.get(position).getApplicationCount()+"");
            Intent intent = new Intent(AgentProfileActivity.this, ViewJobApplication.class);
            intent.putExtra("job",myItemList.get(position));
            startActivity(intent);
        }
    }

    @Override
    protected void imageAdded() {
        super.imageAdded();
        Glide.with(this)
                .load(imagePath)
                .into(iv_profile_pic);
    }
}
